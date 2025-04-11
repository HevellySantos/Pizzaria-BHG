package br.PI.Pizzaria.controllerProdutos;

import br.PI.Pizzaria.modelProdutos.Produto;
import br.PI.Pizzaria.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    // Método GET para exibir o formulário de cadastro de produto
    @GetMapping("/cadastroProduto")
    public String exibirFormularioCadastro(Model model) {
        model.addAttribute("avaliacoes", new Integer[]{1, 2, 3, 4, 5});
        return "cadastroProduto";
    }

    // Método GET para exibir a lista de produtos com paginação e suporte à busca
    @GetMapping("/listar-produtos")
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

        return "listar-produtos";  // Retorna o nome da view (listar-produtos.html)
    }

    @GetMapping("/alterar-status/{id}")
    public String alterarStatus(@PathVariable Long id) {
        Produto produto = produtoRepository.findById(id).orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        boolean novoStatus = (produto.getAtivo() != null) ? produto.getAtivo() : false;
        produto.setAtivo(!novoStatus); // Alterna o status

        produtoRepository.save(produto);
        return "redirect:/listar-produtos";
    }

    @PostMapping("/salvar-produto")
    public String salvarProduto(@ModelAttribute Produto produto,
                                @RequestParam("file") MultipartFile file,
                                Model model) {

        try {
            if (!file.isEmpty()) {
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path uploadPath = Paths.get("uploads");

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(fileName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                produto.setImagem(fileName);
            }
        } catch (IOException e) {
            e.printStackTrace(); // Trate melhor em produção!
        }

        produtoRepository.save(produto);
        return "redirect:/listar-produtos";
    }


}

