/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import javax.swing.JOptionPane;
import model.Produto;

/**
 *
 * @author Admin
 */
public class ValidacaoController {
    
    public boolean verificarCampos(String nome, String quantidade, String valor) {
        if (nome.equals("") || quantidade.equals("") || valor.equals("")) {
            JOptionPane.showMessageDialog(null, "Não pode ter campos vazios", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    
        public Produto calcularQuantidade(int quantidade, Produto p) {

        if (p.getQuantidade() < quantidade || quantidade == 0) { // quantidade menor ou igual a 0
            return null;
        } else {
            p.setQuantidade(p.getQuantidade() - quantidade);
            return p;
        }
    }
}
