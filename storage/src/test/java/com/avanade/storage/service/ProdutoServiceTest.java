package com.avanade.storage.service;

import com.avanade.storage.entity.Produto;
import com.avanade.storage.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private ProdutoService produtoService;

    private Produto produto;

    @BeforeEach
    void setUp() {
        produto = new Produto();
        produto.setId(1L);
        produto.setNome("Teclado Mecânico");
        produto.setPreco(350.0);
    }

    @Test
    void findAll() {

        Produto produto2 = new Produto();
        produto2.setId(2L);
        produto2.setNome("Mouse Gamer");
        produto2.setPreco(150.0);

        when(produtoRepository.findAll()).thenReturn(Arrays.asList(produto, produto2));

        List<Produto> resultado = produtoService.findAll();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Teclado Mecânico", resultado.getFirst().getNome());
        verify(produtoRepository, times(1)).findAll();
    }

    @Test
    void save() {

        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

        Produto produtoSalvo = produtoService.save(produto);

        assertNotNull(produtoSalvo);
        assertEquals(1L, produtoSalvo.getId());
        assertEquals("Teclado Mecânico", produtoSalvo.getNome());
        verify(produtoRepository, times(1)).save(produto);
    }

    @Test
    void alter() {

        Produto produtoAtualizado = new Produto();
        produtoAtualizado.setNome("Teclado Mecânico RGB");
        produtoAtualizado.setPreco(399.90);

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(produtoRepository.save(any(Produto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Produto resultado = produtoService.alter(1L, produtoAtualizado);

        assertNotNull(resultado);
        assertEquals("Teclado Mecânico RGB", resultado.getNome());
        assertEquals(399.90, resultado.getPreco());
        verify(produtoRepository, times(1)).findById(1L);
        verify(produtoRepository, times(1)).save(produto);
    }

    @Test
    void alter_RuntimeException() {
        Produto produtoAtualizado = new Produto();
        when(produtoRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> produtoService.alter(1L, produtoAtualizado));

        assertEquals("Produto não encontrado com o ID: 1", exception.getMessage());
        verify(produtoRepository, times(1)).findById(1L);
        verify(produtoRepository, never()).save(any());
    }

    @Test
    void deleteById() {

        when(produtoRepository.existsById(1L)).thenReturn(false);
        assertDoesNotThrow(() -> produtoService.deleteById(1L));

        verify(produtoRepository, times(1)).existsById(1L);
        verify(produtoRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteById_RuntimeException() {

        when(produtoRepository.existsById(1L)).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            produtoService.deleteById(1L);
        });

        assertEquals("Produto não encontrado com o ID: 1", exception.getMessage());
        verify(produtoRepository, times(1)).existsById(1L);
        verify(produtoRepository, never()).deleteById(anyLong());
    }
}
