package xin.wanyun.queue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Job {

    /**
     * 队列ID
     */
    private String id;

    /**
     * 类名
     */
    private String className;

    /**
     * 当前执行次数
     */
    private Integer current;

    /**
     * 重试次数
     */
    private Integer maxTries;

    /**
     * 最大错误次数
     */
    private Integer maxExceptions;

    /**
     * 延时时间
     */
    private Duration duration;

    /**
     * 序列化类数据
     */
    private String data;

}
