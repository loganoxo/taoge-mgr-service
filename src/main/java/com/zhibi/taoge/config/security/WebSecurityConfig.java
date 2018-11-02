package com.zhibi.taoge.config.security;

import com.zhibi.taoge.config.security.jwt.AuthenticationFailHandler;
import com.zhibi.taoge.config.security.jwt.AuthenticationSuccessHandler;
import com.zhibi.taoge.config.security.jwt.JWTAuthenticationFilter;
import com.zhibi.taoge.config.security.jwt.RestAccessDeniedHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Created by QinHe on 2018-10-21.
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsService;
    private final IgnoredUrlsProperties ignoredUrlsProperties;
    private final AuthenticationFailHandler authenticationFailHandler;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final RestAccessDeniedHandler restAccessDeniedHandler;
//    private final MyFilterSecurityInterceptor myFilterSecurityInterceptor;

    public WebSecurityConfig(UserDetailsServiceImpl userDetailsService,
                             IgnoredUrlsProperties ignoredUrlsProperties,
                             AuthenticationFailHandler authenticationFailHandler,
                             AuthenticationSuccessHandler authenticationSuccessHandler,
//                             MyFilterSecurityInterceptor myFilterSecurityInterceptor,
                             RestAccessDeniedHandler restAccessDeniedHandler) {
        this.userDetailsService = userDetailsService;
        this.ignoredUrlsProperties = ignoredUrlsProperties;
        this.authenticationFailHandler = authenticationFailHandler;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.restAccessDeniedHandler = restAccessDeniedHandler;
//        this.myFilterSecurityInterceptor = myFilterSecurityInterceptor;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http
                .authorizeRequests();
        //除配置文件忽略路径其它所有请求都需经过认证和授权
        for (String url : ignoredUrlsProperties.getUrls()) {
            registry.antMatchers(url).permitAll();
        }

        registry.and()
                //表单登录方式
                .formLogin()
                .loginPage("/console/security/needLogin")
                //登录需要经过的url请求
                .loginProcessingUrl("/console/admin/user/login")
                .permitAll()
                //成功处理类
                .successHandler(authenticationSuccessHandler)
                //失败
                .failureHandler(authenticationFailHandler)
                .and()
                //允许网页iframe
                .headers().frameOptions().disable()
                .and()
                .logout()
                .permitAll()
                .and()
                .authorizeRequests()
                //任何请求
                //有注解的方法才拦截，默认不拦截
//                .anyRequest()
//                //需要身份认证
//                .authenticated()
                .and()
                //关闭跨站请求防护
                .csrf().disable()
                //前后端分离采用JWT 不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //自定义权限拒绝处理类
                .exceptionHandling().accessDeniedHandler(restAccessDeniedHandler)
                .and()
                //添加自定义权限过滤器
//                .addFilterBefore(myFilterSecurityInterceptor, FilterSecurityInterceptor.class)
                //添加JWT过滤器 除/console/user/login其它请求都需经过此过滤器
                .addFilter(new JWTAuthenticationFilter(authenticationManager()));
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //解决静态资源被拦截的问题
        web.ignoring().antMatchers("/css/**");
    }

}
