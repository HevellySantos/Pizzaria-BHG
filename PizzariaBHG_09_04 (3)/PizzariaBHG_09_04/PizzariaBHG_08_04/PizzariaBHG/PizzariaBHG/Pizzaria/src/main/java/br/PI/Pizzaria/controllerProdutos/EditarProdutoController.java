package br.PI.Pizzaria.controllerProdutos;

import br.PI.Pizzaria.modelProdutos.Produto;
import br.PI.Pizzaria.repository.ProdutoRepository;
import br.PI.Pizzaria.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Controller
public class EditarProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ProdutoService produtoService;

    // Exibe o formulário de edição
    @GetMapping("/editar-produto/{id}")
    public String mostrarFormularioEdicao(@PathVariable Long id, Model model) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        // Define uma imagem padrão, se necessário
        if (produto.getImagem() == null || produto.getImagem().isEmpty()) {
            produto.setImagem("imagem_padrao.jpg");
        }

        model.addAttribute("produto", produto);
        return "editarProduto"; // Thymeleaf: templates/editarProduto.html
    }

    @PostMapping("/editar-produto/{id}")
    public String salvarAlteracoes(@PathVariable Long id,
                                   @ModelAttribute Produto produtoForm,
                                   @RequestParam("imagem") MultipartFile imagem) {

        try {
            Produto produtoExistente = produtoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

            // Atualiza os campos do formulário
            produtoExistente.setNome(produtoForm.getNome());
            produtoExistente.setDescricao(produtoForm.getDescricao());
            produtoExistente.setQuantidade(produtoForm.getQuantidade());
            produtoExistente.setValor(produtoForm.getValor());
            produtoExistente.setAvaliacao(produtoForm.getAvaliacao());

            // Atualiza a imagem apenas se foi enviada uma nova
            if (imagem != null && !imagem.isEmpty()) {
                String nomeArquivo = imagem.getOriginalFilename();
                Path caminhoImagem = Paths.get("src/main/resources/static/uploads/" + nomeArquivo);
                Files.write(caminhoImagem, imagem.getBytes());

                produtoExistente.setImagem(nomeArquivo);
            }

            produtoRepository.save(produtoExistente);
            return "redirect:/listar-produtos";

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/erro";
        }
    }


}
