package com.tads.webprojeto.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.tads.webprojeto.dominio.ProdutoDAO;

import com.tads.webprojeto.aplicacao.Produto;

import java.io.IOException;
import java.util.List;

@Controller
public class LojistaController {

    @GetMapping("/produtosLojista")
    public void homeLojista(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // Buscar os produtos do ProdutoDAO
        ProdutoDAO produtoDAO = new ProdutoDAO();
        List<Produto> produtos = produtoDAO.buscarTodos();

        // Escrevendo a resposta
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println("<html><body>");
        response.getWriter().println("<h1>Tabela de Produtos</h1>");
        response.getWriter().println("<style>");
        response.getWriter().println("table {");
        response.getWriter().println("    width: 80%;"); // Define a largura da tabela como 80% da largura da página
        response.getWriter().println("    margin: 0 auto;"); // Centraliza a tabela na página
        response.getWriter().println("}");
        response.getWriter().println("<style>");
        response.getWriter().println("table {");
        response.getWriter().println("    width: 90%;"); // Aumenta a largura da tabela para 90% da largura da página
        response.getWriter().println("    margin: 0 auto;"); // Centraliza a tabela na página
        response.getWriter().println("    border-collapse: collapse;"); // Colapsa as bordas das células
        response.getWriter().println("}");
        response.getWriter().println("th, td {");
        response.getWriter().println("    padding: 10px;"); // Adiciona espaçamento interno de 10 pixels às células
        response.getWriter().println("}");
        response.getWriter().println("</style>");

        response.getWriter().println("<table border='1'>"); // Adiciona borda à tabela
        response.getWriter().println("<tr><th>ID</th><th>Nome</th><th>Descrição</th><th>Preço</th><th>Estoque</th></tr>");

        for (Produto produto : produtos) {
            response.getWriter().println("<tr>");
            response.getWriter().println("<td>" + produto.getId() + "</td>");
            response.getWriter().println("<td>" + produto.getNome() + "</td>");
            response.getWriter().println("<td>" + produto.getDescricao() + "</td>");
            response.getWriter().println("<td>" + produto.getPreco() + "</td>");
            response.getWriter().println("<td>" + produto.getQuantidade() + "</td>");
            response.getWriter().println("<td><a href=\"/editarProduto?id=" + produto.getId() + "\">Editar</a></td>");
            response.getWriter().println("<td><a href=\"/deletarProduto?id=" + produto.getId() + "\">Delete</a></td>");
            response.getWriter().println("</tr>");
        }

        response.getWriter().println("</table><br>");


        response.getWriter().println("<h2 style=\"text-align:center;\">ADICIONAR NOVO PRODUTO</h2>");
        response.getWriter().println(
                "<form action=\"/adicionarProduto\" method=\"post\" style=\"margin: auto; width: 50%; text-align: center;\">");
        response.getWriter().println("<label for=\"nome\" style=\"display:block;\">Nome do Produto:</label>");
        response.getWriter().println(
                "<input type=\"text\" id=\"nome\" name=\"nome\" style=\"width: 100%; margin-bottom: 10px;\"><br>");
        response.getWriter().println("<label for=\"descricao\" style=\"display:block;\">Descrição do Produto:</label>");
        response.getWriter().println(
                "<input type=\"text\" id=\"descricao\" name=\"descricao\" style=\"width: 100%; margin-bottom: 10px;\"><br>");
        response.getWriter().println("<label for=\"preco\" style=\"display:block;\">Preço do Produto:</label>");
        response.getWriter().println(
                "<input type=\"number\" id=\"preco\" name=\"preco\" style=\"width: 100%; margin-bottom: 10px;\"><br>");
        response.getWriter().println("<label for=\"estoque\" style=\"display:block;\">Quantidade em Estoque:</label>");
        response.getWriter().println(
                "<input type=\"number\" id=\"quantidade\" name=\"quantidade\" style=\"width: 100%; margin-bottom: 10px;\"><br><br>");
        response.getWriter()
                .println("<div style=\"text-align:center;\"><input type=\"submit\" value=\"Adicionar Produto\"></div>");
        response.getWriter().println("</form>");

        response.getWriter().println("<td colspan=\"5\" style=\"text-align: center;\">");
        response.getWriter().println("<button onclick=\"window.location.href='homeLojista.html'\">Voltar Home</button></td>");

        response.getWriter().println("</body></html>");
    }

