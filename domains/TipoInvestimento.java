/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.unipar.fish.invest.domains;

/**
 *
 * @author user
 */

public class TipoInvestimento extends BaseEntity {

    private String nomeTipo;

    public TipoInvestimento() {
    }

    public TipoInvestimento(Integer id, String nomeTipo) {
        super(id);
        this.nomeTipo = nomeTipo;
    }

    public String getNomeTipo() {
        return nomeTipo;
    }

    public void setNomeTipo(String nomeTipo) {
        this.nomeTipo = nomeTipo;
    }

    @Override
    public String toString() {
        return "TipoInvestimento{" + "nomeTipo=" + nomeTipo + '}';
    }
    
   
    
}
