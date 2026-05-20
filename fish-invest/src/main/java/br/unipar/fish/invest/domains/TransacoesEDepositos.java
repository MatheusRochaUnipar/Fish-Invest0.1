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
public class TransacoesEDepositos extends BaseEntity {

   
    private CarteiraInvestimentos carteira;
    private double  valorTransacao;
    private String  tipoOperacao;
    private String  metodoPagamento;
    private LocalDate   dataTransacao;
    private String  statusOperacao;

  
    public TransacoesEDepositos() {
        super();
    }

    public TransacoesEDepositos(CarteiraInvestimentos carteira, double valorTransacao, String tipoOperacao, String metodoPagamento, LocalDate dataTransacao, String statusOperacao, Integer id) {
        super(id);
        this.carteira = carteira;
        this.valorTransacao = valorTransacao;
        this.tipoOperacao = tipoOperacao;
        this.metodoPagamento = metodoPagamento;
        this.dataTransacao = dataTransacao;
        this.statusOperacao = statusOperacao;
    }

    public CarteiraInvestimentos getCarteira() {
        return carteira;
    }

    public void setCarteira(CarteiraInvestimentos carteira) {
        this.carteira = carteira;
    }

    public double getValorTransacao() {
        return valorTransacao;
    }

    public void setValorTransacao(double valorTransacao) {
        this.valorTransacao = valorTransacao;
    }

    public String getTipoOperacao() {
        return tipoOperacao;
    }

    public void setTipoOperacao(String tipoOperacao) {
        this.tipoOperacao = tipoOperacao;
    }

    public String getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(String metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public LocalDate getDataTransacao() {
        return dataTransacao;
    }

    public void setDataTransacao(LocalDate dataTransacao) {
        this.dataTransacao = dataTransacao;
    }

    public String getStatusOperacao() {
        return statusOperacao;
    }

    public void setStatusOperacao(String statusOperacao) {
        this.statusOperacao = statusOperacao;
    }

    @Override
    public String toString() {
        return "TransacoesEDepositos{" + "carteira=" + carteira + ", valorTransacao=" + valorTransacao + ", tipoOperacao=" + tipoOperacao + ", metodoPagamento=" + metodoPagamento + ", dataTransacao=" + dataTransacao + ", statusOperacao=" + statusOperacao + '}';
    }
    
}

    