    @PostMapping("/adicionarProduto")
    public void adicionandoProduto(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String nome = request.getParameter("nome");
        String descricao = request.getParameter("descricao");
        double preco = Double.parseDouble(request.getParameter("preco"));
    
        // Verifica se o parâmetro "quantidade" não é nulo ou vazio antes de tentar convertê-lo para inteiro
        String quantidadeStr = request.getParameter("quantidade");
        int quantidade = 0; // valor padrão
        if (quantidadeStr != null && !quantidadeStr.isEmpty()) {
            quantidade = Integer.parseInt(quantidadeStr);
        }
    
        Produto novoProduto = new Produto(0, nome, descricao, preco, quantidade);
    
        ProdutoDAO produtoDAO = new ProdutoDAO();
        produtoDAO.inserirProduto(novoProduto);
    
        response.sendRedirect("/produtosLojista");
    }

    @GetMapping("/deletarProduto")
    public void deletarProduto(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int idProduto = Integer.parseInt(request.getParameter("id"));

        ProdutoDAO produtoDAO = new ProdutoDAO();
        produtoDAO.deletarProduto(idProduto);

        response.sendRedirect("/produtosLojista");
    }

    @GetMapping("/editarProduto")
    public void editarProduto(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int idProduto = Integer.parseInt(request.getParameter("id"));

        ProdutoDAO produtoDAO = new ProdutoDAO();
        Produto produto = produtoDAO.buscarPorId(idProduto);

        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println(
                "<html><head><style>form {margin: auto; width: 50%; text-align: center;}</style></head><body>");
        response.getWriter().println("<h2 style=\"text-align:center;\">Editar Produto</h2>");
        response.getWriter().println("<form action=\"/salvarEdicaoProduto\" method=\"post\">");
        response.getWriter().println("<input type=\"hidden\" name=\"id\" value=\"" + produto.getId() + "\">");
        response.getWriter().println("<label for=\"nome\">Nome do Produto:</label><br>");
        response.getWriter()
                .println("<input type=\"text\" id=\"nome\" name=\"nome\" value=\"" + produto.getNome() + "\"><br>");
        response.getWriter().println("<label for=\"descricao\">Descrição do Produto:</label><br>");
        response.getWriter().println("<input type=\"text\" id=\"descricao\" name=\"descricao\" value=\""
                + produto.getDescricao() + "\"><br>");
        response.getWriter().println("<label for=\"preco\">Preço do Produto:</label><br>");
        response.getWriter().println(
                "<input type=\"number\" id=\"preco\" name=\"preco\" value=\"" + produto.getPreco() + "\"><br>");
        response.getWriter().println("<label for=\"estoque\">Quantidade em Estoque:</label><br>");
        response.getWriter().println("<input type=\"number\" id=\"quantidade\" name=\"quantidade\" value=\""
                + produto.getQuantidade() + "\"><br><br>");
        response.getWriter()
                .println("<div style=\"text-align:center;\"><input type=\"submit\" value=\"Salvar Alterações\"></div>");
        response.getWriter().println("</form>");
        response.getWriter().println("</body></html>");
    }

    @PostMapping("/salvarEdicaoProduto")
    public void salvarEdicaoProduto(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int idProduto = Integer.parseInt(request.getParameter("id"));
        String nome = request.getParameter("nome");
        String descricao = request.getParameter("descricao");
        double preco = Double.parseDouble(request.getParameter("preco"));
    
        // Verifica se o parâmetro "quantidade" não é nulo ou vazio antes de tentar convertê-lo para inteiro
        String quantidadeStr = request.getParameter("quantidade");
        int quantidade = 0; // Valor padrão
        if (quantidadeStr != null && !quantidadeStr.isEmpty()) {
            quantidade = Integer.parseInt(quantidadeStr);
        }
    
        Produto produto = new Produto(idProduto, nome, descricao, preco, quantidade);
    
        ProdutoDAO produtoDAO = new ProdutoDAO();
        produtoDAO.alterarProduto(produto);
    
        response.sendRedirect("/produtosLojista");
    }
}
