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
public class Cliente extends BaseEntity {
    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private LocalDate dataCadastro;

    private PerfilInvestidor perfilInvestidor;
    private SegurancaAcesso segurancaAcesso;

    public Cliente() {
    }

    public Cliente(String nome, String email, String senha, String telefone, LocalDate dataCadastro, PerfilInvestidor perfilInvestidor, SegurancaAcesso segurancaAcesso, Integer id) {
        super(id);
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
        this.dataCadastro = dataCadastro;
        this.perfilInvestidor = perfilInvestidor;
        this.segurancaAcesso = segurancaAcesso;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public PerfilInvestidor getPerfilInvestidor() {
        return perfilInvestidor;
    }

    public void setPerfilInvestidor(PerfilInvestidor perfilInvestidor) {
        this.perfilInvestidor = perfilInvestidor;
    }

    public SegurancaAcesso getSegurancaAcesso() {
        return segurancaAcesso;
    }

    public void setSegurancaAcesso(SegurancaAcesso segurancaAcesso) {
        this.segurancaAcesso = segurancaAcesso;
    }

    @Override
    public String toString() {
        return "Cliente{" + "nome=" + nome + ", email=" + email + ", senha=" + senha + ", telefone=" + telefone + ", dataCadastro=" + dataCadastro + ", perfilInvestidor=" + perfilInvestidor + ", segurancaAcesso=" + segurancaAcesso + '}';
    }

    
    
}

    

