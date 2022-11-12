package com.itjing.multi;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author lijing
 * @date 2022年06月16日 20:37
 * @description 支持动态切换的数据源
 * 通过重写 determineCurrentLookupKey 实现数据源切换
 */
public class RoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return RoutingDataSourceContext.getRoutingKey();
    }

}