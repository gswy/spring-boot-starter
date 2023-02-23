# SpringBoot队列包

这是一个基于SpringBoot的异步队列包，队列驱动为Redis。当Application启动时，自动启动队列监听，可配置队列消费线程数。

## 快速使用
控制器
```java
@RestController
public class IndexController {

    @Autowired
    QueueTemplate queueTemplate;

    @GetMapping("/")
    public ResponseEntity<Object> index() {
        queueTemplate.dispatch(new SmsQueue("发送短信"), TimeUnit.DAYS);
        return ResponseEntity.ok("发送成功");
    }
}
```

队列类
```java
@Data
public class SmsQueue implements ShouldQueue {

    private String phone;

    public SmsQueue(String phone) {
        this.phone = phone;
    }

    @Override
    public void handle() throws QueueException {
        System.out.println("消费短信啦，给" + phone + "发送短信");
    }
}
```

其他特性请参考源码。