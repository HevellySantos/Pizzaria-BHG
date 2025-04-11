package br.PI.Pizzaria.controllerCarrinho;

import br.PI.Pizzaria.modelCarrinho.ItemCarrinho;
import br.PI.Pizzaria.modelProdutos.Produto;
import br.PI.Pizzaria.service.CarrinhoService;
import br.PI.Pizzaria.service.ProdutoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/carrinho")
public class CarrinhoController {

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private CarrinhoService carrinhoService;

    @PostMapping("/adicionar/{id}")
    public String adicionarAoCarrinho(@PathVariable Long id,
                                      HttpSession session) {
        Produto produto = produtoService.buscarPorId(id);

        if (produto != null) {
            carrinhoService.adicionarProduto(produto, session);
        }

        return "redirect:/carrinho";
    }

    @GetMapping
    public String verCarrinho(Model model, HttpSession session) {
        List<ItemCarrinho> carrinho = carrinhoService.obterCarrinho(session);
        double totalCompra = carrinhoService.calcularTotal(carrinho);

        model.addAttribute("carrinho", carrinho);
        model.addAttribute("totalCompra", totalCompra);

        return "carrinho";
    }

    @PostMapping("/remover")
    public String removerProduto(@RequestParam("produtoId") Long produtoId, HttpSession session) {
        carrinhoService.removerProduto(produtoId, session);
        return "redirect:/carrinho";
    }

    @PostMapping("/incrementar")
    public String incrementarQuantidade(@RequestParam("produtoId") Long produtoId, HttpSession session) {
        carrinhoService.incrementarQuantidade(produtoId, session);
        return "redirect:/carrinho";
    }

    @PostMapping("/decrementar")
    public String decrementarQuantidade(@RequestParam("produtoId") Long produtoId, HttpSession session) {
        carrinhoService.decrementarQuantidade(produtoId, session);
        return "redirect:/carrinho";
    }
}
