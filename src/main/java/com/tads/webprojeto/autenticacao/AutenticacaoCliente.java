package com.tads.webprojeto.autenticacao;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(urlPatterns = { "/homeLojista.html", "/cadastroProduto.html" })
public class AutenticacaoCliente implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect("index.html?msg=Você precisa logar antes");
            return;
        }

        Boolean clienteLogado = (Boolean) session.getAttribute("clienteLogado");
        Boolean lojistaLogado = (Boolean) session.getAttribute("lojistaLogado");

        if (clienteLogado == null && lojistaLogado == null) {
            response.sendRedirect("index.html?msg=Você precisa logar antes");
            return;
        }

        if (clienteLogado != null && clienteLogado) {
            response.sendRedirect("homeCliente.html?msg=Você não possui autorizacao");
            return;
        }

        if (lojistaLogado != null && lojistaLogado) {
            // Permitir acesso para lojistas
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        response.sendRedirect("index.html?msg=Você precisa logar antes");
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
