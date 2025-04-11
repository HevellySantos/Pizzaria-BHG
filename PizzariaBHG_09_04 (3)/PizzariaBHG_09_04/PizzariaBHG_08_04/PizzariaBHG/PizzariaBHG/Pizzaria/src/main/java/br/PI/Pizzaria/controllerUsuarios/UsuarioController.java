package br.PI.Pizzaria.controllerUsuarios;

import br.PI.Pizzaria.modelUsuarios.Usuario;
import br.PI.Pizzaria.repository.UsuarioRepository;
import br.PI.Pizzaria.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // 🔹 Exibir página com a lista de usuários ou buscar pelo nome
    @GetMapping
    public String listarUsuarios(
            @RequestParam(required = false) String buscaUsuario, // Parâmetro opcional para busca
            Model model) {

        List<Usuario> usuarios;

        if (buscaUsuario != null && !buscaUsuario.isEmpty()) {
            // Busca usuários pelo nome, ignorando maiúsculas e minúsculas
            usuarios = usuarioRepository.findByNomeContainingIgnoreCase(buscaUsuario);
        } else {
            // Busca todos os usuários
            usuarios = usuarioRepository.findAll();
        }

        model.addAttribute("usuarios", usuarios); // Adiciona a lista ao modelo
        model.addAttribute("buscaUsuario", buscaUsuario); // Mantém o valor do campo de busca

        return "usuarios"; // Retorna a página de usuários
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

    @RestController
    @RequestMapping("/acoes")
    public class AcaoController {

        @Autowired
        private UsuarioService usuarioService;

        @PostMapping("/realizar")
        public ResponseEntity<String> realizarAcao(@RequestParam Long userId) {
            try {
                // Verifica se o usuário está ativo
                usuarioService.verificarUsuarioAtivo(userId);

                // Lógica da ação
                return ResponseEntity.ok("Ação realizada com sucesso!");

            } catch (IllegalStateException e) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
        }
    }





}



