package com.zhibi.taoge.config.data;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

/**
 * Created by QinHe on 2018-07-24.
 */
@Configuration
public class DruidDataSourceConfig {

    /**
     * 主DataSource 配置
     */
    @Primary
    @Bean(name = "primaryDruidDataSource")
    @ConfigurationProperties("spring.datasource.druid")
    public DruidDataSource primaryDruidDataSource(Environment environment) {
//        DruidDataSourceWrapperPrimary druidDataSourceWrapperPrimary = new DruidDataSourceWrapperPrimary();
//        return druidDataSourceWrapperPrimary;
        return DruidDataSourceBuilder.create().build(environment, "spring.datasource.druid.");
    }


//    /**
//     * 从DataSource 配置
//     */
//    @Bean(name = "slaveDruidDataSource")
//    @ConfigurationProperties("spring.datasource.druid.slave")
//    public DruidDataSource slaveDruidDataSource(Environment environment) {
////        DruidDataSourceWrapperSlave druidDataSourceWrapperSlave = new DruidDataSourceWrapperSlave();
////        return druidDataSourceWrapperSlave;
//        return DruidDataSourceBuilder.create().build(environment, "spring.datasource.druid.slave.");
//    }


}
