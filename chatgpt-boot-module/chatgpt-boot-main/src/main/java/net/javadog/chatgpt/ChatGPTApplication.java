package net.javadog.chatgpt;


import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;


import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * SpringBoot方式启动类
 *
 * @author hdx
 * @date 2022年11月21日 15:26:29
 */
@SpringBootApplication
@ServletComponentScan
@Slf4j
@MapperScan(basePackages = {"net.javadog.chatgpt.mapper"})
public class ChatGPTApplication {

    public static void main(String[] args) throws UnknownHostException {
        // 启动类
        ConfigurableApplicationContext application = SpringApplication.run(ChatGPTApplication.class, args);
        // 打印基础信息
        info(application);
    }

    static void info(ConfigurableApplicationContext application) throws UnknownHostException {
        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String active = env.getProperty("spring.profiles.active");
        String contextPath = env.getProperty("server.servlet.context-path");
        if (contextPath == null) {
            contextPath = "";
        }
        log.info("\n----------------------------------------------------------\n\t" +
                "ChatGPT程序【" + active + "】环境已启动! 地址如下:\n\t" +
                "Local: \t\thttp://localhost:" + port + contextPath + "\n\t" +
                "----------------------------------------------------------");
    }

}
