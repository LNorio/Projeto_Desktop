/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import model.Produto;

/**
 *
 * @author bayma
 */
public class ControleProduto {

    
    private PreparedStatement pstdados = null;
    private Statement stdados = null;
    private ResultSet rsdados = null;
    private BDController bd = null;
    private Connection connection = null;
    
    public ControleProduto() {
        bd = new BDController();
        connection = bd.acessaBD();
    }

    public int cadastrarProduto(String nome, int quantidade, double preco, String validade) throws ProdException {

        int resp = 0;
        Produto p = buscarProduto(nome);
        if(p == null){
            try {
                String sql = "Insert into produto ( nome, quantidade, preco, validade)" + "values(?,?,?,?)";

                pstdados = connection.prepareStatement(sql);
                pstdados.setString(1, nome);
                if (quantidade <= 0) { 
                    throw new ProdException("Quantidade menor igual a 0");
                }else if(preco <= 0){
                    throw new ProdException("Preco menor igual a 0");
                }else {
                    pstdados.setInt(2, quantidade);
                    pstdados.setDouble(3, preco);
                }
                pstdados.setString(4, validade);
                resp = pstdados.executeUpdate();
            } catch (SQLException ex) {
                throw new ProdException("Erro no banco");
            }
        }
        else{
            JOptionPane.showMessageDialog(null, "Nome ja existe", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        return resp;
    }

    public void venderProduto(String nome, int quantidade) {

        Produto p = buscarProduto(nome);
        ValidacaoController vc = new ValidacaoController();
        if (p == null) {
            JOptionPane.showMessageDialog(null, " Produto nao encontrado");
        } else {
            try {
                p = vc.calcularQuantidade(quantidade, p);
                quantidade = p.getQuantidade();
                if (p != null) {
                    String sql = "UPDATE produto SET quantidade=?  WHERE nome = '" + nome + "'";
                    pstdados = connection.prepareStatement(sql);
                    pstdados.setInt(1, quantidade);
                    pstdados.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Produto vendido com sucesso", "Mensagem", JOptionPane.PLAIN_MESSAGE);
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro ao vender produto", "Erro", JOptionPane.ERROR_MESSAGE);
                System.out.println("" + ex);
            }
        }
    }

    public Produto buscarProduto(String nome) {

        String sql = "Select * from produto where nome = '" + nome + "';";
        try {
            stdados = connection.createStatement();
            rsdados = stdados.executeQuery(sql);
            while (rsdados.next()) {
                if (rsdados.getString("nome").equals(nome)) {
                    Produto p = new Produto(rsdados.getString("nome"), rsdados.getInt("quantidade"), rsdados.getDouble("preco"), rsdados.getString("validade"));
                    return p;
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na query " + ex);
        }
        return null;

    }

    public int excluirProduto(String nome) {
        
        int resp = 0;
        try {
            Produto p = buscarProduto(nome);

            if (p == null) {
                JOptionPane.showMessageDialog(null, "Produto nao encontrado");
            } else {
                String sql = "Delete from produto where nome = '" + p.getNome() + "'";
                try {
                    pstdados = connection.prepareStatement(sql);
                    resp = pstdados.executeUpdate();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Erro na query" + ex);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro no banco");
        }
        return resp;
    }

}
