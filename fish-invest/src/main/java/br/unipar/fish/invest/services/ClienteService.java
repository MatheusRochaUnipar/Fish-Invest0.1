/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.unipar.fish.invest.services;

import br.unipar.fish.invest.domains.Cliente;
import br.unipar.fish.invest.domains.SegurancaAcesso;
import br.unipar.fish.invest.repositories.ClienteRepository;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author matheus291107
 */

public class ClienteService {
 
    private final ClienteRepository clienteRepository;
 
    // RN-03: controle de tentativas e bloqueios por e-mail (em memória)
    private static final Map<String, Integer>       tentativasLogin = new HashMap<>();
    private static final Map<String, LocalDateTime> bloqueioLogin   = new HashMap<>();
 
    private static final int MAX_TENTATIVAS   = 3;
    private static final int MINUTOS_BLOQUEIO = 15;
 
    public ClienteService() {
        this.clienteRepository = new ClienteRepository();
    }
 
    // =========================================================================
    // EFETUAR LOGIN
    // =========================================================================
    public Cliente efetuarLogin(String email, String senha) throws Exception {
 
        // RN-06: verificar conexão com internet
        if (!isConectado()) {
            throw new Exception(
                "Sem conexao com a internet. Verifique sua rede e tente novamente.");
        }
 
        // RN-02: validar formato do e-mail
        if (!validarFormatoEmail(email)) {
            throw new Exception(
                "E-mail invalido. Utilize o formato usuario@dominio.com");
        }
 
        // RN-03: verificar bloqueio
        if (estaBloqueado(email)) {
            LocalDateTime desbloqueio = bloqueioLogin.get(email)
                    .plusMinutes(MINUTOS_BLOQUEIO);
            throw new Exception(
                "Acesso bloqueado por tentativas invalidas. Tente novamente apos: "
                + desbloqueio);
        }
 
        // RN-01: buscar cliente pelo e-mail
        Cliente cliente = clienteRepository.findByEmail(email);
 
        if (cliente == null || !cliente.getSenha().equals(senha)) {
            int tentativas = tentativasLogin.getOrDefault(email, 0) + 1;
            tentativasLogin.put(email, tentativas);
 
            if (tentativas >= MAX_TENTATIVAS) {
                bloqueioLogin.put(email, LocalDateTime.now());
                tentativasLogin.put(email, 0);
                throw new Exception(
                    "Acesso bloqueado por " + MINUTOS_BLOQUEIO
                    + " minutos apos " + MAX_TENTATIVAS + " tentativas incorretas.");
            }
 
            throw new Exception(
                "E-mail ou senha invalidos. Tentativas restantes: "
                + (MAX_TENTATIVAS - tentativas));
        }
 
        tentativasLogin.remove(email);
        bloqueioLogin.remove(email);
        return cliente;
    }
 
