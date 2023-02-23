package xin.wanyun.queue;

import java.io.Serializable;
import java.time.Duration;

/**
 * 队列基础类
 */
public interface ShouldQueue extends Serializable {

    /**
     * 队列名称
     * （队列名称用于区分被消费的队列分类，默认情况下为default。）
     *
     * @return 返回队列名称
     */
    public default String getName() {
        return "default";
    }

    /**
     * 队列重试次数
     * （当队列抛出异常时，该队列将被再次执行的次数）
     * @return 重试次数
     */
    public default Integer getMaxTries() {
        return null;
    }

    /**
     * 队列可错误次数
     * （当队列执行错误时，队列将被再次执行，与getMaxTries搭配使用。
     * getMaxTries返回null时，此配置失效。）
     *
     * @return 返回最大错误次数
     */
    public default Integer getMaxExceptions() {
        return null;
    }

    /**
     * 延时队列
     * （此配置返回null时，队列立即执行。dispatch时的时间优先级大于此处延迟时间）
     *
     * @return 返回需要延迟的时间。
     */
    public default Duration getDuration() {
        return null;
    }

    /**
     * 队列执行内容
     *
     * @throws QueueException 返回队列执行错误Exception
     */
    public void handle() throws QueueException;
}
