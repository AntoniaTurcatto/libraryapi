package io.github.AntoniaTurcatto.libraryapi.config;

import io.github.AntoniaTurcatto.libraryapi.security.CustomUserDetailsService;
import io.github.AntoniaTurcatto.libraryapi.service.UsuarioService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity //NECESSÁRIA PARA CONFIGURACOES DE SEGURANÇA
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true) //permitir as autorizações serem configuradas nos endpoints
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(AbstractHttpConfigurer::disable)//config para aplicação web; para que a aplicação consiga fazer as requisições de forma autenticada, ela envia um token CSRF para o backend, garantindo que a página que enviou a requisição é a da aplicação
                //.formLogin(configurer -> configurer.loginPage("/login.html").successForwardUrl("/home.html"))
                .formLogin(Customizer.withDefaults())
//                .formLogin(configurer -> {
//                    configurer.loginPage("/login");
//                })
                .httpBasic(Customizer.withDefaults())//definindo Http Basic
                .authorizeHttpRequests(authorize -> {
                    //controle de requisições por ROLE e Authorities
                    authorize.requestMatchers("/login").permitAll();//deixando acessar sem autenticação a pagina para autenticação
                    authorize.requestMatchers(HttpMethod.POST, "/usuarios").permitAll();
//
//                    authorize.requestMatchers(HttpMethod.POST, "/autores/**").hasAuthority("CADASTRAR_AUTOR");//somente admins podem salvar
//                    authorize.requestMatchers(HttpMethod.DELETE, "/autores/**").hasAuthority("EXCLUIR_AUTOR");
//                    authorize.requestMatchers(HttpMethod.PUT, "/autores/**").hasAuthority("ATUALIZAR_AUTOR");
//                    authorize.requestMatchers(HttpMethod.GET, "/autores/**").hasAnyRole("ADMIN", "USER");
//                    authorize.requestMatchers("/livros/**").hasAnyRole("USER", "ADMIN");



                    authorize.anyRequest().authenticated(); //qualquer requisição feita para a API tem que estar autenticada
                    //se eu colocar qualquer regra após o anyRequest() ele não vai atender
                })
                .oauth2Login(Customizer.withDefaults())
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public UserDetailsService userDetailsService(UsuarioService usuarioService){
//        UserDetails user1 = User.builder()
//                .username("usuario")
//                .password(encoder.encode("123"))
//                .roles("USER")
//                .build();
//
//        UserDetails user2 = User.builder()
//                .username("admin")
//                .password(encoder.encode("321"))
//                .roles("ADMIN")
//                .authorities("ROLE_ADMIN", "CADASTRAR_AUTOR", "EXCLUIR_AUTOR", "ATUALIZAR_AUTOR")//outro méthodo de fazer
//                .build();
//      return new InMemoryUserDetailsManager(user1, user2);
        return new CustomUserDetailsService(usuarioService);
    }

}
