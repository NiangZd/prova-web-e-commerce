package com.tads.webprojeto.controller;

import com.tads.webprojeto.aplicacao.Produto;
import com.tads.webprojeto.dominio.ProdutoDAO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.tads.webprojeto.CarrinhoStorage;

import java.io.IOException;
import java.util.ArrayList;

@Controller
public class CarrinhoController {

    @GetMapping("/carrinhoServlet")
    public void controlCarrinho(@RequestParam int id, @RequestParam String comando, HttpServletRequest request,
                                HttpServletResponse response) throws IOException {

        String idProduto = String.valueOf(id);
        String nomeCarrinho = CarrinhoStorage.cookieCarrinho;

        if (comando.equals("add")) {
            String idProdutos = getIdProdutosFromCookie(request, nomeCarrinho);
            int quantidadeNoCarrinho = idProdutos.split("_").length - 1; // Contagem de itens no carrinho

            // Verifica se o item está disponível no estoque e se ainda há espaço no carrinho
            ProdutoDAO pDao = new ProdutoDAO();
            int quantidadeDisponivel = pDao.buscarQuantidade(id);
            if (quantidadeDisponivel == 0 || quantidadeNoCarrinho == quantidadeDisponivel) {
                response.sendRedirect("/listarProdutosCliente?msg=Produto sem estoque ou carrinho cheio");
                return;
            }

            idProdutos += idProduto + "_";

            // Atualizar o cookie
            setCookie(response, nomeCarrinho, idProdutos);

            response.sendRedirect("/listarProdutosCliente");
        } else if (comando.equals("remove")) {
            // Remover o produto do carrinho
            String idProdutos = getIdProdutosFromCookie(request, nomeCarrinho);
            idProdutos = idProdutos.replaceFirst(idProduto + "_", "");

            // Atualizar o cookie
            if (idProdutos.isEmpty()) {
                deleteCookie(response, nomeCarrinho);
                response.sendRedirect("homeCliente.html");
            } else {
                setCookie(response, nomeCarrinho, idProdutos);
                response.sendRedirect("/verCarrinho");
            }
        }
    }



    @GetMapping("/carrinhoServletFromVerCarrinho")
    public void controlCarrinhoFromVerCarrinho(@RequestParam int id, @RequestParam String comando,
                                               HttpServletRequest request, HttpServletResponse response) throws IOException {

        String idProduto = String.valueOf(id);
        String nomeCarrinho = CarrinhoStorage.cookieCarrinho;
        ProdutoDAO pDao = new ProdutoDAO(); // Instanciando o DAO aqui

        int quantidade = pDao.buscarQuantidade(id);
        String idProdutos = getIdProdutosFromCookie(request, nomeCarrinho);
        int totalItensCarrinho = idProdutos.split("_").length - 1; // Contagem de itens no carrinho

        if (comando.equals("add")) {
            if (quantidade == 0 || totalItensCarrinho == quantidade) {
                response.sendRedirect("/verCarrinho?msg=Produto sem estoque");
                return;
            }

            // Adicionando o ID do produto ao carrinho
            idProdutos += idProduto + "_"; // Adicionando o novo ID ao final da string

            setCookie(response, nomeCarrinho, idProdutos);
            response.sendRedirect("/verCarrinho");

        } else if (comando.equals("remove")) {
            // Removendo o ID do produto do carrinho
            idProdutos = idProdutos.replaceFirst(idProduto + "_", ""); // Removendo o ID da string

            if (idProdutos.isEmpty()) {
                deleteCookie(response, nomeCarrinho);
                response.sendRedirect("homeCliente.html");
            } else {
                setCookie(response, nomeCarrinho, idProdutos);
                response.sendRedirect("/verCarrinho");
            }
        }
    }



    @GetMapping("/finalizarCompra")
    public void finalizarCompra(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ArrayList<Integer> arrayIds = CarrinhoStorage.idsCarrinho;
        ArrayList<Integer> arrayQuantidades = CarrinhoStorage.quantidadeProdutosCarrinho;
        ProdutoDAO pDao = new ProdutoDAO();

        double valorTotalCompra = 0.0;
        int totalProdutosComprados = 0;

        // Calcular o valor total da compra e o total de produtos comprados
        for (int i = 0; i < arrayIds.size(); i++) {
            int id = arrayIds.get(i);
            int quantidade = arrayQuantidades.get(i);
            Produto produto = pDao.buscarPorId(id);
            double preco = produto.getPreco();

            valorTotalCompra += preco * quantidade;
            totalProdutosComprados += quantidade;
        }

        // Verificar se há estoque suficiente para todos os produtos no carrinho
        boolean estoqueSuficiente = true;
        for (int i = 0; i < arrayIds.size(); i++) {
            int id = arrayIds.get(i);
            int quantidade = arrayQuantidades.get(i);
            Produto produto = pDao.buscarPorId(id);

            if (produto.getQuantidade() < quantidade) {
                estoqueSuficiente = false;
                break;
            }
        }

        if (estoqueSuficiente) {
            // Atualizar o estoque para cada produto no carrinho
            for (int i = 0; i < arrayIds.size(); i++) {
                int id = arrayIds.get(i);
                int quantidade = arrayQuantidades.get(i);

                Produto produto = pDao.buscarPorId(id);
                int estoqueAtualizado = produto.getQuantidade() - quantidade;
                pDao.updateQuantidade(id, estoqueAtualizado);
            }

            // Limpar o carrinho
            deleteCookie(response, CarrinhoStorage.cookieCarrinho);

            // Redirecionar para a página de home com mensagem de compra realizada e dados da compra
            String redirectURL = String.format("homeCliente.html?msg=Compra realizada. Total de produtos: %d. Valor total: %.2f", totalProdutosComprados, valorTotalCompra);
            response.sendRedirect(redirectURL);
        } else {
            // Se não houver estoque suficiente, redirecionar para a página de listagem de produtos com mensagem de erro
            response.sendRedirect("/listarProdutosCliente?msg=Estoque insuficiente para finalizar a compra");
        }
    }


    private String getIdProdutosFromCookie(HttpServletRequest request, String cookieName) {
        String idProdutos = "";
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    idProdutos = cookie.getValue();
                    break;
                }
            }
        }
        return idProdutos;
    }

    private void setCookie(HttpServletResponse response, String cookieName, String value) {
        Cookie carrinho = new Cookie(cookieName, value);
        carrinho.setMaxAge(172800); // tempo de vida em segundos
        response.addCookie(carrinho);
    }

    private void deleteCookie(HttpServletResponse response, String cookieName) {
        Cookie carrinho = new Cookie(cookieName, "");
        carrinho.setMaxAge(0);
        response.addCookie(carrinho);
    }
}
