/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.unipar.fish.invest.domains;
import java.time.LocalDateTime;

/**
 *
 * @author matheus291107
 */


public class Usuario extends BaseEntity {

    private String nome;
    private String email;
    private String senha;
    private Double saldo;

    private Boolean aceitouTermos;
    private Integer tentativasLogin;
    private LocalDateTime bloqueadoAte;

    public Usuario() {
        this.saldo = 0.0;
        this.tentativasLogin = 0;
    }

    public Usuario(String nome,
            String email,
            String senha,
            Double saldo,
            Boolean aceitouTermos) {

        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.saldo = saldo;
        this.aceitouTermos = aceitouTermos;
        this.tentativasLogin = 0;
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

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public Boolean getAceitouTermos() {
        return aceitouTermos;
    }

    public void setAceitouTermos(Boolean aceitouTermos) {
        this.aceitouTermos = aceitouTermos;
    }

    public Integer getTentativasLogin() {
        return tentativasLogin;
    }

    public void setTentativasLogin(Integer tentativasLogin) {
        this.tentativasLogin = tentativasLogin;
    }

    public LocalDateTime getBloqueadoAte() {
        return bloqueadoAte;
    }

    public void setBloqueadoAte(LocalDateTime bloqueadoAte) {
        this.bloqueadoAte = bloqueadoAte;
    }
}