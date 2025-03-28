package br.PI.Pizzaria.ControllerCardapio;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControllerCardapio {
    @GetMapping("/cardapio")
    public String cardapio() {
        return "cardapio"; // Nome do arquivo .html no diretório templates
    }

    @GetMapping("/detalheProduto")
    public String detalheProduto() {
        return "detalheProduto"; // Nome do arquivo .html no diretório templates
    }

    @GetMapping("/frete")
    public String frete() {
        return "frete"; // Nome do arquivo .html no diretório templates
    }
}
