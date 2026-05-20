/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.unipar.fish.invest.domains;

/**
 *
 * @author user
 */
public class Cliente extends BaseEntity {
    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private String codigoVerificacao;

    private PerfilInvestidor perfilInvestidor;
    private SegurancaAcesso segurancaAcesso;

    public Cliente() {
        super();
    }

    public Cliente(String nome, String email, String senha, String telefone, String codigoVerificacao, PerfilInvestidor perfilInvestidor, SegurancaAcesso segurancaAcesso, Integer id) {
        super(id);
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
        this.codigoVerificacao = codigoVerificacao;
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

    public String getCodigoVerificacao() {
        return codigoVerificacao;
    }

    public void setCodigoVerificacao(String codigoVerificacao) {
        this.codigoVerificacao = codigoVerificacao;
        
        
    }

    @Override
    public String toString() {
        return "Cliente{" + "nome=" + nome + ", email=" + email + ", senha=" + senha + ", telefone=" + telefone + ", codigoVerificacao=" + codigoVerificacao + ", perfilInvestidor=" + perfilInvestidor + ", segurancaAcesso=" + segurancaAcesso + '}';
    }
    
    
}
