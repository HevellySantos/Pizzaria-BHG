package br.PI.Pizzaria.service;

import br.PI.Pizzaria.modelCarrinho.ItemCarrinho;
import br.PI.Pizzaria.modelProdutos.Produto;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class CarrinhoService {

    public List<ItemCarrinho> obterCarrinho(HttpSession session) {
        List<ItemCarrinho> carrinho = (List<ItemCarrinho>) session.getAttribute("carrinho");
        if (carrinho == null) {
            carrinho = new ArrayList<>();
            session.setAttribute("carrinho", carrinho);
        }
        return carrinho;
    }

    public void adicionarProduto(Produto produto, HttpSession session) {
        List<ItemCarrinho> carrinho = obterCarrinho(session);

        for (ItemCarrinho item : carrinho) {
            if (item.getId().equals(produto.getId())) {
                return;
            }
        }

        ItemCarrinho novo = new ItemCarrinho();
        novo.setId(produto.getId());
        novo.setNome(produto.getNome());
        novo.setValor(produto.getValor());
        carrinho.add(novo);
    }

    public void removerProduto(Long produtoId, HttpSession session) {
        List<ItemCarrinho> carrinho = obterCarrinho(session);
        carrinho.removeIf(item -> item.getId().equals(produtoId));
    }

    public void incrementarQuantidade(Long produtoId, HttpSession session) {
        List<ItemCarrinho> carrinho = obterCarrinho(session);
        for (ItemCarrinho item : carrinho) {
            if (item.getId().equals(produtoId)) {
                item.setQuantidade(item.getQuantidade() + 1);
                break;
            }
        }
    }

    public void decrementarQuantidade(Long produtoId, HttpSession session) {
        List<ItemCarrinho> carrinho = obterCarrinho(session);
        Iterator<ItemCarrinho> iterator = carrinho.iterator();
        while (iterator.hasNext()) {
            ItemCarrinho item = iterator.next();
            if (item.getId().equals(produtoId)) {
                if (item.getQuantidade() > 1) {
                    item.setQuantidade(item.getQuantidade() - 1);
                } else {
                    iterator.remove();
                }
                break;
            }
        }
    }

    public double calcularTotal(List<ItemCarrinho> carrinho) {
        return carrinho.stream()
                .mapToDouble(item -> item.getValor() * item.getQuantidade())
                .sum();
    }
}
