package br.PI.Pizzaria.controller;

import br.PI.Pizzaria.model.Role;
import br.PI.Pizzaria.model.Usuario;
import br.PI.Pizzaria.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Optional;


@Controller //define que a tela de Login é a principal - primeira de acesso
public class LoginController {

    @Autowired
    private UsuarioRepository ur;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String dashboard() {
        return "index";

    }

    @PostMapping("/logar")
    public String loginUsuario(Usuario usuario, Model model, HttpServletRequest request) {
        Optional<Usuario> usuarioLogado = ur.login(usuario.getEmail(), usuario.getSenha());

        if (usuarioLogado.isPresent()) {
            request.getSession().setAttribute("role", usuarioLogado.get().getRole()); // Guarda a role na sessão

            // Redireciona conforme o tipo de usuário
            if (usuarioLogado.get().getRole() == Role.ADMIN) {
                return "redirect:/adminDashboard";
            } else if (usuarioLogado.get().getRole() == Role.ESTOQUISTA) {
                return "redirect:/estoqueDashboard";
            } else if (usuarioLogado.get().getRole() == Role.CLIENTE) {
                return "redirect:/acesso_negado";
            }
        }

        model.addAttribute("erro", "Usuário Inválido!");
        return "redirect:/login?erro=true";
    }


    @GetMapping("/adminDashboard")
    public String adminDashboard(HttpSession session) {
        Role role = (Role) session.getAttribute("role");

        if (role != null && role == Role.ADMIN) {
            return "adminDashboard"; // Página do admin
        } else {
            return "acesso_negado"; // Página de erro
        }

    }

    @GetMapping("/estoqueDashboard")
    public String estoqueDashboard(HttpSession session) {
        Role role = (Role) session.getAttribute("role");

        if (role != null && (role == Role.ADMIN || role == Role.ESTOQUISTA)) {
            return "estoqueDashboard"; // Página do estoque
        } else {
            return "acesso_negado";
        }
    }


    @GetMapping("/index")
    public String clienteDashboard(HttpSession session) {
        Role role = (Role) session.getAttribute("role");

        if (role == Role.CLIENTE) {
            return "index";
        } else {
            return "acesso_negado";
        }
    }



    @GetMapping("/cadastroUsuario")
    public String cadastro() {
        return "cadastroUsuario";
    }

    @RequestMapping(value = "/cadastroUsuario", method = RequestMethod.POST)
    public String cadastroUsuario(@Valid Usuario usuario, BindingResult result) {

        if (result.hasErrors()) {
            return "redirect:/cadastroUsuario";
        }

        ur.save(usuario);

        return "redirect:/login";
    }

}
