package com.itjing;

import cn.smallbun.screw.core.Configuration;
import cn.smallbun.screw.core.engine.EngineConfig;
import cn.smallbun.screw.core.engine.EngineFileType;
import cn.smallbun.screw.core.engine.EngineTemplateType;
import cn.smallbun.screw.core.execute.DocumentationExecute;
import cn.smallbun.screw.core.process.ProcessConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Title: JAVA 生成数据库文档
 * @Description: JAVA 生成数据库文档
 * @author: lijing
 * @Date: 2021.4.28
 * @Version: 1.0
 */
public class Generation {

    public static void main(String[] args) {
        //数据源
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("com.mysql.jdbc.Driver");
        hikariConfig.setJdbcUrl("jdbc:mysql://10.10.10.75:3306/jngj?useUnicode=true&characterEncoding=utf8&characterSetResults=utf8");
        hikariConfig.setUsername("root");
        hikariConfig.setPassword("root");

        //设置可以获取tables remarks信息
        hikariConfig.addDataSourceProperty("cacheServerConfiguration", "true");
        hikariConfig.addDataSourceProperty("alwaysSendSetIsolation", "false");
        hikariConfig.addDataSourceProperty("useLocalSessionState", "true");
        hikariConfig.addDataSourceProperty("elideSetAutoCommits", "true");
        hikariConfig.addDataSourceProperty("maintainTimeStats", "false");
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        hikariConfig.addDataSourceProperty("useInformationSchema", "true");

        DataSource dataSource = new HikariDataSource(hikariConfig);

        EngineConfig engineConfig = EngineConfig.builder()
                //导出文件地址
                .fileOutputDir("E:\\workspace_idea\\ComponentStudy\\generateDatabaseDoc\\src\\main\\resources")
                //是否打开文件夹
                .openOutputDir(true)
                //文件类型:html、doc、mockdown
                .fileType(EngineFileType.HTML)
                //模板引擎
                .produceType(EngineTemplateType.freemarker).build();

        //设置生成pojo相关配置
        Configuration config = Configuration.builder()
                .version("2022.07.05")
                .description("数据库设计文档")
                .dataSource(dataSource)
                .engineConfig(engineConfig)
                .produceConfig(getProcessConfig()).build();
        new DocumentationExecute(config).execute();
    }

//        ProcessConfig processConfig = ProcessConfig.builder()
//                //指定生成逻辑、当存在指定表、指定表前缀、指定表后缀时，将生成指定表，其余表不生成、并跳过忽略表配置
//                //根据名称指定表生成
//                .designatedTableName(new ArrayList<>())
//                //根据表前缀生成
//                .designatedTablePrefix(new ArrayList<>())
//                //根据表后缀生成
//                .designatedTableSuffix(new ArrayList<>()).build();

    /**
     * 配置想要生成的表+ 配置想要忽略的表
     *
     * @return 生成表配置
     */
    public static ProcessConfig getProcessConfig() {
        // 忽略表名
        List<String> ignoreTableName = Arrays.asList();
        // 忽略表前缀，如忽略a开头的数据库表
        List<String> ignorePrefix = Arrays.asList();
        // 忽略表后缀
        List<String> ignoreSuffix = Arrays.asList();
        return ProcessConfig.builder()
                //根据名称指定表生成
//                .designatedTableName(Arrays.asList("user"))
                //根据表前缀生成("a")
                .designatedTablePrefix(Arrays.asList("gj","ext"))
                //根据表后缀生成("_user")
                .designatedTableSuffix(new ArrayList<>())
                //忽略表名
                .ignoreTableName(ignoreTableName)
                //忽略表前缀
                .ignoreTablePrefix(ignorePrefix)
                //忽略表后缀
                .ignoreTableSuffix(ignoreSuffix).build();
    }
}