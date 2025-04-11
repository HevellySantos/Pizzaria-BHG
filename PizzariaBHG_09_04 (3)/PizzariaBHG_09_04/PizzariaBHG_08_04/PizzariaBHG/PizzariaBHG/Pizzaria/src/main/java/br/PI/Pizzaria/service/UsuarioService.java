package br.PI.Pizzaria.service;

import br.PI.Pizzaria.modelUsuarios.UpdateUsuarioDTO;
import br.PI.Pizzaria.modelUsuarios.Usuario;
import br.PI.Pizzaria.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario buscarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario atualizarUsuario(Long id, UpdateUsuarioDTO dto) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        usuarioExistente.setNome(dto.getNome());
        usuarioExistente.setCpf(dto.getCpf());
        usuarioExistente.setRole(dto.getRole());

        // ⚠️ Encripta a senha apenas se ela foi preenchida no formulário
        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            // Garante que não está passando uma senha já encriptada
            if (!dto.getSenha().startsWith("$2")) {
                String senhaEncriptada = passwordEncoder.encode(dto.getSenha());
                usuarioExistente.setSenha(senhaEncriptada);
            } else {
                usuarioExistente.setSenha(dto.getSenha());
            }
        }

        return usuarioRepository.save(usuarioExistente);
    }

    public Usuario verificarUsuarioAtivo(Long userId) {
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        if (!usuario.isAtivo()) {
            throw new IllegalStateException("Usuário inativo. Entre em contato com o administrador.");
        }

        return usuario;
    }

    public Usuario validarLogin(String email, String senha) {
        Usuario usuario = usuarioRepository.login(email, senha)
                .orElseThrow(() -> new IllegalArgumentException("Usuário ou senha inválidos."));

        if (!usuario.isAtivo()) {
            throw new IllegalStateException("Usuário inativo. Entre em contato com o administrador.");
        }

        return usuario;
    }


    public Usuario salvarUsuario(Usuario usuario){
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);
        return usuarioRepository.save(usuario);
    }






}
