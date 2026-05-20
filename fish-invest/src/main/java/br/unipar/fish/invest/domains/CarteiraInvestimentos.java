/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.unipar.fish.invest.domains;

/**
 *
 * @author user
 */
public class CarteiraInvestimentos extends BaseEntity {
    private String cliente;
    private double tipoInvestimento;
    private String nomeEspecifico;
    private double valorTotalAcumulado; 
    private double valorRendimento;

    public CarteiraInvestimentos() {
        super();
    }
    
    public CarteiraInvestimentos(String cliente, double  tipoInvestimento, String nomeEspecifico, double valorTotalAcumulado, double valorRendimento,Integer id) {
        
        super(id);
        this.cliente = cliente;
        this.tipoInvestimento = tipoInvestimento;
        this.nomeEspecifico = nomeEspecifico;
        this.valorTotalAcumulado = valorTotalAcumulado;
        this.valorRendimento = valorRendimento;{
   }
        
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public double getTipoInvestimento() {
        return tipoInvestimento;
    }

    public void setTipoInvestimento(double tipoInvestimento) {
        this.tipoInvestimento = tipoInvestimento;
    }

    public String getNomeEspecifico() {
        return nomeEspecifico;
    }

    public void setNomeEspecifico(String nomeEspecifico) {
        this.nomeEspecifico = nomeEspecifico;
    }

    public double getValorTotalAcumulado() {
        return valorTotalAcumulado;
    }

    public void setValorTotalAcumulado(double valorTotalAcumulado) {
        this.valorTotalAcumulado = valorTotalAcumulado;
    }

    public double getValorRendimento() {
        return valorRendimento;
    }

    public void setValorRendimento(double valorRendimento) {
        this.valorRendimento = valorRendimento;
    }

    @Override
    public String toString() {
        return "carteira_investimentos{" + "cliente=" + cliente + ", tipoInvestimento=" + tipoInvestimento + ", nomeEspecifico=" + nomeEspecifico + ", valorTotalAcumulado=" + valorTotalAcumulado + ", valorRendimento=" + valorRendimento + '}';
    }
    
}     

