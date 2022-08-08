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
@MapperScan(basePackages= "kr.co.ubcn.receipt.mapper.vmms" , sqlSessionFactoryRef =  "vmmsSqlSessionFactory")
@EnableTransactionManagement
public class DatabaseVmmsConfig {
    @Bean(name="vmmsDataSource")
    @ConfigurationProperties(prefix = "spring.vmms.datasource")

    public DataSource vmmsDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean(name = "vmmsSqlSessionFactory")

    public SqlSessionFactory vmmsSqlSessionFactory(@Qualifier("vmmsDataSource") DataSource vmmsDataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(vmmsDataSource);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sessionFactory.setMapperLocations(resolver.getResources("classpath:mybatis/vmms/*.xml"));
        sessionFactory.setConfigLocation(resolver.getResource("classpath:mybatis/mybatis-config.xml" ));
        return sessionFactory.getObject();
    }

    @Bean(name = "vmmsTransactionManager")

    public PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(vmmsDataSource());
        transactionManager.setGlobalRollbackOnParticipationFailure(false);
        return transactionManager;
    }

    @Bean(name = "vmmsSqlSessionTemplate")

    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("vmmsSqlSessionFactory") SqlSessionFactory vmmsSqlSessionFactory) throws Exception {
        final SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(vmmsSqlSessionFactory);
        return sqlSessionTemplate;
    }
}
