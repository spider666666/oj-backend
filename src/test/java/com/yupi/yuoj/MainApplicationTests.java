package com.yupi.yuoj;

import com.yupi.yuoj.job.codesand.CodeSand;
import com.yupi.yuoj.job.codesand.CodeSandProxy;
import com.yupi.yuoj.job.codesand.factory.CodeSandFactory;
import com.yupi.yuoj.job.codesand.imp.ExampleCodeSand;
import com.yupi.yuoj.job.codesand.model.CodeSandRequest;
import com.yupi.yuoj.job.codesand.model.CodeSandResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${codeSand.type: example}")
    private String type;
    @Test
    public void CodeSandTest(){
        //第一次改进：但是在这里呢，为了实现代码的通用性，建议是使用工厂模式，通过字符串 + 配置化的方式实现代码，这样更方便一些
//        ExampleCodeSand CodeSand = new ExampleCodeSand();

        CodeSandFactory codeSandFactory = new CodeSandFactory(type);

        //获取实例
        CodeSand codeSand = codeSandFactory.newInstance();
        //第二次改进，通过静态代理的方式实现对功能方法的增强，实现对日志的添加
        CodeSandProxy codeSandProxy = new CodeSandProxy(codeSand);

        //创建代码请求
        CodeSandRequest request = CodeSandRequest.builder()
                .code("int main(){return 0;}")
                .language("cpp")
                .build();
        CodeSandResponse response = codeSandProxy.executeCode(request);
        System.out.println("response = " + response);
    }

}
