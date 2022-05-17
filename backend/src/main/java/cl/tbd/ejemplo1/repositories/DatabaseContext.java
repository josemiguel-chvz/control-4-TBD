package cl.tbd.ejemplo1.repositories;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.sql2o.Sql2o;

@Configuration
public class DatabaseContext {
    // jdbc:postgresql://<HOST>:<PORT>/<DB_NAME>
    // String user = <User Postgres>;
    // String password = <Contraseña Postgres>;
    @Value("${database.url}")
    private String db;

    @Value("${database.user}")
    private String user;

    @Value("${database.password}")
    private String password;

    @Bean
    public Sql2o sql2o(){
        return new Sql2o(db,user,password);
    }
}