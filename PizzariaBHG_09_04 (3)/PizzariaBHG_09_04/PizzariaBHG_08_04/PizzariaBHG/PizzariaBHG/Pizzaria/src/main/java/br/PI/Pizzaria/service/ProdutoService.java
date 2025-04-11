package br.PI.Pizzaria.service;

import br.PI.Pizzaria.modelProdutos.Produto;
import br.PI.Pizzaria.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id).orElse(null);
    }

    public Produto salvar(Produto produto) {
        return produtoRepository.save(produto);
    }

    public void atualizarProduto(Long id, Produto produtoAtualizado, MultipartFile imagem) throws IOException {
        Produto produtoExistente = buscarPorId(id);
        if (produtoExistente != null) {
            produtoExistente.setNome(produtoAtualizado.getNome());
            produtoExistente.setDescricao(produtoAtualizado.getDescricao());
            produtoExistente.setQuantidade(produtoAtualizado.getQuantidade());
            produtoExistente.setValor(produtoAtualizado.getValor());
            produtoExistente.setAvaliacao(produtoAtualizado.getAvaliacao());
            produtoExistente.setAtivo(produtoAtualizado.getAtivo());

            if (!imagem.isEmpty()) {
                String nomeArquivo = imagem.getOriginalFilename();
                Path caminho = Paths.get("src/main/resources/static/uploads/", nomeArquivo);
                Files.write(caminho, imagem.getBytes());
                produtoExistente.setImagem(nomeArquivo);
            }

            produtoRepository.save(produtoExistente);
        }
    }
}
