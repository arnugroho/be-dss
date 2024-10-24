package com.arnugroho.be_dss.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@PropertySource("classpath:clients.properties")
@EnableAsync
public class WebConfig implements WebMvcConfigurer {
    @Value("${allowedHosts}")
    private String allowedHosts;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins(allowedHosts.split(",")).allowedMethods("*");

    }

    // @Override
    // public void addInterceptors(InterceptorRegistry registry) {
    //     registry.addInterceptor(new Interceptor(utilService)).addPathPatterns("/projectOwner/**");
    //     registry.addInterceptor(new Interceptor(utilService)).addPathPatterns("/masterSurvey/**");
    //     registry.addInterceptor(new Interceptor(utilService)).addPathPatterns("/question/**");
    // }

//    @Bean
//    public ObjectMapper objectMapper() {
//        return new ObjectMapper()
//                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
//                .registerModule(new JavaTimeModule());
//    }

}
