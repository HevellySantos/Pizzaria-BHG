package br.PI.Pizzaria.controllerProdutos;

import br.PI.Pizzaria.modelProdutos.Produto;
import br.PI.Pizzaria.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class EditarProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    // Método para exibir a página de edição do produto
    @GetMapping("/editar-produto/{id}")
    public String editarProduto(@PathVariable Long id, Model model) {
        Produto produto = produtoRepository.findById(id).orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        model.addAttribute("produto", produto);
        return "editarProduto"; // Nome da página de edição
    }

    // Método para salvar as alterações no produto
    @PostMapping("/editar-produto/{id}")
    public String salvarAlteracoes(@PathVariable Long id, @ModelAttribute Produto produto) {
        Produto produtoExistente = produtoRepository.findById(id).orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        produtoExistente.setNome(produto.getNome());
        produtoExistente.setDescricao(produto.getDescricao());
        produtoExistente.setQuantidade(produto.getQuantidade());
        produtoExistente.setValor(produto.getValor());
        produtoExistente.setAvaliacao(produto.getAvaliacao());

        produtoRepository.save(produtoExistente);
        return "redirect:/listar-produtos"; // Redireciona para a lista de produtos após salvar
    }

}