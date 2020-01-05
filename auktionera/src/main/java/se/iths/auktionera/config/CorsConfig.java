package se.iths.auktionera.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    //IMPORTANT: it has to be a normal configuration class,
//not extending WebMvcConfigurerAdapter or other Spring Security class
    @Bean
    public FilterRegistrationBean customCorsFilter() {
        System.out.println("hit hit hit hit");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("https://accounts.google.com/o/oauth2/v2/auth");
        config.addAllowedOrigin("http://localhost:8081");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));

        //IMPORTANT #2: I didn't stress enough the importance of this line in my original answer,
        //but it's here where we tell Spring to load this filter at the right point in the chain
        //(with an order of precedence higher than oauth2's filters)
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }
}

//@Configuration
//@EnableWebMvc
//public class CorsConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        System.out.println("hit hit");
//        registry.addMapping("/**")
//                .allowedMethods("GET", "POST", "PUT", "DELETE")
//                .allowCredentials(true)
//                .allowedHeaders("*")
//                .allowedOrigins("http://localhost:8081");
//    }
//
////    @Bean
////    public WebMvcConfigurer corsConfigurer() {
////        return new WebMvcConfigurerAdapter() {
////            @Override
////            public void addCorsMappings(CorsRegistry registry) {
////                registry.addMapping("/**").allowedMethods("GET", "POST", "PUT", "DELETE").allowedOrigins("*")
////                        .allowedHeaders("*");
////            }
////        };
////    }
//}
