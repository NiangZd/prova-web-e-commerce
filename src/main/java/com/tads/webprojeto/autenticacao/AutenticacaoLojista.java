package com.tads.webprojeto.autenticacao;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

//Filtrando as páginas que o lojista não pode acessar
@WebFilter(urlPatterns = { "/homeCliente.html", "/verCarrinho" })
public class AutenticacaoLojista implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletResponse response = ((HttpServletResponse) servletResponse);
        HttpServletRequest request = ((HttpServletRequest) servletRequest);

        System.out.println("filtrou");

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect("index.html?msg=Você precisa logar antes");
        } else {
            Boolean lojistaLogado = (Boolean) session.getAttribute("lojistaLogado");
            if (lojistaLogado != null && lojistaLogado.booleanValue()) {
                response.sendRedirect("homeLojista.html?msg=Você precisa logar como cliente antes");
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}