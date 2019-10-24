/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.sql.Connection;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Admin
 */
public class RelatorioController {
    
    private BDController bd = null;
    
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

    public RelatorioController() {
        this.bd = new BDController();
    }
            
    public void relatorioEmEstoque() {
        JasperPrint impressao;
        Connection connection = bd.acessaBD();
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
        Connection connection = bd.acessaBD();
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
        Connection connection = bd.acessaBD();
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
