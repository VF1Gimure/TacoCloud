package tacos.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  
  
  @Autowired
  DataSource dataSource;
  
  @Autowired
  private UserDetailsService userDetailsService;
  
  
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
        .antMatchers("/design", "/orders")
        .hasRole("ROLE_USER")
        .antMatchers("/", "/**").access("permitAll")
        .and()
        .formLogin()
        .loginPage("/login")
        .and()
        .logout()
        .logoutSuccessUrl("/");
  }
  
  
  /*@Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
        .antMatchers("/design", "/orders")
        .access("hasRole('ROLE_USER') && " +
            "T(java.util.Calendar).getInstance().get(" +
            "T(java.util.Calendar).DAY_OF_WEEK) == " +
            "T(java.util.Calendar).TUESDAY")
        .antMatchers(“ /”,"/**").access("permitAll")
    ;
  }
  * With SpEL-based security constraints, the possibilities are virtually endless.
  * I’ll bet that you’re already dreaming up interesting
  * security constraints based on SpEL.
  */
  
  
  @Override
  protected void configure(AuthenticationManagerBuilder auth)
      throws Exception {
    
    auth
        .userDetailsService(userDetailsService)
        .passwordEncoder(encoder());
    
    /*auth
        .jdbcAuthentication()
        .dataSource(dataSource)
        .usersByUsernameQuery(secConfQueries().usersQuery)
        .authoritiesByUsernameQuery(secConfQueries().authoritiesQuery)
        .passwordEncoder(passwordEncoder()); //StandardPasswordEncoder is deprecated*/
    
  }
  
  
  //To be honest, I just want to clean the configure function... no static sounds just a clean read.
  @Bean
  public PasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }
  
  @Bean
  public SecurityConfigQueries secConfQueries() {
    return new SecurityConfigQueries();
  }
  
  private static class SecurityConfigQueries {
    public String usersQuery = "select username, password, enabled from Users " +
        "where username=?";
    
    public String authoritiesQuery = "select username, authority from UserAuthorities " +
        "where username=?";
  }
  
}
