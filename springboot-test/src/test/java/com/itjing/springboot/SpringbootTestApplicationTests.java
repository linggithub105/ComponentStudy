package com.itjing.springboot;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringbootTestApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void testDebug() {
        for (int i = 0; i < 1000; i++) {
            System.out.print(i + " ");
        }
    }

}
