package com.itjing.multi.config;

import com.itjing.multi.RoutingDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lijing
 * @date 2022年06月16日 20:39
 * @description 数据源配置
 * 把多个数据源，装配到一个 RoutingDataSource 里
 */
@Configuration
@MapperScan("com.itjing.multi.mapper")
public class RoutingDataSourcesConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.db1")
    public DataSource db1DataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.db2")
    public DataSource db2DataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean
    public RoutingDataSource routingDataSource() {
        RoutingDataSource routingDataSource = new RoutingDataSource();
        // 默认数据源为db1
        routingDataSource.setDefaultTargetDataSource(db1DataSource());
        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put("db1", db1DataSource());
        dataSourceMap.put("db2", db2DataSource());
        routingDataSource.setTargetDataSources(dataSourceMap);
        return routingDataSource;
    }

}
