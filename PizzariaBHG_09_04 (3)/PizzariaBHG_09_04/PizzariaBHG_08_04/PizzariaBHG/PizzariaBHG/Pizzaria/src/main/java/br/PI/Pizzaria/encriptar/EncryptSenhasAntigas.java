package br.PI.Pizzaria.encriptar;

import br.PI.Pizzaria.modelUsuarios.Usuario;
import br.PI.Pizzaria.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EncryptSenhasAntigas implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public EncryptSenhasAntigas(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        List<Usuario> usuarios = usuarioRepository.findAll();

        for (Usuario usuario : usuarios) {
            String senha = usuario.getSenha();
            // Verifica se a senha já está encriptada (BCrypt começa com $2)
            if (!senha.startsWith("$2")) {
                usuario.setSenha(passwordEncoder.encode(senha));
                usuarioRepository.save(usuario);
                System.out.println("Senha encriptada para: " + usuario.getEmail());
            }
        }

        System.out.println("Senhas atualizadas!");
    }
}

