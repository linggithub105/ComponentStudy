package com.itjing.multi.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author lijing
 * @date 2022年06月15日 20:19
 * @description db1数据源配置
 */
@Configuration
// 扫描mapper接口包路径
@MapperScan(basePackages = "com.itjing.multi.mapper.db1", sqlSessionTemplateRef = "db1SqlSessionTemplate")
public class DB1DataSourceConfig {

    /**
     * 配置连接池，这里直接new一个Druid连接池
     * 也可以new其他的连接池，比如spring boot默认的hikari连接池
     */
    @Bean(name = "db1DataSource")
    @ConfigurationProperties(prefix = "spring.datasource.db1")
    @Primary
    public DataSource setDataSource() {
        return new DruidDataSource();
    }

    /**
     * 事务配置
     * @param dataSource
     * @return
     */
    @Bean(name = "db1TransactionManager")
    @Primary
    public DataSourceTransactionManager setTransactionManager(@Qualifier("db1DataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(new DruidDataSource());
    }

    /**
     * 配置sessionFactory，这里的多数据源就是每个数据源对应一个sessionFactory
     * 下面getResources的就是mapper.xml文件
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Bean(name = "db1SqlSessionFactory")
    @Primary
    public SqlSessionFactory setSqlSessionFactory(@Qualifier("db1DataSource") DataSource dataSource) throws Exception {
        // 如果用myBatis plus的话配置类中SqlSessionFactoryBean bean = new SqlSessionFactoryBean()
        // 换成MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean()
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/db1/*.xml"));
        return bean.getObject();
    }

    /**
     * 配置SqlSessionTemplate
     * @param sqlSessionFactory
     * @return
     */
    @Bean(name = "db1SqlSessionTemplate")
    @Primary
    public SqlSessionTemplate setSqlSessionTemplate(@Qualifier("db1SqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
