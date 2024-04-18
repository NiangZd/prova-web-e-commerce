package com.tads.webprojeto.aplicacao;

//import com.tads.webprojeto.dominio.ProdutoDAO;

public class Produto {
    private int id;
    private String nome;
    private double preco;
    private int quantidade;
    private String descricao;

    public Produto() {

    }

    public Produto(int id) {
        this.id = id;
    }

    public Produto(int id, String nome, double preco, int quantidade, String descricao) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
        this.descricao = descricao;
    }

    public Produto(int id, String nome, String descricao, Double preco, int quantidade) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.quantidade = quantidade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /*
     * public Produto getProdutoPorId(int id) {
     * ProdutoDAO produtoDAO = new ProdutoDAO();
     * return produtoDAO.getProdutoPorId(id);
     * }
     */
}