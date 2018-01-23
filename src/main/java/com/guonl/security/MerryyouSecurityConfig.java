package com.guonl.security;

import com.guonl.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.guonl.authorize.AuthorizeConfigProvider;
import com.guonl.properties.SecurityConstants;
import com.guonl.properties.SecurityProperties;
import com.guonl.session.MerryyounExpiredSessionStrategy;
import com.guonl.validate.code.ValidateCodeSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.social.security.SpringSocialConfigurer;

import javax.sql.DataSource;

/**
 * Created on 2018/1/4.
 *
 * @author zlf
 * @since 1.0
 */
@Configuration
public class MerryyouSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AuthorizeConfigProvider authorizeConfigProvider;

    @Autowired
    private SpringSocialConfigurer merryyouSpringSocialConfigurer;

    @Autowired
    private ValidateCodeSecurityConfig validateCodeSecurityConfig;

    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
        http
                .formLogin()//使用表单登录，不再使用默认httpBasic方式
                .loginPage(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL)//如果请求的URL需要认证则跳转的URL
                .loginProcessingUrl(SecurityConstants.DEFAULT_SIGN_IN_PROCESSING_URL_FORM)//处理表单中自定义的登录URL
                .and()
                .apply(validateCodeSecurityConfig)//验证码拦截
                .and()
                .apply(smsCodeAuthenticationSecurityConfig)
                .and()
                .apply(merryyouSpringSocialConfigurer)//社交登录
                .and()
                .rememberMe()
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(securityProperties.getRememberMeSeconds())
                .userDetailsService(userDetailsService)
                .and()
                .sessionManagement()
                .invalidSessionUrl("/session/invalid")
                .maximumSessions(1)//最大session并发数量1
                .maxSessionsPreventsLogin(false)//之后的登录踢掉之前的登录
                .expiredSessionStrategy(new MerryyounExpiredSessionStrategy())
                .and()
                .and()
                .logout()
                .logoutUrl("/signOut")//默认退出地址/logout
                .logoutSuccessUrl("/register")//退出之后跳转到注册页面
                .deleteCookies("JSESSIONID")
                .and()
                .authorizeRequests().antMatchers(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL,
                SecurityConstants.DEFAULT_SIGN_IN_PROCESSING_URL_FORM,
                SecurityConstants.DEFAULT_REGISTER_URL,
                SecurityConstants.DEFAULT_SIGN_IN_PROCESSING_URL_MOBILE,
                SecurityConstants.DEFAULT_SIGN_IN_URL_MOBILE_PAGE,
                "/register",
                "/social/info",
                "/session/invalid",
                "/**/*.js",
                "/**/*.css",
                "/**/*.jpg",
                "/**/*.png",
                "/**/*.woff2",
                "/code/*")
                .permitAll()//以上的请求都不需要认证
                //.antMatchers("/").access("hasRole('USER')")
                .and()
                .csrf().disable()//关闭csrd拦截
        ;
        //安全模块单独配置
        authorizeConfigProvider.config(http.authorizeRequests());
    }
}
