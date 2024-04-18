package com.tads.webprojeto.dominio;

import com.tads.webprojeto.aplicacao.Cliente;

import org.springframework.stereotype.Repository;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class ClienteDAO {

    private Connection conexao;

    public ClienteDAO() {
        try {
            conexao = Conexao.getConnection();
        } catch (SQLException | URISyntaxException ex) {
            ex.printStackTrace();
        }
    }

    public Cliente verificarLogin(String email, String senha) {
        ResultSet rs = null;
        Cliente c = null;
    
        try {
            String sql = "SELECT * FROM \"Clientes\" WHERE email_cliente = ? AND senha_cliente = ?";
            PreparedStatement ps = conexao.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, senha);
    
            rs = ps.executeQuery();
            if (rs.next()) {
                c = new Cliente(rs.getString("nome_cliente"), rs.getString("email_cliente"), rs.getString("senha_cliente"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    
        return c;
    }
    
    
    /* 
    public boolean verificarLogin(Cliente cliente) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT COUNT(*) FROM \"clientes\" WHERE email_cliente = ? AND senha_cliente = ?";
            ps = conexao.prepareStatement(sql);
            ps.setString(1, cliente.getEmail());
            ps.setString(2, cliente.getSenha());
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
    }*/

    public boolean verificarEmailExistente(String email) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT COUNT(*) FROM \"Clientes\" WHERE email_cliente = ?";
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

    public void registrarCliente(Cliente cliente) {
        PreparedStatement ps = null;

        try {
            String sql = "INSERT INTO \"Clientes\" (nome_cliente, email_cliente, senha_cliente) VALUES (?, ?, ?)";
            ps = conexao.prepareStatement(sql);
            ps.setString(1, cliente.getNome());
            ps.setString(2, cliente.getEmail());
            ps.setString(3, cliente.getSenha());
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

}