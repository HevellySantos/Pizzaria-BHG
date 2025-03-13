package br.PI.Pizzaria.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class ProdutosController {

    @GetMapping("/produtosAdmin")
    public String produtosAdmin() {
        return "produtosAdmin"; // Retorna o nome do template HTML
    }

    @GetMapping("/produtosEstoquista")
    public String produtosEstoquista() {
        return "produtosEstoquista"; // Retorna o nome do template HTML
    }


}
