package br.PI.Pizzaria.repository;

import br.PI.Pizzaria.modelProdutos.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    // Busca produtos pelo nome com busca parcial, ignorando maiúsculas/minúsculas
    Page<Produto> findByNomeContainingIgnoreCase(String nome, PageRequest pageRequest);
    List<Produto> findByAtivoTrue();


}