    // =========================================================================
    // CRIAR CONTA  (agora pede CPF)
    // =========================================================================
    public Cliente criarConta(Cliente cliente,
                               boolean aceitouTermos) throws Exception {
 
        // RN-09: campos obrigatórios
        if (cliente.getNome() == null || cliente.getNome().isBlank()) {
            throw new Exception("O campo Nome e obrigatorio.");
        }
        if (cliente.getCpf() == null || cliente.getCpf().isBlank()) {
            throw new Exception("O campo CPF e obrigatorio.");
        }
        if (cliente.getEmail() == null || cliente.getEmail().isBlank()) {
            throw new Exception("O campo E-mail e obrigatorio.");
        }
        if (cliente.getSenha() == null || cliente.getSenha().isBlank()) {
            throw new Exception("O campo Senha e obrigatorio.");
        }
        if (cliente.getTelefone() == null || cliente.getTelefone().isBlank()) {
            throw new Exception("O campo Telefone e obrigatorio.");
        }
 
        // Validar formato e-mail
        if (!validarFormatoEmail(cliente.getEmail())) {
            throw new Exception(
                "E-mail invalido. Utilize o formato usuario@dominio.com");
        }
 
        // Validar CPF
        if (!validarCpf(cliente.getCpf())) {
            throw new Exception(
                "CPF invalido. Utilize o formato 000.000.000-00 com digitos validos.");
        }
 
        // RN-07: aceite dos termos
        if (!aceitouTermos) {
            throw new Exception(
                "E obrigatorio aceitar os Termos de Uso para criar uma conta.");
        }
 
        // RN-08: complexidade de senha
        if (!validarSenha(cliente.getSenha())) {
            throw new Exception(
                "A senha deve conter no minimo 8 caracteres, "
              + "incluindo pelo menos uma letra e um numero.");
        }
 
        // RN-06: e-mail único
        try {
            if (clienteRepository.existsByEmail(cliente.getEmail())) {
                throw new Exception(
                    "Ja existe uma conta cadastrada com o e-mail: "
                    + cliente.getEmail());
            }
 
            // CPF único
            if (clienteRepository.existsByCpf(cliente.getCpf())) {
                throw new Exception(
                    "Ja existe uma conta cadastrada com o CPF: "
                    + cliente.getCpf());
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao verificar dados no banco: " + e.getMessage());
        }
 
        // Definir data de cadastro
        cliente.setDataCadastro(LocalDate.now());
 
        // SegurancaAcesso padrão
        if (cliente.getSegurancaAcesso() == null) {
            SegurancaAcesso seguranca = new SegurancaAcesso();
            seguranca.setPinAcesso(null);
            seguranca.setBiometriaAtiva(false);
            cliente.setSegurancaAcesso(seguranca);
        }
 
        try {
            cliente = clienteRepository.inserir(cliente);
        } catch (SQLException e) {
            throw new Exception("Erro ao salvar cliente no banco: " + e.getMessage());
        }
 
        return cliente;
    }
 
    // =========================================================================
    // AUXILIARES
    // =========================================================================
 
    private boolean validarFormatoEmail(String email) {
        if (email == null || email.isBlank()) return false;
        return email.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$");
    }
 
    private boolean validarSenha(String senha) {
        if (senha == null || senha.length() < 8) return false;
        return senha.matches(".*[a-zA-Z].*") && senha.matches(".*[0-9].*");
    }
 
    /**
     * Valida CPF no formato 000.000.000-00 e verifica os dígitos verificadores.
     */
    private boolean validarCpf(String cpf) {
        if (cpf == null) return false;
 
        // Remove máscara
        String numeros = cpf.replaceAll("[^0-9]", "");
        if (numeros.length() != 11) return false;
 
        // Rejeita sequências iguais (111.111.111-11 etc.)
        if (numeros.matches("(\\d)\\1{10}")) return false;
 
        // Dígito verificador 1
        int soma = 0;
        for (int i = 0; i < 9; i++) soma += (numeros.charAt(i) - '0') * (10 - i);
        int dig1 = 11 - (soma % 11);
        if (dig1 >= 10) dig1 = 0;
 
        // Dígito verificador 2
        soma = 0;
        for (int i = 0; i < 10; i++) soma += (numeros.charAt(i) - '0') * (11 - i);
        int dig2 = 11 - (soma % 11);
        if (dig2 >= 10) dig2 = 0;
 
        return dig1 == (numeros.charAt(9)  - '0')
            && dig2 == (numeros.charAt(10) - '0');
    }
 
    private boolean estaBloqueado(String email) {
        if (!bloqueioLogin.containsKey(email)) return false;
        if (LocalDateTime.now().isAfter(
                bloqueioLogin.get(email).plusMinutes(MINUTOS_BLOQUEIO))) {
            bloqueioLogin.remove(email);
            tentativasLogin.remove(email);
            return false;
        }
        return true;
    }
 
    private boolean isConectado() {
        try {
            java.net.InetAddress.getByName("google.com");
            return true;
        } catch (java.net.UnknownHostException e) {
            return false;
        }
    }
}