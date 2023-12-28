package tomato.classifier.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public CustomAuthenticationEntryPoint getCustomAuthenticationEntryPoint(){
        return new CustomAuthenticationEntryPoint();
    }

    //passwordEncoder 빈 등록을 직접 해줘야 함
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http.csrf().disable();

        http.formLogin()
                .loginPage("/auth/login")    //로그인 page url
                .defaultSuccessUrl("/main")         //로그인 성공 시 이동할 url
                .usernameParameter("Id")     //로그인 시 사용할 파라미터 이름 설정
                .passwordParameter("password")
                .failureUrl("/auth/login/error"); //실패시 이동할 url

        http.logout()
                .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/main");

        http.authorizeRequests()    //요청 URL 권한 설정 - whitelist 방식으로 허용할 거 빼고 다 인증 필요하게끔
                .mvcMatchers("/images/**","/result/**").permitAll()
                .mvcMatchers("/main/**").permitAll()
                .antMatchers("/auth/login", "/auth/login/error", "/auth/logout","/auth/register").permitAll()
                .antMatchers(HttpMethod.GET, "/article").permitAll()    //path var 어캐 넣음
                .mvcMatchers("/article/detail/**").permitAll()
                .mvcMatchers("/admin/**").hasRole("ADMIN")
                .mvcMatchers( "/comment/**","auth/edit/**").hasRole("MEMBER")
                .anyRequest().authenticated();

        http.exceptionHandling()
                //.authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .authenticationEntryPoint(getCustomAuthenticationEntryPoint());



        return http.build();
    }
}
