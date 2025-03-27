package br.PI.Pizzaria.controllerProdutos;

import br.PI.Pizzaria.modelProdutos.Produto;
import br.PI.Pizzaria.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
public class ProdutoControllerEstoq {

    @Autowired
    private ProdutoRepository produtoRepository;

    // Método GET para exibir a lista de produtos com paginação e suporte à busca
    @GetMapping("/listar-produtos-estoq")
    public String listarProdutos(
            @RequestParam(defaultValue = "0") int page,   // Pega o número da página, padrão é 0
            @RequestParam(defaultValue = "10") int size,  // Tamanho da página, padrão é 10
            @RequestParam(required = false) String buscaProduto,  // Parâmetro opcional para a busca
            Model model) {

        // Criação da página com ordenação decrescente pelo ID
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Order.desc("id")));

        Page<Produto> produtosPage;

        if (buscaProduto != null && !buscaProduto.isEmpty()) {
            // Chama o método de busca no repositório
            produtosPage = produtoRepository.findByNomeContainingIgnoreCase(buscaProduto, pageRequest);
        } else {
            // Caso não tenha busca, apenas exibe todos os produtos
            produtosPage = produtoRepository.findAll(pageRequest);
        }

        model.addAttribute("produtos", produtosPage.getContent());  // Lista de produtos na página
        model.addAttribute("totalPages", produtosPage.getTotalPages()); // Total de páginas
        model.addAttribute("currentPage", page);  // Página atual
        model.addAttribute("buscaProduto", buscaProduto);  // Mantém a busca no campo de pesquisa

        return "listar-produtos-estoq";  // Retorna o nome da view (listar-produtos.html)
    }

    @GetMapping("/produto/{id}")
    @ResponseBody
    public ResponseEntity<Produto> buscarProdutoPorId(@PathVariable Long id) {
        Optional<Produto> produtoOptional = produtoRepository.findById(id);
        if (produtoOptional.isPresent()) {
            return ResponseEntity.ok(produtoOptional.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }



}

