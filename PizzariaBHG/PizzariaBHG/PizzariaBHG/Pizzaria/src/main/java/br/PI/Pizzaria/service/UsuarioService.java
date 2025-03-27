package br.PI.Pizzaria.service;

import br.PI.Pizzaria.modelUsuarios.UpdateUsuarioDTO;
import br.PI.Pizzaria.modelUsuarios.Usuario;
import br.PI.Pizzaria.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario buscarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));
    }

    public Usuario atualizarUsuario(Long id, UpdateUsuarioDTO dto){
       Usuario usuario = usuarioRepository.findById(id).
               orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

       //verificação das senhas
        if(!dto.getSenha().equals(dto.getConfirmaSenha())){
            throw new IllegalArgumentException("Senha incorreta!");
        }

        usuario.setNome(dto.getNome());
        usuario.setSenha(dto.getSenha());
        usuario.setCpf(dto.getCpf());
        usuario.setRole(dto.getRole());

        return usuarioRepository.save(usuario);

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


}
