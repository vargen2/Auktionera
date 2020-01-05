//package se.iths.auktionera.config;
//
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//
//@Configuration
//@Profile("!noSecurity")
//public class SecConfig extends WebSecurityConfigurerAdapter {
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        System.out.println("hit hit hit");
//
//        http.authorizeRequests()
//                // permit all access to these endpoints.
//                .antMatchers(HttpMethod.GET, "/api/auctions").permitAll()
//                // any other request needs to be authenticated
//                .anyRequest().authenticated();
//
//        //http.oauth2Login().and().cors().and().csrf().disable();
//
////        http.authorizeRequests(authorizeRequests ->
////                authorizeRequests
////                        .anyRequest().authenticated()
////        )
////                .oauth2Client(withDefaults()).cors()
////                .and().csrf().disable();
//    }
//
//}
