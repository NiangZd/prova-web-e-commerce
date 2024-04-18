package com.tads.webprojeto.controller;

import com.tads.webprojeto.aplicacao.Cliente;
import com.tads.webprojeto.dominio.ClienteDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

@Controller
public class CadastroController {
    @RequestMapping(value = "/cadastro", method = RequestMethod.POST)
    public void doCadastro(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var nome = request.getParameter("nome");
        var login = request.getParameter("login");
        var senha = request.getParameter("senha");
        var confirmaSenha = request.getParameter("confirma_senha");

        if (nome != null && !nome.isEmpty() && login != null && !login.isEmpty() &&
                senha != null && !senha.isEmpty() && confirmaSenha != null && !confirmaSenha.isEmpty()) {
            // Todos os campos estão preenchidos
            ClienteDAO cDAO = new ClienteDAO();
            // if (cDAO.jaExiste(login)) {
            // // O email já está cadastrado, redirecionar de volta ao formulário com uma
            // mensagem de erro
            // response.sendRedirect("cadastro.html?erro=Este email já está cadastrado");
            // return; // Sair do método, pois não podemos prosseguir com o cadastro
            // }
            if (senha.equals(confirmaSenha)) {
                // Senhas coincidem, continuar com o cadastro
                Cliente c = new Cliente(nome, login, senha);
                try {
                    cDAO.registrarCliente(c);
                    response.sendRedirect("index.html?msg=Cadastrado com sucesso!");

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    response.sendRedirect("cadastro.html?msg=Erro no cadastro");
                }
            } else {
                // Senhas não coincidem, redirecionar de volta ao formulário com uma mensagem de
                // erro
                response.sendRedirect("cadastro.html?erro=As senhas não coincidem");
            }
        } else {
            // Algum campo não foi preenchido, redirecionar de volta ao formulário com uma
            // mensagem de erro
            response.sendRedirect("cadastro.html?erro=Preencha todos os campos");
        }

    }

}