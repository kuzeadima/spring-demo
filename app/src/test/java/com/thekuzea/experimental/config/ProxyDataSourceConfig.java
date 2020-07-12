package com.thekuzea.experimental.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.thekuzea.experimental.config.resource.ProxyDataSourceBeanPostProcessor;

@TestConfiguration
public class ProxyDataSourceConfig {

    @Bean
    public ProxyDataSourceBeanPostProcessor proxyTestDataSourceBeanPostProcessor() {
        return new ProxyDataSourceBeanPostProcessor();
    }
}
