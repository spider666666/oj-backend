package com.yupi.yuoj;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

/**
 * 主类测试
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@SpringBootTest
class MainApplicationTests {

    @Test
    void contextLoads() {
    }
    @Test
    public void userLogin(){
        String encryptPassword = DigestUtils.md5DigestAsHex("yupi12345678".getBytes());
        System.out.println("encryptPassword = " + encryptPassword);
    }

}
