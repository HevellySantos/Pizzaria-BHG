package br.PI.Pizzaria.controller;

import br.PI.Pizzaria.model.Usuario;
import br.PI.Pizzaria.model.Role;
import br.PI.Pizzaria.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller  // Controller para Thymeleaf
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // 🔹 Exibir página com a lista de usuários
    @GetMapping
    public String listarUsuarios(Model model) {
        List<Usuario> usuarios = (List<Usuario>) usuarioRepository.findAll();
        model.addAttribute("usuarios", usuarios);
        return "usuarios";  // Renderiza a página de listagem de usuários
    }

    // 🔹 Perfil do usuário
    @GetMapping("/perfil")
    public String perfilUsuario(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login"; // Se não estiver logado, redireciona para o login
        }

        model.addAttribute("usuario", usuario);
        return "perfil"; // Retorna a página de perfil
    }

    // 🔹 Alterar status do usuário (Usando formulário Thymeleaf)
    @PostMapping("/{id}/toggle-status")
    public String toggleStatus(@PathVariable Long id) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);

        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            usuario.setAtivo(!usuario.isAtivo());  // Alterna o status
            usuarioRepository.save(usuario);
        }

        return "redirect:/usuarios";  // Redireciona para a página de listagem de usuários
    }
}

