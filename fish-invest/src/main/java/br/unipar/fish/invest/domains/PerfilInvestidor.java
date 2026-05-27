/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.unipar.fish.invest.domains;

/**
 *
 * @author user
 */

public class PerfilInvestidor extends BaseEntity {

    private String nomePerfil;
    private String descricaoPerfil;

    public PerfilInvestidor() {

    }

    public PerfilInvestidor(Integer id, String nomePerfil, String descricaoPerfil) {
        super(id);
        this.nomePerfil = nomePerfil;
        this.descricaoPerfil = descricaoPerfil;
    }

    public String getNomePerfil() {
        return nomePerfil;
    }

    public void setNomePerfil(String nomePerfil) {
        this.nomePerfil = nomePerfil;
    }

    public String getDescricaoPerfil() {
        return descricaoPerfil;
    }

    public void setDescricaoPerfil(String descricaoPerfil) {
        this.descricaoPerfil = descricaoPerfil;
    }

    @Override
    public String toString() {
        return "PerfilInvestidor{" + "nomePerfil=" + nomePerfil + ", descricaoPerfil=" + descricaoPerfil + '}';
    }

    
}
