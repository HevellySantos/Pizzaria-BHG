package br.PI.Pizzaria.controllerClientes;

import br.PI.Pizzaria.modelCliente.Cliente;
import br.PI.Pizzaria.repository.ClienteRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


@Controller
public class LoginCliController {

    @Autowired
    private ClienteRepository ur;

    @GetMapping("/loginCliente")
    public String loginCliente() {
        return "loginCliente";
    }

    @GetMapping("/cadastroCliente")
    public String cadastroCliente() {
        return "cadastroCliente";
    }

    @PostMapping("/loginCliente")
    public String processaLogin(@RequestParam String email, @RequestParam String senha, Model model) {
        Cliente cliente = ur.findByEmail(email);

        if (cliente != null && passwordEncoder.matches(senha, cliente.getSenha())) {
            return "redirect:/cardapio";
        }

        model.addAttribute("erro", "Email ou senha inválidos");
        return "loginCliente";
    }


    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/cadastroCliente")
    public String cadastroCliente(@Valid Cliente cliente, BindingResult result, Model model) {
        if (ur.findByEmail(cliente.getEmail()) != null) {
            model.addAttribute("erroEmail", "Este e-mail já está em uso.");
            return "cadastroCliente";
        }

        if (result.hasErrors()) {
            return "cadastroCliente";
        }

        if (cliente.getDataNascimento() == null) {
            result.rejectValue("dataNascimento", "error.cliente", "Data de nascimento é obrigatória.");
            return "cadastroCliente";
        }


        if (ur.findByCpf(cliente.getCpf()) != null) {
            model.addAttribute("erroCpf", "Este CPF já está cadastrado.");
            return "cadastroCliente";
        }

        if (!cliente.getSenha().equals(cliente.getConfirmarSenha())) {
            result.rejectValue("confirmarSenha", "erro.confirmarSenha", "As senhas não conferem.");
            return "cliente/cadastro";
        }


        // 🔒 Criptografa a senha antes de salvar
        cliente.setSenha(passwordEncoder.encode(cliente.getSenha()));

        ur.save(cliente);
        return "redirect:/loginCliente";
    }

}



