package com.springboot.demo.mvcsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class DemoSecurityConfig {
    // JDBC Configuration
    @Bean
    public UserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {
        // the data source is automatically configured by spring boot
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        // custom tables and queries provided to spring security
        jdbcUserDetailsManager.setUsersByUsernameQuery("SELECT user_id, pw, active FROM members WHERE user_id = ?");
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("SELECT user_id, role FROM roles WHERE user_id = ?");
        return jdbcUserDetailsManager;
    }

//    @Bean
//    public InMemoryUserDetailsManager userDetailsManager() {
//        UserDetails john = User.builder().username("john").password("{noop}test123").roles("EMPLOYEE").build();
//        UserDetails mary = User.builder().username("mary").password("{noop}test123").roles("EMPLOYEE", "MANAGER").build();
//        UserDetails susan = User.builder().username("susan").password("{noop}test123").roles("EMPLOYEE", "MANAGER", "ADMIN").build();
//        return new InMemoryUserDetailsManager(john, mary, susan);
//    }

    // configure security of web paths
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(configurer -> configurer
                        .requestMatchers("/css/**").permitAll()
                        .requestMatchers("/leaders/**").hasRole("MANAGER")
                        .requestMatchers("/systems/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
            .formLogin(form -> form.loginPage("/loginPage")
                .loginProcessingUrl("/authenticate").defaultSuccessUrl("/", true)
                .failureUrl("/loginPage?error=true")
                .permitAll())
            .logout(logout -> logout.logoutUrl("/logout")
                .logoutSuccessUrl("/loginPage?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .exceptionHandling(handler -> handler.accessDeniedPage("/access-denied"));

        return httpSecurity.build();
    }
}
