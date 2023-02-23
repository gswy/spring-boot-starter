package xin.wanyun.queue;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.time.Duration;
import java.util.*;

@Slf4j
public class QueueTemplate {

    @Autowired
    QueueProperties queueProperties;

    @Autowired
    StringRedisTemplate redisTemplate;

    /**
     * 初始化队列
     */
    public QueueTemplate(QueueProperties queueProperties) {
        this.queueProperties = queueProperties;
        for(int i = 0; i < queueProperties.getNumber(); i++) {
            int number = i;
            new Thread(() -> {
                log.info("启动线程：" + number);
                while (true) {
                    if (Objects.isNull(redisTemplate)) continue;
                    Job job = pop();
                    if (Objects.nonNull(job)) {
                        try {
                            Class<?> aClass = Class.forName(job.getClassName());
                            ShouldQueue queue = (ShouldQueue) JSON.parseObject(job.getData(), aClass);
                            queue.handle();
                        } catch (ClassNotFoundException e) {
                            log.error("Task class does not exist! ClassName is " + job.getClassName());
                        } catch (QueueException e) {
                            log.error("Task execution failed! Error message:" + e.getMessage());
                        }
                    } else {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ignored) {}
                    }
                }
            }).start();
        }
    }

    /**
     * 分发队列
     * @param shouldQueue 分发任务
     */
    public void dispatch(ShouldQueue shouldQueue) {
        Job job = baseDispatch(shouldQueue);
        // 计算时间
        long expired = Objects.isNull(shouldQueue.getDuration()) ? 0 : shouldQueue.getDuration().toMillis() + System.currentTimeMillis();
        redisTemplate.opsForZSet().add(getKeyName(), JSON.toJSONString(job), expired);
    }

    /**
     * 分发延迟队列
     *
     * @param shouldQueue 分发任务
     * @param duration 延迟时间
     */
    public void dispatch(ShouldQueue shouldQueue, Duration duration) {
        Job job = baseDispatch(shouldQueue);
        // 计算时间
        long expired = duration.toMillis() + System.currentTimeMillis();
        System.out.println("执行时间" + expired);
        redisTemplate.opsForZSet().add(getKeyName(), JSON.toJSONString(job), expired);
    }

    /**
     * 生成任务
     * @return 任务类实例
     */
    private Job baseDispatch(ShouldQueue shouldQueue) {
        String className = shouldQueue.getClass().getName();
        String data = JSON.toJSONString(shouldQueue);
        String id = getRandomId();
        Job job = new Job();
        job.setId(id);
        job.setClassName(className);
        job.setMaxTries(shouldQueue.getMaxTries());
        job.setMaxExceptions(job.getMaxExceptions());
        job.setDuration(shouldQueue.getDuration());
        job.setData(data);
        return job;
    }

    /**
     * 生成任务唯一ID
     *
     * @return 返回ID
     */
    public String getRandomId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * Redis操作Key名称
     * @return 返回队列KeyName
     */
    public String getKeyName() {
        return queueProperties.getPrefix() + ":queue";
    }

    /**
     * 获取到时间的任务
     */
    private Job pop() {
        // 定义Lua脚本
        String script = "local delayKey = KEYS[1]\n" +
                "local start = ARGV[1]\n" +
                "local endTime = ARGV[2]\n" +
                "local limitNum = ARGV[3]\n" +
                "local result = redis.call('zrangebyscore', delayKey, start, endTime, 'limit', 0, limitNum)\n" +
                "if next(result) ~= nil\n" +
                "then\n" +
                "    local res = redis.call('zrem',delayKey,unpack(result))\n" +
                "    if res > 0\n" +
                "    then\n" +
                "        return result\n" +
                "    end\n" +
                "else\n" +
                "    return {}\n" +
                "end";

        // 定义Key参数和Arg参数
        List<String> keys = Collections.singletonList(getKeyName());

        // 执行脚本
        DefaultRedisScript<Object> redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(Object.class);
        redisScript.setScriptText(script);
        Object result = redisTemplate.execute(redisScript, keys, "0", String.valueOf(System.currentTimeMillis()), "1");
        if (Objects.nonNull(result)) {
            return JSON.parseObject(result.toString(), Job.class);
        }
        return null;
    }
}
