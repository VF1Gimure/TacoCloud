package tacos.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.sql.DataSource;


@Configuration
@EnableWebSecurity
public class SecurityConfig  extends WebSecurityConfigurerAdapter {


    @Autowired
    DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
    throws Exception {

        //inner private class to increase readability.
        //SecurityConfigQueries are just queries for *THIS* file
        SecurityConfigQueries scq = new SecurityConfigQueries();

        auth
            .jdbcAuthentication()
            .dataSource(dataSource)
            .usersByUsernameQuery(scq.usersQuery)
            .authoritiesByUsernameQuery(scq.authoritiesQuery);










    }


    private static class SecurityConfigQueries {
        public String usersQuery = "select username, password, enabled from Users " +
                "where username=?";

        public String authoritiesQuery = "select username, authority from UserAuthorities " +
                "where username=?";
    }

}
