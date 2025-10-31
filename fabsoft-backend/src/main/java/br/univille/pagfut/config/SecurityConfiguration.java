package br.univille.pagfut.config;

import br.univille.pagfut.domain.auth.SecurityFilter;
import br.univille.pagfut.domain.user.UserService;
import br.univille.pagfut.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, SecurityFilter securityFilter) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> {
					authorize.requestMatchers("/auth/**").permitAll();
					authorize.requestMatchers("/h2-console").permitAll();

					// 2. Permite caminhos de registro específicos
					authorize.requestMatchers("/users/register").permitAll();
					authorize.requestMatchers("/users/confirm").permitAll();

					// 3. Permite OPTIONS (CORS pre-flight) em todos os lugares
					authorize.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();

                    authorize.anyRequest().authenticated();
                })

                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public UserDetailsService userDetailsService(UserService userService) {
        return new CustomUserDetailsService(userService);
    }


    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 1. Defina a(s) origem(ns) permitida(s). Use a URL exata do seu Angular.
        // **IMPORTANTE:** Não use "*" se estiver usando Basic Auth (credenciais).
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));

        // 2. Permite os métodos HTTP que você usará. OPTIONS é essencial para o CORS.
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // 3. **CRUCIAL para HTTP Basic/Credenciais:** Permite que o cabeçalho Authorization seja enviado.
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

        // 4. **CRUCIAL para HTTP Basic:** Indica ao navegador que as credenciais podem ser incluídas.
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Aplica a configuração a todos os paths

        return source;
    }
}
