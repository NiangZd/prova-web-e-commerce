package com.tads.webprojeto.dominio;

import com.tads.webprojeto.aplicacao.Lojista;
import org.springframework.stereotype.Repository;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class LojistaDAO {

    private Connection conexao;

    public LojistaDAO() {
        try {
            conexao = Conexao.getConnection();
        } catch (SQLException | URISyntaxException ex) {
            ex.printStackTrace();
        }
    }

    public boolean verificarEmailExistente(String email) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT COUNT(*) FROM \"Lojistas\" WHERE email_lojista = ?";
            ps = conexao.prepareStatement(sql);
            ps.setString(1, email);
            rs = ps.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public Lojista verificarLogin(String email, String senha) {
        PreparedStatement ps = null;
        ResultSet rs = null;
    
        try {
            String sql = "SELECT * FROM \"Lojistas\" WHERE email_lojista = ? AND senha_lojista = ?";
            ps = conexao.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, senha);
            rs = ps.executeQuery();
    
            if (rs.next()) {
                // Se encontrarmos um lojista com esse email e senha, criamos um objeto Lojista
                Lojista lojista = new Lojista();
                lojista.setNome(rs.getString("nome_lojista"));
                lojista.setEmail(rs.getString("email_lojista"));
  
                return lojista;
            } else {
                // Se não encontrarmos nenhum lojista com essas credenciais, retornamos null
                return null;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
}