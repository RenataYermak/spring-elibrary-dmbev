package org.example.service.config;

import lombok.RequiredArgsConstructor;
import org.example.service.service.CustomUserDetails;
import org.example.service.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Set;

import static org.example.service.database.entity.Role.ADMIN;
import static org.example.service.database.entity.Role.USER;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(urlConfig -> urlConfig
                        .antMatchers("/users/{\\d+}/delete", "/books/add", "/books/{\\d+}/update", "/books/{\\d+}/delete", "/users/registration").hasAuthority(ADMIN.getAuthority())
                        .antMatchers(HttpMethod.POST, "/books/**", "/users").hasAuthority(ADMIN.getAuthority())
                        .antMatchers(HttpMethod.POST, "/orders", "/orders/{id}/update").hasAuthority(USER.getAuthority())
                        .antMatchers("/orders", "/books", "/users/{\\d+}/update", "/orders/{id}/delete", "/v3/api-docs/**", "/swagger-ui/**").authenticated()
                        .antMatchers("/login").permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .deleteCookies("JSESSIONID"))
                .formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/books"))
                .oauth2Login(config -> config
                        .loginPage("/login")
                        .defaultSuccessUrl("/books")
                        .userInfoEndpoint(userInfo -> userInfo
                                .oidcUserService(oidcUserService())));

    }

    private OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        return userRequest -> {
            String email = userRequest.getIdToken().getClaim("email");

            UserDetails userDetails = userService.loadUserByUsername(email);

            DefaultOidcUser oidcUser = new DefaultOidcUser(userDetails.getAuthorities(), userRequest.getIdToken());

            Set<Method> userDetailsMethods = new java.util.HashSet<>(Set.of(CustomUserDetails.class.getMethods()));
            userDetailsMethods.addAll(Set.of(UserDetails.class.getMethods()));
            return (OidcUser) Proxy.newProxyInstance(SecurityConfiguration.class.getClassLoader(),
                    new Class[]{UserDetails.class, OidcUser.class},
                    (proxy, method, args) -> userDetailsMethods.contains(method)
                            ? method.invoke(userDetails, args)
                            : method.invoke(oidcUser, args));
        };
    }
}
