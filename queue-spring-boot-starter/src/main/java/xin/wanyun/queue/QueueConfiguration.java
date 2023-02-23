package xin.wanyun.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(QueueProperties.class)
@ConditionalOnClass(QueueTemplate.class)
public class QueueConfiguration {

    @Autowired
    private QueueProperties queueProperties;

    @Bean
    @ConditionalOnMissingBean
    public QueueTemplate queueTemplate() {
        return new QueueTemplate(queueProperties);
    }

}
