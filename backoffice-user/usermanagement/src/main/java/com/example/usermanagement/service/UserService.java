package com.example.usermanagement.service;

import com.example.usermanagement.model.User;
import com.example.usermanagement.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> listarUsuarios() {
        return userRepository.findAll();
    }

    public void cadastrarUsuario(@Valid User user, String confirmSenha) {
        // Verificar se o e-mail já existe
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email já cadastrado!");
        }

        // Verificar se o CPF já existe
        if (userRepository.findByCpf(user.getCpf()).isPresent()) {
            throw new RuntimeException("CPF já cadastrado!");
        }

        // Verificar se as senhas são iguais
        if (!user.getSenha().equals(confirmSenha)) {
            throw new RuntimeException("As senhas não coincidem!");
        }

        userRepository.save(user);
    }

    public User buscarUsuario(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public void atualizarUsuario(User user) {
        userRepository.save(user);
    }

    public void ativarDesativarUsuario(Long id) {
        User user = buscarUsuario(id);
        user.setAtivo(!user.isAtivo());
        userRepository.save(user);
    }

    public void alterarSenha(Long id, String novaSenha) {
        User user = buscarUsuario(id);
        user.setSenha(novaSenha); // As senhas devem ser criptografadas dentro do modelo, conforme você já fez
        userRepository.save(user);
    }
}
