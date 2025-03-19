package io.github.AntoniaTurcatto.libraryapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity //NECESSÁRIA PARA CONFIGURACOES DE SEGURANÇA
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(AbstractHttpConfigurer::disable)//config para aplicação web; para que a aplicação consiga fazer as requisições de forma autenticada, ela envia um token CSRF para o backend, garantindo que a página que enviou a requisição é a da aplicação
                //.formLogin(configurer -> configurer.loginPage("/login.html").successForwardUrl("/home.html"))
                //.formLogin(Customizer.withDefaults())//formulário padrão de login
                .formLogin(configurer -> {
                    configurer.loginPage("/login").permitAll();//deixando acessar sem autenticação a pagina para autenticação
                })
                .httpBasic(Customizer.withDefaults())//definindo Http Basic
                .authorizeHttpRequests(authorize -> {
                    authorize.anyRequest().authenticated(); //qualquer requisição feita para a API tem que estar autenticada
                })
                .build();
    }

}
