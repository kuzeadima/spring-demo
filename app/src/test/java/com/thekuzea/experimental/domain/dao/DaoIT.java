package com.thekuzea.experimental.domain.dao;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import net.ttddyy.dsproxy.asserts.ProxyTestDataSource;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.thekuzea.experimental.config.DbUnitConfig;
import com.thekuzea.experimental.config.PersistenceConfig;
import com.thekuzea.experimental.config.ProxyDataSourceConfig;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("it")
@ComponentScan(basePackages = "com.thekuzea.experimental.domain.dao")
@Import(value = {
        ProxyDataSourceConfig.class,
        PersistenceConfig.class,
        DbUnitConfig.class
})
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureTestEntityManager
@TestExecutionListeners(listeners = {
        DependencyInjectionTestExecutionListener.class,
        TransactionDbUnitTestExecutionListener.class,
        DbUnitTestExecutionListener.class
})
@DbUnitConfiguration(databaseConnection = "dbUnitDatabaseConnection")
public abstract class DaoIT {

    @Autowired
    protected ProxyTestDataSource dataSource;

    @Autowired
    protected TestEntityManager entityManager;

    @Before
    public void setUp() {
        dataSource.reset();
    }
}
