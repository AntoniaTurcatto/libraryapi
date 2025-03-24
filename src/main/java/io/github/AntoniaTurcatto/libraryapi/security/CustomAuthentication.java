package io.github.AntoniaTurcatto.libraryapi.security;

import io.github.AntoniaTurcatto.libraryapi.config.model.Usuario;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class CustomAuthentication implements Authentication {

    private final Usuario usuario;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //return this.usuario.getRoles().stream().map(role -> new SimpleGrantedAuthority("ROLE_"+role)).toList();//uma forma de fazer é colocar o prefixo ROLE_ nas roles, que é a forma que o spring identifica as roles
        //outra forma é mudando a forma de identificar, eliminando o prefixo ROLE_ da identificação
        return this.usuario.getRoles().stream().map(role -> new SimpleGrantedAuthority(role)).toList();
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return usuario;
    }

    @Override
    public Object getPrincipal() {
        return usuario;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return usuario.getLogin();
    }
}
