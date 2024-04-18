package com.tads.webprojeto.aplicacao;

import java.util.ArrayList;

public class Carrinho {
    ArrayList<Produto> produtos;

    public Carrinho(ArrayList<Produto> produtos) {
        super();
        this.produtos = produtos;
    }

    public ArrayList<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(ArrayList<Produto> produtos) {
        this.produtos = produtos;
    }

    public Produto getProduto(int id) {
        for (Produto p : produtos) {
            if (p.getId() == id) {
                return p;
            }
        }

        return null;
    }

    public void removeProduto(int id) {
        Produto p = getProduto(id);
        produtos.remove(p);
    }

    public void addProduto(Produto p) {
        produtos.add(p);
    }
}