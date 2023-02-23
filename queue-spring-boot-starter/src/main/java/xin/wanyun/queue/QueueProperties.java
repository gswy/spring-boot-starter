package xin.wanyun.queue;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "queue")
public class QueueProperties {

    /**
     * 队列Key前缀，当一台服务器上存在多个应用时，区分不同应用的队列前缀。
     */
    private String prefix = "app";

    /**
     * 消费者线程数量
     */
    private Integer number = 1;

}
