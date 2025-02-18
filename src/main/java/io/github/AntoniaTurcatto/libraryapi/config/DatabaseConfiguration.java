package io.github.AntoniaTurcatto.libraryapi.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfiguration {

    @Value("${spring.datasource.url}")
    String url;

    @Value("${spring.datasource.username}")
    String username;

    @Value("${spring.datasource.password}")
    String password;

    @Value("${spring.datasource.driver-class-name}")
    String driver;

    //@Bean
    public DataSource dataSource(){//data source nativo, não recomendado
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setDriverClassName(driver);
        return ds;
    }

    @Bean
    public DataSource hikariDataSource(){//Data source recomendado, padrão do Spring
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName(driver);

        //pool de conexoes
        config.setMaximumPoolSize(10);//maximo de conexoes liberadas
        config.setMinimumIdle(1);//minimo que será liberado de início
        config.setPoolName("libraryDbPool");
        config.setMaxLifetime(600000);//tempo máximo de uma conexão no pool em ms (padrão 30min). foi colocado 10min
        config.setConnectionTimeout(100000);//tempo máximo de busca por nova conexao
        config.setConnectionTestQuery("select 1");//teste para checar se o banco está conectado

        return new HikariDataSource(config);
    }

}
