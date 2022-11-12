package com.itjing.multi;

import com.itjing.multi.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Map;

/**
 * @author lijing
 * @date 2022年06月15日 20:24
 * @description
 */
@SpringBootApplication
public class MultiApplication implements ApplicationRunner {

    @Autowired
    private TestService testService;

    public static void main(String[] args) {
        SpringApplication.run(MultiApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Map<String, Object>> db1Maps = testService.testDB1();
        System.out.println("db1Maps: " + db1Maps);
        List<Map<String, Object>> db2Maps = testService.testDB2();
        System.out.println("db2Maps: " + db2Maps);
    }
}
