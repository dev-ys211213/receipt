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
@MapperScan(basePackages= "kr.co.ubcn.receipt.mapper.bingo" , sqlSessionFactoryRef =  "bingoSqlSessionFactory")
@EnableTransactionManagement
public class DatabaseBingoConfig {
    @Bean(name="bingoDataSource")
    @ConfigurationProperties(prefix = "spring.bingo.datasource")

    public DataSource bingoDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean(name = "bingoSqlSessionFactory")

    public SqlSessionFactory bingoSqlSessionFactory(@Qualifier("bingoDataSource") DataSource bingoDataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(bingoDataSource);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sessionFactory.setMapperLocations(resolver.getResources("classpath:mybatis/bingo/*.xml"));
        sessionFactory.setConfigLocation(resolver.getResource("classpath:mybatis/mybatis-config.xml" ));
        return sessionFactory.getObject();
    }

    @Bean(name = "bingoTransactionManager")

    public PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(bingoDataSource());
        transactionManager.setGlobalRollbackOnParticipationFailure(false);
        return transactionManager;
    }

    @Bean(name = "bingoSqlSessionTemplate")

    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("bingoSqlSessionFactory") SqlSessionFactory bingoSqlSessionFactory) throws Exception {
        final SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(bingoSqlSessionFactory);
        return sqlSessionTemplate;
    }
}
