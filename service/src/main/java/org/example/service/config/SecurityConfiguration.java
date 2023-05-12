package org.example.service.config;

import lombok.RequiredArgsConstructor;
import org.example.service.service.SecurityUserDetails;
import org.example.service.service.SecurityUserDetailsImpl;
import org.example.service.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashSet;
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
                        .antMatchers(HttpMethod.POST, "/books/**", "/users", "/users/{\\d+}/delete").hasAuthority(ADMIN.getAuthority())
                        .antMatchers(HttpMethod.POST, "/orders", "/orders/{\\d+}/update").hasAuthority(USER.getAuthority())
                        .antMatchers(HttpMethod.POST, "/users/{\\d+}/update").authenticated()
                        .antMatchers("/orders", "/books", "/users/{\\d+}/update", "/orders/{\\d+}/delete", "/v3/api-docs/**", "/swagger-ui/**").authenticated()
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
                                .oidcUserService(oidcUserService(userService))));

    }

    private OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService(UserService userService) {
        return userRequest -> {
            String email = userRequest.getIdToken().getClaim("email");

            var securityUserDetails = userService.loadUserByUsername(email);

            var oidcUser = new DefaultOidcUser(securityUserDetails.getAuthorities(), userRequest.getIdToken());

            Set<Method> userDetailsMethods = new HashSet<>(Set.of(SecurityUserDetailsImpl.class.getMethods()));
            userDetailsMethods.addAll(Set.of(SecurityUserDetails.class.getMethods()));

            return (OidcUser) Proxy.newProxyInstance(SecurityConfiguration.class.getClassLoader(),
                    new Class[]{SecurityUserDetails.class, OidcUser.class},
                    (proxy, method, args) -> userDetailsMethods.contains(method)
                            ? method.invoke(securityUserDetails, args)
                            : method.invoke(oidcUser, args));
        };
    }
}
