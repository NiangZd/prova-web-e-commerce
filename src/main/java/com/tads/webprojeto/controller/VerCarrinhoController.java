package com.tads.webprojeto.controller;

import com.tads.webprojeto.CarrinhoStorage;
import com.tads.webprojeto.aplicacao.Produto;
import com.tads.webprojeto.dominio.ProdutoDAO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class VerCarrinhoController {

    @RequestMapping(value = "/verCarrinho", method = RequestMethod.GET)
    public void verCarrinho(HttpServletRequest request, HttpServletResponse response) throws IOException {

        var writer = response.getWriter();
        Cookie[] cookies = request.getCookies();
        String valorCookie = "";
        Boolean vazio = true;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(CarrinhoStorage.cookieCarrinho)) {
                    valorCookie = cookie.getValue();
                    vazio = false;
                    break;
                }
            }
        }

        if (vazio) {
            writer.println("<html><body><h1>Carrinho Vazio</h1><br><button onclick=\"window.location.href='homeCliente.html'\">Voltar para Home</button><body></html>");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("<html><body><table border='1'><tr><th>ID do Produto</th><th>Nome</th><th>Preco</th><th>Quantidade</th><th>Add</th><th>Remover</th></tr>");

        Map<Integer, Integer> contagemIds = new HashMap<>(); // usando o Map para contar os ids repetidos que estão no cookie
        String[] ids = valorCookie.split("_");
        for (String id : ids) {
            int intId = Integer.parseInt(id);
            contagemIds.put(intId, contagemIds.getOrDefault(intId, 0) + 1); // deixando os ids sem repetir nenhum
        }

        ProdutoDAO pDao = new ProdutoDAO();

        for (Map.Entry<Integer, Integer> entry : contagemIds.entrySet()) {
            int id = entry.getKey();
            int quantidadeRepetida = entry.getValue();
            Produto p = pDao.buscarPorId(id);
            int estoque = pDao.buscarQuantidade(id);

            if (quantidadeRepetida == estoque) {
                // não deixa adicionar caso esteja no máximo do estoque
                sb.append("<tr><td>").append(String.valueOf(p.getId())).append("</td><td>").append(p.getNome()).append("</td><td>").append(String.valueOf(p.getPreco())).append("</td><td>").append(String.valueOf(quantidadeRepetida)).append("</td><td>Maximo de estoque</td><td>").append("<a href='/carrinhoServletFromVerCarrinho?id=").append(String.valueOf(p.getId())).append("&comando=remove'>Remover</a></td></tr>");
            } else {
                sb.append("<tr><td>").append(String.valueOf(p.getId())).append("</td><td>").append(p.getNome()).append("</td><td>").append(String.valueOf(p.getPreco())).append("</td><td>").append(String.valueOf(quantidadeRepetida)).append("</td><td>").append("<a href='/carrinhoServletFromVerCarrinho?id=").append(String.valueOf(p.getId())).append("&comando=add'>Adicionar</a></td><td>").append("<a href='/carrinhoServletFromVerCarrinho?id=").append(String.valueOf(p.getId())).append("&comando=remove'>Remover</a></td></tr>");
            }

            CarrinhoStorage.idsCarrinho.add(p.getId());
            CarrinhoStorage.quantidadeProdutosCarrinho.add(quantidadeRepetida);
        }

        sb.append("</table><br><button onclick=\"window.location.href='homeCliente.html'\">Voltar para Home</button><br><button onclick=\"window.location.href='/finalizarCompra'\">Finalizar Compra</button></body></html>");
        writer.println(sb.toString());
    }
}
