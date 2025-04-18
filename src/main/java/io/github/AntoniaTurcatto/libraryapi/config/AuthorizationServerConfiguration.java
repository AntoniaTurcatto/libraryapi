package io.github.AntoniaTurcatto.libraryapi.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import io.github.AntoniaTurcatto.libraryapi.security.CustomAuthentication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.UUID;

@Configuration
@EnableWebSecurity
public class AuthorizationServerConfiguration {

    @Bean
    @Order(1)
    public SecurityFilterChain authServerSecurityFilterChain(HttpSecurity http)throws Exception{
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                OAuth2AuthorizationServerConfigurer.authorizationServer();

        http
                .oauth2ResourceServer(oauth2ResServer -> {
                    oauth2ResServer.jwt(Customizer.withDefaults()); //vou utilizar o token JWT e o resource server vai validar os tokens
                })
                .formLogin(configurer->configurer.loginPage("/login"))

                //by Spring:
                .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
                .with(authorizationServerConfigurer, (authorizationServer) ->
                        authorizationServer
                                .oidc(Customizer.withDefaults())	// Enable OpenID Connect 1.0
                )
                .authorizeHttpRequests((authorize) ->
                        authorize
                                .anyRequest().authenticated()
                )
                // Redirect to the login page when not authenticated from the
                // authorization endpoint
                .exceptionHandling((exceptions) -> exceptions
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint("/login"),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                        )
                );
        return http.build();
    }

    @Bean
    public TokenSettings tokenSettings(){
        return TokenSettings.builder()
                .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                //access token: utilizado nas requisições
                .accessTokenTimeToLive(Duration.ofMinutes(15))
                //refresh token: para renovar o access token
                .refreshTokenTimeToLive(Duration.ofMinutes(90))
                .build();
    }

    @Bean
    public ClientSettings clientSettings(){
        return ClientSettings.builder()
                .requireAuthorizationConsent(false)
                .build();
    }

    //JWKGerar token JWK
    //JWK -> Json Web key
    @Bean
    public JWKSource<SecurityContext> jwkSource() throws Exception{
        RSAKey rsaKey = gerarChaveRSA();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }
    //RSA = chave publica criptografa, chave privada descriptografa
    private RSAKey gerarChaveRSA() throws Exception{
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPublicKey chavePublica = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey chavePrivada = (RSAPrivateKey) keyPair.getPrivate();

        return new RSAKey
                .Builder(chavePublica)
                .privateKey(chavePrivada)
                .keyID(UUID.randomUUID().toString())
                .build();
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource){
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings(){
        return AuthorizationServerSettings.builder()//AQUI ESTÃO AS PADRÕES:
                .tokenEndpoint("/oauth2/token")
                .tokenIntrospectionEndpoint("/oauth2/introspect")// p/ pegar informações (claims) do token
                .tokenRevocationEndpoint("/oauth2/revoke") //revogar token
                .authorizationEndpoint("/oauth2/authorize")
                .oidcUserInfoEndpoint("/oauth2/userinfo") //informações do usuario OPEN ID CONNECT
                //obter chave publica para verificar a assinatura do token
                .jwkSetEndpoint("/oauth2/jwks")
                //logout
                .oidcLogoutEndpoint("/oauth2/logout")
                .build();
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> oAuth2TokenCustomizer(){
        return cotext -> {
            var principal = cotext.getPrincipal();

            if(principal instanceof CustomAuthentication auth){
                OAuth2TokenType tipoToken = cotext.getTokenType();
                if(OAuth2TokenType.ACCESS_TOKEN.equals(tipoToken)){
                    cotext
                            .getClaims()
                            .claim("authorities", auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                            .claim("email", auth.getUsuario().getEmail());

                }
            }
        };
    }

}
