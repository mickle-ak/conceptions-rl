package org.mickleak.testcontainerspostgreflyway.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;


@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "org.mickleak.testcontainerspostgreflyway.infrastructure.persistence",
        entityManagerFactoryRef = "conceptEntityManagerFactory",
        transactionManagerRef = "conceptTransactionManager"
)
public class ConceptDataSourceConfig {
    @Bean
    @ConfigurationProperties("concept.datasource")
    public DataSourceProperties conceptDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "conceptDataSource")
    public DataSource gvDataSource(@Qualifier("conceptDataSourceProperties") DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean(name = "conceptEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean gvEntityManagerFactory(
            @Qualifier("conceptDataSource") DataSource dataSource,
            Environment env
    ) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan("org.mickleak.testcontainerspostgreflyway.infrastructure.persistence");
        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Map<String, Object> props = new HashMap<>();
        putIfPresent(env, props, "concept.jpa.properties.hibernate.dialect", "hibernate.dialect");
        putIfPresent(env, props, "concept.jpa.properties.hibernate.default_schema", "hibernate.default_schema");
        putIfPresent(env, props, "concept.jpa.hibernate.ddl-auto", "hibernate.hbm2ddl.auto");
        putIfPresent(env, props, "concept.jpa.show-sql", "hibernate.show_sql");
        putIfPresent(env, props, "concept.jpa.properties.hibernate.temp.use_jdbc_metadata_defaults", "hibernate.temp.use_jdbc_metadata_defaults");

        // Damit beim Mapping der DB-Columns Snake-Case verwendet wird
        props.put("hibernate.physical_naming_strategy", CamelCaseToUnderscoresNamingStrategy.class.getName());

        emf.setJpaPropertyMap(props);

        return emf;
    }

    private static void putIfPresent(Environment env, Map<String, Object> map, String property, String targetKey) {
        String value = env.getProperty(property);
        if (value != null) {
            map.put(targetKey, value);
        }
    }

    @Bean(name = "conceptTransactionManager")
    public PlatformTransactionManager gvTransactionManager(
            @Qualifier("conceptEntityManagerFactory") EntityManagerFactory emf
    ) {
        return new JpaTransactionManager(emf);
    }

}
