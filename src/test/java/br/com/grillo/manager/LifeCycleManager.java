package br.com.grillo.manager;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.MySQLContainer;

public class LifeCycleManager implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    private static MySQLContainer sqlContainer;

    static {
        sqlContainer = new MySQLContainer("mysql:8.0.20")
                .withDatabaseName("integration-tests-db")
                .withUsername("sa")
                .withPassword("sa");
        sqlContainer.start();
    }

    public void stop() {
        if (sqlContainer != null) {
            sqlContainer.stop();
        }
    }

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        TestPropertyValues
                .of("spring.datasource.url=" + sqlContainer.getJdbcUrl(),
                        "spring.datasource.username=" + sqlContainer.getUsername(),
                        "spring.datasource.password=" + sqlContainer.getPassword())
                .applyTo(configurableApplicationContext.getEnvironment());
    }

}