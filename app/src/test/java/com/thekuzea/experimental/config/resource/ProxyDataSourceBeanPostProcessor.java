package com.thekuzea.experimental.config.resource;

import java.util.concurrent.TimeUnit;
import javax.sql.DataSource;

import net.ttddyy.dsproxy.asserts.ProxyTestDataSource;
import net.ttddyy.dsproxy.listener.logging.SLF4JLogLevel;
import net.ttddyy.dsproxy.support.ProxyDataSource;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class ProxyDataSourceBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(final Object bean, final String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException {
        if (bean instanceof DataSource) {
            final ProxyDataSource proxyDataSource = ProxyDataSourceBuilder.create((DataSource) bean)
                    .logQueryBySlf4j(SLF4JLogLevel.INFO)
                    .logSlowQueryBySlf4j(1, TimeUnit.MINUTES, SLF4JLogLevel.WARN)
                    .countQuery()
                    .build();

            return new ProxyTestDataSource(proxyDataSource);
        }

        return bean;
    }
}
