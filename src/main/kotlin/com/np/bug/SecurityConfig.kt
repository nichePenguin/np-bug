package com.np.bug

import com.np.auth.common.NpAuthenticationProvider
import com.np.auth.common.npAuth
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Configuration
class SecurityConfig {
    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(
        http: HttpSecurity,
        authConfig: AuthenticationConfiguration,
        provider: NpAuthenticationProvider
    ): SecurityFilterChain? = http
        .npAuth(authConfig, provider)
        .csrf { it.disable() }
        .authorizeHttpRequests {
            it.requestMatchers("/graphql").permitAll()
                .anyRequest().authenticated()
        }
        .exceptionHandling {
            it.authenticationEntryPoint { req, resp, _ ->
                val redirect = URLEncoder.encode(req.requestURI, StandardCharsets.UTF_8)
                resp.sendRedirect("/login?redirect=$redirect")
            }
        }.build()
}