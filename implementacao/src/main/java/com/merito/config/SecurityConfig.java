package com.merito.config;

import com.merito.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(false);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authz -> authz
                // Endpoints públicos
                .requestMatchers(new AntPathRequestMatcher("/api/auth/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/api/health")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/api/test")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/api/cors-test/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
                
                // Endpoints protegidos
                .requestMatchers(new AntPathRequestMatcher("/api/alunos/me")).authenticated() // Aluno pode buscar seus próprios dados
                .requestMatchers(new AntPathRequestMatcher("/api/alunos", "GET")).hasAnyAuthority("ROLE_ADMIN", "ROLE_PROFESSOR") // Professores podem listar alunos
                .requestMatchers(new AntPathRequestMatcher("/api/alunos/instituicao/**", "GET")).hasAnyAuthority("ROLE_ADMIN", "ROLE_PROFESSOR") // Professores podem buscar por instituição
                .requestMatchers(new AntPathRequestMatcher("/api/alunos/**")).hasAuthority("ROLE_ADMIN") // Outras operações apenas ADMIN
                .requestMatchers(new AntPathRequestMatcher("/api/transacoes/professor", "GET")).hasAuthority("ROLE_PROFESSOR") // Professor pode ver seu histórico
                .requestMatchers(new AntPathRequestMatcher("/api/transacoes/aluno", "GET")).hasAuthority("ROLE_ALUNO") // Aluno pode ver seu histórico
                .requestMatchers(new AntPathRequestMatcher("/api/empresas/**")).hasAuthority("ROLE_ADMIN")
                .requestMatchers(new AntPathRequestMatcher("/api/instituicoes/**")).hasAuthority("ROLE_ADMIN")
                
                // Todos os outros endpoints requerem autenticação
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // Para H2 Console
        http.headers(headers -> headers.frameOptions().disable());

        return http.build();
    }
}
