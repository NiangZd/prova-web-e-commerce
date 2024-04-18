package com.tads.webprojeto.dominio;

import com.tads.webprojeto.aplicacao.Produto;

import org.springframework.stereotype.Repository;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProdutoDAO {

    private Connection conexao;

    public ProdutoDAO() {
        try {
            conexao = Conexao.getConnection();
        } catch (SQLException | URISyntaxException ex) {
            ex.printStackTrace();
        }
    }
    
    private static final String SQL_BUSCAR_TODOS = "SELECT id_produto, nome_produto, descricao_produto, preco_produto, estoque_produto FROM \"Produtos\"";
    private static final String SQL_DELETAR_PRODUTO = "DELETE FROM \"Produtos\" WHERE id_produto = ?";
    private static final String SQL_ALTERAR_PRODUTO = "UPDATE \"Produtos\" SET nome_produto = ?, descricao_produto = ?, preco_produto = ?, estoque_produto = ? WHERE id_produto = ?";

    public List<Produto> buscarTodos() {
        List<Produto> produtos = new ArrayList<>();

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_BUSCAR_TODOS);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id_produto");
                String nome = rs.getString("nome_produto");
                String descricao = rs.getString("descricao_produto");
                double preco = rs.getDouble("preco_produto");
                int quantidade = rs.getInt("estoque_produto");

                Produto produto = new Produto(id, nome, descricao, preco, quantidade);
                produtos.add(produto);
            }
        } catch (SQLException | URISyntaxException e) {
            e.printStackTrace();
        }

        return produtos;
    }

    public List<Produto> listarProdutos() {
        List<Produto> listaProdutos = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT * FROM \"Produtos\"";
            ps = conexao.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Produto produto = new Produto(
                        rs.getInt("id_produto"),
                        rs.getString("nome_produto"),
                        rs.getDouble("preco_produto"),
                        rs.getInt("estoque_produto"),
                        rs.getString("descricao_produto"));
                listaProdutos.add(produto);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            closeResources(ps, rs);
        }

        return listaProdutos;
    }

    public void inserirProduto(Produto produto) {
        String SQL_INSERIR_PRODUTO = "INSERT INTO \"Produtos\" (nome_produto, descricao_produto, preco_produto, estoque_produto) VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_INSERIR_PRODUTO)) {

            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setDouble(3, produto.getPreco());
            stmt.setInt(4, produto.getQuantidade());

            stmt.executeUpdate();
        } catch (SQLException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void deletarProduto(int idProduto) {
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_DELETAR_PRODUTO)) {
            stmt.setInt(1, idProduto);
            stmt.executeUpdate();
        } catch (SQLException | URISyntaxException e) {
            e.printStackTrace();
            // Lidar com exceções adequadamente
        }
    }

    public void alterarProduto(Produto produto) {
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_ALTERAR_PRODUTO)) {
            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setDouble(3, produto.getPreco());
            stmt.setInt(4, produto.getQuantidade());
            stmt.setInt(5, produto.getId());
            stmt.executeUpdate();
        } catch (SQLException | URISyntaxException e) {
            e.printStackTrace();
            // Lidar com exceções adequadamente
        }
    }

    public Produto buscarPorId(int idProduto) {
        String SQL_BUSCAR_POR_ID = "SELECT id_produto, nome_produto, descricao_produto, preco_produto, estoque_produto FROM \"Produtos\" WHERE id_produto = ?";
        Produto produto = null;

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_BUSCAR_POR_ID)) {
            stmt.setInt(1, idProduto);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id_produto");
                    String nome = rs.getString("nome_produto");
                    String descricao = rs.getString("descricao_produto");
                    double preco = rs.getDouble("preco_produto");
                    int quantidade = rs.getInt("estoque_produto");

                    produto = new Produto(id, nome, descricao, preco, quantidade);
                }
            }
        } catch (SQLException | URISyntaxException e) {
            e.printStackTrace();
            // Lidar com exceções adequadamente
        }
        return produto;
    }

    public int buscarQuantidade(int id) {
        int quantidade = 0;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT estoque_produto FROM \"Produtos\" WHERE id_produto = ?";
            ps = conexao.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                quantidade = rs.getInt("estoque_produto");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            closeResources(ps, rs);
        }

        return quantidade;
    }

    public void updateQuantidade(int id, int quantidade) {
        PreparedStatement ps = null;

        try {
            String sql = "UPDATE \"Produtos\" SET estoque_produto = ? WHERE id_produto = ?";
            ps = conexao.prepareStatement(sql);
            ps.setInt(1, quantidade);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            closeResources(ps);
        }
    }

    // Adicione outros métodos conforme necessário

    private void closeResources(PreparedStatement ps) {
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void closeResources(PreparedStatement ps, ResultSet rs) {
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
