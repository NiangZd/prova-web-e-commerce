package com.tads.webprojeto.controller;

import com.tads.webprojeto.CarrinhoStorage;
import com.tads.webprojeto.aplicacao.Cliente;
import com.tads.webprojeto.aplicacao.Lojista;
import com.tads.webprojeto.dominio.ClienteDAO;
import com.tads.webprojeto.dominio.LojistaDAO;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
@Controller
public class LoginController {

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void doLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var email = request.getParameter("email");
        var senha = request.getParameter("senha");
        char remover = '@';

        ClienteDAO cDAO = new ClienteDAO();
        LojistaDAO lDAO = new LojistaDAO();

        Cliente cliente = cDAO.verificarLogin(email, senha);
        Lojista lojista = lDAO.verificarLogin(email, senha);

        if (cliente != null || lojista != null) {
            HttpSession session = request.getSession(true);

            System.out.println("Sessão criada para o usuário: " + email);

            if (cliente != null) {
                session.setAttribute("clienteLogado", true);
                email = email.replace(String.valueOf(remover), "");
                CarrinhoStorage.cookieCarrinho = email;
                response.sendRedirect("homeCliente.html");
            } else {
                session.setAttribute("lojistaLogado", true);
                response.sendRedirect("/homeLojista.html");
            }
        } else {
            response.sendRedirect("index.html?msg=O login falhou");
        }

    }

    @RequestMapping(value="/logout")
    public void doLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        if (session != null) {
            // Adiciona um log para registrar quando a sessão é invalidada
            System.out.println("Sessão invalidada para o usuário: " + session.getAttribute("email"));
            session.invalidate();
        }
        response.sendRedirect("index.html?msg=Usuario saiu");
    }

}
