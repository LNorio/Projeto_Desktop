/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.Produto;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author bayma
 */
public class ControleProduto {

    private Connection connection = null;
    private PreparedStatement pstdados = null;
    private Statement stdados = null;
    private ResultSet rsdados = null;

    public static final String pdfEmEstoque
            = System.getProperty("user.dir") + "/src/relatorio/ProdutoEmEstoque.pdf";
    public static final String pdfEmFalta
            = System.getProperty("user.dir") + "/src/relatorio/ProdutoEmFalta.pdf";
    public static final String pdfPorValidade
            = System.getProperty("user.dir") + "/src/relatorio/ProdutosPorValidade.pdf";
    public static final String relatorioEmEstoque
            = System.getProperty("user.dir") + "/src/relatorio/ProdutoEmEstoque.jasper";
    public static final String relatorioEmFalta
            = System.getProperty("user.dir") + "/src/relatorio/ProdutoEmFalta.jasper";
    public static final String relatorioPorValidade
            = System.getProperty("user.dir") + "/src/relatorio/ProdutosPorValidade.jasper";

    public ControleProduto() {
        acessaBD();
    }

    public void acessaBD() {
        String usuario = "leonardo";
        String senha = "leonardo";
        try {
            Class.forName("org.postgresql.Driver");
            String urlconexao = "jdbc:postgresql://localhost/ProjetoDesk";
            connection = DriverManager.getConnection(urlconexao, usuario, senha);
            //conn.setAutoCommit(false);
            //JOptionPane.showMessageDialog(this, "Deu certo");

        } catch (ClassNotFoundException ex) {
            System.out.println("Erro " + ex);
            JOptionPane.showMessageDialog(null, "Falha em se conectar ao postgre");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao acessar o Banco");
        }
    }

    public void cadastrarProduto(String nome, int quantidade, double preco, String validade) throws ProdException {

        int resp = 0;
        try {
            String sql = "Insert into produto (nome, quantidade, preco, validade)" + "values(?,?,?,?)";

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
            throw new ProdException("Nome ja existente");
        }

        if (resp > 0) {
            JOptionPane.showMessageDialog(null, "cadastro com sucesso");
        } else {
            JOptionPane.showMessageDialog(null, "Erro no cadastro");
        }
    }

    public void venderProduto(String nome, int quantidade) {

        Produto p = buscarProduto(nome);
        if (p == null) {
            JOptionPane.showMessageDialog(null, " Produto nao encontrado");
        } else {
            try {
                p = calcularQuantidade(quantidade, p);
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

    public Produto calcularQuantidade(int quantidade, Produto p) {

        if (p.getQuantidade() < quantidade || quantidade == 0) { // quantidade menor ou igual a 0
            return null;
        } else {
            p.setQuantidade(p.getQuantidade() - quantidade);
            return p;
        }
    }

    public void alterarProduto(String nome, int quantidade, Double valor, String validade) throws Exception {

        String sql = "UPDATE produto SET nome=?, quantidade=?, preco=?, validade=?  WHERE nome = '" + nome + "'";
        try {
            pstdados = connection.prepareStatement(sql);
            pstdados.setString(1, nome);
            if (quantidade <= 0 || valor <= 0) {
                throw new Exception();
            } else {
                pstdados.setInt(2, quantidade);
                pstdados.setDouble(3, valor);
            }
            pstdados.setString(4, validade);
            pstdados.executeUpdate();
            JOptionPane.showMessageDialog(null, "Produto alterado com sucesso", "Mensagem", JOptionPane.PLAIN_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro =" + e);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro ao alterar produto", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public Produto buscarProduto(String nome) {

        String sql = "Select * from produto where nome = '" + nome + "'";
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

    public void excluirProduto(String nome) {

        try {
            Produto p = buscarProduto(nome);

            if (p == null) {
                JOptionPane.showMessageDialog(null, "Produto nao encontrado");
            } else {
                String sql = "Delete from produto where nome = '" + p.getNome() + "'";
                try {
                    pstdados = connection.prepareStatement(sql);
                    int resp = pstdados.executeUpdate();
                    if (resp == 0) {
                        JOptionPane.showMessageDialog(null, "Erro ao excluir produto");
                    } else {
                        JOptionPane.showMessageDialog(null, "Produto deletado com sucesso");
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Erro na query" + ex);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir produto");
        }

    }

    public void sair() {
        try {
            connection.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao fechar conexao");
        }
    }

    public void relatorioEmEstoque() {
        JasperPrint impressao;
        acessaBD();
        try {
            impressao = JasperFillManager.fillReport(
                    relatorioEmEstoque,
                    null,
                    connection);

            JasperViewer.viewReport(impressao);
            JasperExportManager.exportReportToPdfFile(impressao, pdfEmEstoque);
        } catch (JRException ex) {
            System.err.println("Nao foi possivel gerar o relatorio\n\n");
            ex.printStackTrace();
        }
    }

    public void relatorioEmFalta() {
        JasperPrint impressao;
        acessaBD();
        try {
            impressao = JasperFillManager.fillReport(
                    relatorioEmFalta,
                    null,
                    connection);

            JasperViewer.viewReport(impressao);
            JasperExportManager.exportReportToPdfFile(impressao, pdfEmFalta);
        } catch (JRException ex) {
            System.err.println("Nao foi possivel gerar o relatorio\n\n");
            ex.printStackTrace();
        }
    }

    public void relatorioPorValidade() {
        JasperPrint impressao;
        acessaBD();
        try {
            impressao = JasperFillManager.fillReport(
                    relatorioPorValidade,
                    null,
                    connection);

            JasperViewer.viewReport(impressao);
            JasperExportManager.exportReportToPdfFile(impressao, pdfPorValidade);
        } catch (JRException ex) {
            System.err.println("Nao foi possivel gerar o relatorio\n\n");
            ex.printStackTrace();
        }
    }
}
