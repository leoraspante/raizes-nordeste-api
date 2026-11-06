package br.com.raizesnordeste.api.infra.security;

/**
 * Serviço de integração com o motor do Spring Security.
 * Fornece os dados do usuário (identidade e privilégios/Roles) extraídos da base de dados,
 * utilizando o e-mail como chave única de autenticação (username) do sistema.
 */
import br.com.raizesnordeste.api.domain.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {

    private final UserRepository userRepository;

    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilizador não encontrado."));
    }
}
