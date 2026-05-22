package com.avanade.storage.repository;

import com.avanade.storage.entity.Produto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
 class ProdutoRepositoryTest {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Test
    @DisplayName("Deve persistir um produto com sucesso no banco H2")
    void save_DeveSalvarProduto() {

        Produto produto = new Produto();
        produto.setNome("Teclado Mecânico");
        produto.setPreco(350.00);


        Produto produtoSalvo = produtoRepository.save(produto);

        assertThat(produtoSalvo).isNotNull();
        assertThat(produtoSalvo.getId()).isNotNull(); // O H2 deve gerar o ID automaticamente
        assertThat(produtoSalvo.getNome()).isEqualTo("Teclado Mecânico");
    }

    @Test
    @DisplayName("Deve buscar um produto por ID com sucesso")
    void findById_DeveRetornarProduto() {
        // Arrange
        Produto produto = new Produto();
        produto.setNome("Mouse Gamer");
        produto.setPreco(150.00);
        Produto produtoSalvo = produtoRepository.save(produto);


        Optional<Produto> produtoEncontrado = produtoRepository.findById(produtoSalvo.getId());

        assertThat(produtoEncontrado).isPresent();
        assertThat(produtoEncontrado.get().getNome()).isEqualTo("Mouse Gamer");
    }

    @Test
    @DisplayName("Deve retornar Optional empty ao buscar por um ID inexistente")
    void findById_DeveRetornarVazioSeNaoExistir() {

        Optional<Produto> produtoEncontrado = produtoRepository.findById(999L);

        assertThat(produtoEncontrado).isEmpty();
    }

    @Test
    @DisplayName("Deve deletar um produto com sucesso")
    void delete_DeveRemoverProduto() {
        // Arrange
        Produto produto = new Produto();
        produto.setNome("Monitor 24p");
        Produto produtoSalvo = produtoRepository.save(produto);

        produtoRepository.deleteById(produtoSalvo.getId());
        Optional<Produto> produtoDeletado = produtoRepository.findById(produtoSalvo.getId());

        assertThat(produtoDeletado).isEmpty();
    }
}
