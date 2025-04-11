package br.PI.Pizzaria.controllerCardapio;

import br.PI.Pizzaria.modelProdutos.Produto;
import br.PI.Pizzaria.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ControllerCardapio {

    @Autowired
    private ProdutoRepository produtoRepository;

    @GetMapping("/cardapio")
    public String cardapio(Model model) {
        List<Produto> produtos = produtoRepository.findByAtivoTrue();
        model.addAttribute("produtos", produtos);
        return "cardapio";
    }


    @GetMapping("/detalheProduto")
    public String detalheProduto() {
        return "detalheProduto";
    }

    @GetMapping("/frete")
    public String frete() {
        return "frete";
    }
}
