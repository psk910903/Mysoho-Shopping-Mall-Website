package com.study.springboot.config;

import com.study.springboot.service.CustomOAuth2UserService;
import com.study.springboot.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity //웹보안 활성화를위한 annotation
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    final private SecurityService securityService;
    final private CustomOAuth2UserService customOAuth2UserService;
    @Override
    public void configure(WebSecurity web) {
        web
                .ignoring()
                .antMatchers("/find/**");
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests() // 요청에 대한 보안설정을 시작
                .antMatchers("/","/order/**","/plan/**","/product/**","/qna/**","/notice/**","/inquiry/**","/myorder","/search","/terms/**","/enlarge/**","/css/**", "/js/**", "/img/**","/captchaImg.do").permitAll()
                .antMatchers("/user/join").permitAll()
                .antMatchers("/user/joinAction").permitAll()
                .antMatchers("/myorder/**").hasAnyRole("USER","ADMIN")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
       .and()
                .formLogin() //로그인 인증에 대한 설정을 시작
                .loginPage("/user/login") //
                .loginProcessingUrl("/user/loginAction") //로그인 액션 URI를 지정한다.
                .successHandler( (request,response,authentication) -> {
                    request.getSession().setAttribute("username", request.getParameter("name"));
                    response.sendRedirect("/");
                })
                .failureUrl("/user/login?error")
                .permitAll()
       .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/user/logoutAction"))
                .deleteCookies("JSESSIONID","XSRF-TOKEN")
                .invalidateHttpSession(true)
                .logoutSuccessUrl("/")
       .and()
                .oauth2Login()
                .successHandler(successHandler())
                .failureHandler(failureHandler())
                .userInfoEndpoint().userService(customOAuth2UserService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(securityService).passwordEncoder(passwordEncoder());
    }
    @Bean
    SimpleUrlAuthenticationSuccessHandler successHandler() {//로그인 성공시 부가 작업
        return new SimpleUrlAuthenticationSuccessHandler("/snsLoginSuccess");
    }
    @Bean
    SimpleUrlAuthenticationFailureHandler failureHandler() {
        return new SimpleUrlAuthenticationFailureHandler("/snsLoginFailure");
    }
}
