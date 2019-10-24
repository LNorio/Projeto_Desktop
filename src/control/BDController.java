/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author Admin
 */
public class BDController {
    
    private Connection connection = null;
    
    public Connection acessaBD() {
        String usuario = "leonardo";
        String senha = "leonardo";
        try {
            Class.forName("org.postgresql.Driver");
            String urlconexao = "jdbc:postgresql://localhost/ProjetoDesk";
            connection = DriverManager.getConnection(urlconexao, usuario, senha);
            //conn.setAutoCommit(false);
            //JOptionPane.showMessageDialog(this, "Deu certo");
            return connection;

        } catch (ClassNotFoundException ex) {
            System.out.println("Erro " + ex);
            JOptionPane.showMessageDialog(null, "Falha em se conectar ao postgre");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao acessar o Banco");
        }
        return null;
    }
    
    public void sair() {
        try {
            if(connection != null){
                connection.close();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao fechar conexao");
        }
    }
    
}
