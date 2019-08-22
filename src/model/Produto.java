/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;

/**
 *
 * @author bayma
 */
public class Produto{
    
    
    int quantidade;
    double valor;
    String nome; 
    String mesValidade;

    public String getMesValidade() {
        return mesValidade;
    }

    public void setMesValidade(String mesValidade) {
        this.mesValidade = mesValidade;
    }

    public Produto(String nome, int quantidade, double valor, String mesValidade){
        this.nome = nome;
        this.quantidade = quantidade;
        this.valor = valor;
        this.mesValidade = mesValidade;
    }
    public Produto(){
        
    }
    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}

