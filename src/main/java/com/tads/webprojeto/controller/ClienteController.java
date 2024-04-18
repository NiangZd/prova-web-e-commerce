package com.tads.webprojeto.controller;

import com.tads.webprojeto.dominio.ProdutoDAO;
import com.tads.webprojeto.aplicacao.Produto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import com.tads.webprojeto.CarrinhoStorage;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

@Controller
public class ClienteController {

    @GetMapping("/listarProdutosCliente")
    public void getAllProdutosCliente(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ProdutoDAO produtoDAO = new ProdutoDAO();
        List<Produto> produtos = produtoDAO.listarProdutos();

        response.setContentType("text/html");
        var writer = response.getWriter();

        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append("<html> <head> <title> Lista de Produtos </title> <style>");
        htmlBuilder.append("table { border-collapse: collapse; width: 80%; margin: 0 auto; }");
        htmlBuilder.append("th, td { border: 1px solid black;}");
        htmlBuilder.append("th, button { margin-top: 20px; margin-left: 10px}");
        htmlBuilder.append("</style></head> <body> <h2 style=\"text-align: center;\">Lista de Produtos</h2> <table>");
        htmlBuilder.append("<tr><th>Nome</th><th>Descrição</th><th>Preço</th><th>Estoque</th><th>Carrinho</th></tr>");

        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        for (Produto produto : produtos) {
            htmlBuilder.append("<tr>");
            htmlBuilder.append("<td>").append(produto.getNome()).append("</td>");
            htmlBuilder.append("<td>").append(produto.getDescricao()).append("</td>");
            htmlBuilder.append("<td>R$ ").append(decimalFormat.format(produto.getPreco())).append("</td>");
            if (produto.getQuantidade() == 0){
                htmlBuilder.append("<td>Sem Estoque</td>");
            } else {
                htmlBuilder.append("<td>").append(produto.getQuantidade()).append("</td>");
            }
            htmlBuilder.append("<td><a href='/carrinhoServlet?id=").append(produto.getId()).append("&comando=add'>Adicionar</a></td>");
            htmlBuilder.append("</tr>");
        }

        htmlBuilder.append("<tr>");
        htmlBuilder.append("<td colspan=\"5\" style=\"text-align: center;\">");
        htmlBuilder.append("<button onclick=\"window.location.href='verCarrinho'\">Ver Carrinho</button>");
        htmlBuilder.append("<button onclick=\"window.location.href='homeCliente.html'\">Voltar para Home</button></td>");
        htmlBuilder.append("</tr>");

        htmlBuilder.append("</body> </html>");

        writer.println(htmlBuilder.toString());
    }
}
