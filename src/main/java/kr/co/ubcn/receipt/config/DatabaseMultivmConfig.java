package kr.co.ubcn.receipt.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages= "kr.co.ubcn.receipt.mapper.multivm" , sqlSessionFactoryRef =  "multivmSqlSessionFactory")
@EnableTransactionManagement
public class DatabaseMultivmConfig {
    @Bean(name="multivmDataSource")
    @ConfigurationProperties(prefix = "spring.multivm.datasource")

    public DataSource multivmDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean(name = "multivmSqlSessionFactory")

    public SqlSessionFactory multivmSqlSessionFactory(@Qualifier("multivmDataSource") DataSource multivmDataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(multivmDataSource);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sessionFactory.setMapperLocations(resolver.getResources("classpath:mybatis/multivm/*.xml"));
        sessionFactory.setConfigLocation(resolver.getResource("classpath:mybatis/mybatis-config.xml" ));
        return sessionFactory.getObject();
    }

    @Bean(name = "multivmTransactionManager")

    public PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(multivmDataSource());
        transactionManager.setGlobalRollbackOnParticipationFailure(false);
        return transactionManager;
    }

    @Bean(name = "multivmSqlSessionTemplate")

    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("multivmSqlSessionFactory") SqlSessionFactory multivmSqlSessionFactory) throws Exception {
        final SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(multivmSqlSessionFactory);
        return sqlSessionTemplate;
    }
}
