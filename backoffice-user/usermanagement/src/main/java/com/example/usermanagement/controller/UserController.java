package com.example.usermanagement.controller;

import com.example.usermanagement.model.User;
import com.example.usermanagement.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuarios")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", userService.listarUsuarios());
        return "usuarios";
    }

    @GetMapping("/novo")
    public String novoUsuario(Model model) {
        model.addAttribute("usuario", new User());
        return "formulario";
    }

    @PostMapping("/salvar")
    public String salvarUsuario(@Valid @ModelAttribute User user, @RequestParam("confirmSenha") String confirmSenha) {
        userService.cadastrarUsuario(user, confirmSenha);  // Passa a confirmação da senha para o serviço
        return "redirect:/usuarios";
    }
}
