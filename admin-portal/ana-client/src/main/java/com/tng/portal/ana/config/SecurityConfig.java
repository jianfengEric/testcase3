package com.tng.portal.ana.config;

import com.tng.portal.ana.authentication.AnaPrincipalAuthenticationProvider;
import com.tng.portal.ana.authentication.RestAccessDeniedHandler;
import com.tng.portal.ana.authentication.RestAuthenticationEntryPoint;
import com.tng.portal.ana.authentication.TokenAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.Filter;

/**
 * Created by Dell on 2018/8/6.
 */
@Configuration
@EnableWebSecurity
//@EnableAutoConfiguration(exclude = {AnaPrincipalAuthenticationProvider.class})
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public AnaPrincipalAuthenticationProvider authenticationProvider(){
        return new AnaPrincipalAuthenticationProvider();
    }

    @Bean
    protected Filter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("HEAD");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");
        config.addExposedHeader("Location");
        source.registerCorsConfiguration("/**", config);
        return new org.springframework.web.filter.CorsFilter(source);
    }

    @Bean
    public TokenAuthenticationFilter customFilter() {
        return new TokenAuthenticationFilter();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/*.css", "/*.js", "/*.html", "/webjars/**", "/swagger-resources/**", "/v2/api-docs/**", "/")
        .antMatchers("/login", "/login/loginCheck", "/login/syncToken", "/oauth/**", "/validCode/**", "/public/**",
                "/account/hasPermession", "/account/createdByClient", "/account/registeredByClient", "/account/checkActivation","/account/checkFirstNameAndLastName",
                "/account/checkEmailDate","/account/checkResetPwd","/account/resetPassword", "/account/activationAccount",
                "/remote/**","/mamAccount/**","/participant/v1/download-material","/approval/v1/deploy","/base_service/v1/**");
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .anyRequest().authenticated()
                .and()
                .anonymous().disable()
                .csrf().disable()
                .sessionManagement()                   // Customize our own  session  strategy 
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // Adjust to let  Spring Security  No creation and use  session


        http.exceptionHandling().authenticationEntryPoint(new RestAuthenticationEntryPoint());
        http.exceptionHandling().accessDeniedHandler(new RestAccessDeniedHandler());

        http.addFilterAt(customFilter(), BasicAuthenticationFilter.class);
        http.addFilterAt(corsFilter(), ChannelProcessingFilter.class);
    }

}
