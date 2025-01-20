package net.javadog.chatgpt.common.config;


import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.theokanning.openai.client.AuthenticationInterceptor;
import com.theokanning.openai.client.OpenAiApi;
import com.theokanning.openai.service.OpenAiService;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import retrofit2.Retrofit;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 跨域处理
 * @Author: hdx
 * @Date: 2022/1/13 16:19
 * @Version:-【】 1.0
 */
@Configuration
public class OpenAIConfig {

    @Value("${openai.apikey}")
    private String API_KEY ;
    @Value("${openai.baseUrl}")
    private String BASE_URL ;
    @Value("${openai.timeout}")
    private int TIMEOUT ;
    @Bean
    public OpenAiService openAiService() {
        OkHttpClient client = new OkHttpClient.Builder()
                //连接池
                .connectionPool(new ConnectionPool(Runtime.getRuntime().availableProcessors() * 10, 30, TimeUnit.SECONDS))
                //自定义的拦截器,如重试拦截器,日志拦截器,负载均衡拦截器等
                // .addInterceptor(new RetryInterceptor())
                // .addInterceptor(new LogInterceptor())
                // .addInterceptor(new LoadBalanceInterceptor())
                //添加代理
                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("192.168.1.1", 7890)))
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT + 30, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT + 30, TimeUnit.SECONDS)
                .protocols(Arrays.asList(Protocol.HTTP_2, Protocol.HTTP_1_1))
                .addInterceptor(new AuthenticationInterceptor(API_KEY))
                .build();
        //4.2 自定义Retorfit配置
        Retrofit retrofit = OpenAiService.defaultRetrofit(client, OpenAiService.defaultObjectMapper(),BASE_URL);
        OpenAiApi openAiApi = retrofit.create(OpenAiApi.class);
        return new OpenAiService(openAiApi);
    }
}
