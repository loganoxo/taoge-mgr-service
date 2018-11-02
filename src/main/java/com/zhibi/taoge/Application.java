package com.zhibi.taoge;

//import com.zhibi.xiuba.rocketmq.annotation.EnableRocketMQConsume;
//import com.zhibi.xiuba.rocketmq.annotation.EnableRocketMQProducer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableDiscoveryClient
@EnableFeignClients
//@EnableRocketMQProducer
//@EnableRocketMQConsume
@EnableAsync
@EnableHystrixDashboard
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
