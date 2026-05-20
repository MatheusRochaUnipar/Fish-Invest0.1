/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.unipar.fish.invest.domains;

import java.time.LocalDate;

/**
 *
 * @author user
 */
public class SegurancaAcesso extends BaseEntity{
    private String pinAcesso;
    private Boolean biometriaAtiva;
    private LocalDate dataCadastro;

    public SegurancaAcesso() {
        super();
    }

    public SegurancaAcesso(String pinAcesso, Boolean biometriaAtiva, LocalDate dataCadastro, Integer id) {
        super(id);
        this.pinAcesso = pinAcesso;
        this.biometriaAtiva = biometriaAtiva;
        this.dataCadastro = dataCadastro;
    }

    public String getPinAcesso() {
        return pinAcesso;
    }

    public void setPinAcesso(String pinAcesso) {
        this.pinAcesso = pinAcesso;
    }

    public Boolean getBiometriaAtiva() {
        return biometriaAtiva;
    }

    public void setBiometriaAtiva(Boolean biometriaAtiva) {
        this.biometriaAtiva = biometriaAtiva;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    @Override
    public String toString() {
        return "SegurancaAcesso{" + "pinAcesso=" + pinAcesso + ", biometriaAtiva=" + biometriaAtiva + ", dataCadastro=" + dataCadastro + '}';
    }
    
    
    
}
