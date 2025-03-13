package br.PI.Pizzaria.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AcessoNegadoController {

    @GetMapping("/acesso_negado")
    public String acessoNegado() {
        return "acesso_negado"; // Retorna o nome do arquivo HTML dentro de templates
    }
}
