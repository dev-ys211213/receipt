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
@MapperScan(basePackages= "kr.co.ubcn.receipt.mapper.vanon" , sqlSessionFactoryRef =  "vanonSqlSessionFactory")
@EnableTransactionManagement
public class DatabaseVanonConfig {
    @Bean(name="vanonDataSource")
    @ConfigurationProperties(prefix = "spring.vanon.datasource")

    public DataSource vanonDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean(name = "vanonSqlSessionFactory")

    public SqlSessionFactory vanonSqlSessionFactory(@Qualifier("vanonDataSource") DataSource vanonDataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(vanonDataSource);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sessionFactory.setMapperLocations(resolver.getResources("classpath:mybatis/vanon/*.xml"));
        sessionFactory.setConfigLocation(resolver.getResource("classpath:mybatis/mybatis-config.xml" ));
        return sessionFactory.getObject();
    }

    @Bean(name = "vanonTransactionManager")

    public PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(vanonDataSource());
        transactionManager.setGlobalRollbackOnParticipationFailure(false);
        return transactionManager;
    }

    @Bean(name = "vanonSqlSessionTemplate")

    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("vanonSqlSessionFactory") SqlSessionFactory vanonSqlSessionFactory) throws Exception {
        final SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(vanonSqlSessionFactory);
        return sqlSessionTemplate;
    }
}
