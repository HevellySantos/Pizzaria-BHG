package br.PI.Pizzaria.controllerUsuarios;

import br.PI.Pizzaria.modelUsuarios.Role;
import br.PI.Pizzaria.modelUsuarios.Usuario;
import br.PI.Pizzaria.repository.UsuarioRepository;
import br.PI.Pizzaria.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@Controller //define que a tela de Login é a principal - primeira de acesso
public class LoginController {

    @Autowired
    private UsuarioRepository ur;
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String dashboard() {
        return "index";

    }

    @PostMapping("/logar")
    public String loginUsuario(Usuario usuario, Model model) {
        Optional<Usuario> usuarioLogado = ur.login(usuario.getEmail(), usuario.getSenha());

        if (usuarioLogado.isPresent()) {
            Usuario user = usuarioLogado.get();

            if (!user.isAtivo()) {
                model.addAttribute("erro", "Usuário inativo. Entre em contato com o administrador.");
                return "login"; // Retorna para a página de login
            }

            // Redireciona conforme o tipo de usuário ativo
            if (user.getRole() == Role.ADMIN) {
                return "redirect:/adminDashboard";
            } else if (user.getRole() == Role.ESTOQUISTA) {
                return "redirect:/estoqueDashboard";
            }
        }

        model.addAttribute("erro", "Usuário ou senha inválidos!");
        return "login"; // Retorna para a página de login
    }

    @GetMapping("/adminDashboard")
    public String adminDashboard(HttpSession session) {
        Role role = (Role) session.getAttribute("role");

        if (role != null && role == Role.ADMIN) {
            return "adminDashboard"; // Página do admin
        } else {
            return "adminDashboard"; // Página de erro
        }

    }

    @GetMapping("/estoqueDashboard")
    public String estoqueDashboard(HttpSession session) {
        Role role = (Role) session.getAttribute("role");

        if (role != null && (role == Role.ADMIN || role == Role.ESTOQUISTA)) {
            return "estoqueDashboard"; // Página do estoque
        } else {
            return "estoqueDashboard";
        }
    }


    @GetMapping("/cadastroUsuario")
    public String cadastro() {
        return "cadastroUsuario";
    }

    @RequestMapping(value = "/cadastroUsuario", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> cadastroUsuario(@RequestBody @Valid Usuario usuario, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Dados inválidos");
        }

        usuarioService.salvarUsuario(usuario); // Aqui é o ponto chave
        return ResponseEntity.ok("Usuário cadastrado com sucesso!");
    }

}
