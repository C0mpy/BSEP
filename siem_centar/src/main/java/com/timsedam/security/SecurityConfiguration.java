package com.timsedam.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.session.SessionManagementFilter;

import com.timsedam.services.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    // definisemo konfiguraciju za autentikaciju, koristi se UserDetailsImpl za pronalazenje korisnika
    // i BCrypt za sifrovanje u bazi
    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        try {
            auth.userDetailsService(userDetailsService)
                    .passwordEncoder(passwordEncoder());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
        AuthenticationTokenFilter authenticationTokenFilter = new AuthenticationTokenFilter();
        authenticationTokenFilter.setAuthenticationManager(authenticationManagerBean());
        return authenticationTokenFilter;
    }


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
        		
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()

                .antMatchers( "/api/operator/login", "/api/admin/login", "/api/agent/login")
                .permitAll()
                .and()
                .authorizeRequests()

                .antMatchers( "/api/agent/savelog")
                .hasAuthority("SEND_LOG")
                .and()
                .authorizeRequests()

                .antMatchers( "/api/operator/getAlarmNumber","/api/operator/getLogNumber")
                .hasAuthority("GET_METRICS")
                .and()
                .authorizeRequests()

                .antMatchers(
                        "/api/operator/getLogsByMonitor/*")
                .hasAuthority("GET_LOG")
                .and()
                .authorizeRequests()

                .antMatchers("/api/operator/getAlarm")
                .hasAuthority("GET_ALARM")
                .and()
                .authorizeRequests()

                .antMatchers( "/api/operator/getAllAgents")
                .hasAuthority("GET_AGENTS")
                .and()
                .authorizeRequests()

                .antMatchers("/api/operator/getMonitors/*")
                .hasAuthority("GET_MONITORS")
                .and()
                .authorizeRequests()
                
                .antMatchers("/api/admin/addAlarm")
                .hasAuthority("ADD_ALARM")
                .and()
                .authorizeRequests()
                
                .and()
        		.csrf()
        		.csrfTokenRepository(csrfTokenRepository())
                .ignoringAntMatchers("/api/agent/**");

        // dodajemo nas authenticationTokenFilterBean da se izvrsava pre UsernamePasswordAuthenticationFilter
        httpSecurity.addFilterBefore(authenticationTokenFilterBean(),
                UsernamePasswordAuthenticationFilter.class);
        httpSecurity.addFilterAfter(new CsrfHeaderFilter(), SessionManagementFilter.class);
    }
    
	private CsrfTokenRepository csrfTokenRepository() {
		HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		repository.setHeaderName("X-XSRF-TOKEN");
		return repository;
	}
}
