/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.unipar.fish.invest.services;

import br.unipar.fish.invest.domains.Cliente;
import br.unipar.fish.invest.repositories.ClienteRepository;
import java.sql.SQLException;
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

    private static final int MAX_TENTATIVAS  = 3;
    private static final int MINUTOS_BLOQUEIO = 15;

    public ClienteService() {
        this.clienteRepository = new ClienteRepository();
    }

    // ── EFETUAR LOGIN ────────────────────────────────────────────────────────
    /**
     * RN-01: Só permite acesso se e-mail e senha coincidirem com o banco.
     * RN-02: Valida o formato "usuario@dominio.com".
     * RN-03: Bloqueia 15 min após 3 tentativas erradas consecutivas.
     * RN-06: Exige conexão ativa com a internet.
     */
    public Cliente efetuarLogin(String email, String senha) throws SQLException, Exception {

        // RN-06: verificar conexão com internet
        if (!isConectado()) {
            throw new Exception(
                "Sem conexão com a internet. Verifique sua rede e tente novamente.");
        }

        // RN-02: validar formato do e-mail
        if (!validarFormatoEmail(email)) {
            throw new Exception(
                "E-mail inválido. Utilize o formato usuario@dominio.com");
        }

        // RN-03: verificar se está bloqueado
        if (estaBlockeado(email)) {
            LocalDateTime desbloqueio = bloqueioLogin.get(email)
                    .plusMinutes(MINUTOS_BLOQUEIO);
            throw new Exception(
                "Acesso bloqueado por tentativas inválidas. Tente novamente após: "
                + desbloqueio);
        }

        // RN-01: buscar cliente pelo e-mail
        Cliente cliente = clienteRepository.findByEmail(email);

        // RN-01: verificar se e-mail existe e senha confere
        if (cliente == null || !cliente.getSenha().equals(senha)) {

            // RN-03: registrar tentativa falha
            int tentativas = tentativasLogin.getOrDefault(email, 0) + 1;
            tentativasLogin.put(email, tentativas);

            if (tentativas >= MAX_TENTATIVAS) {
                bloqueioLogin.put(email, LocalDateTime.now());
                tentativasLogin.put(email, 0);
                throw new Exception(
                    "Acesso bloqueado por " + MINUTOS_BLOQUEIO
                    + " minutos após " + MAX_TENTATIVAS
                    + " tentativas incorretas.");
            }

            throw new Exception(
                "E-mail ou senha inválidos. Tentativas restantes: "
                + (MAX_TENTATIVAS - tentativas));
        }

        // Login bem-sucedido: limpar controle de tentativas
        tentativasLogin.remove(email);
        bloqueioLogin.remove(email);

        return cliente;
    }

    // ── MÉTODOS AUXILIARES ───────────────────────────────────────────────────

    // RN-02: regex padrão usuario@dominio.com
    private boolean validarFormatoEmail(String email) {
        if (email == null || email.isBlank()) return false;
        return email.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$");
    }

    // RN-03: checa se o e-mail ainda está no período de bloqueio
    private boolean estaBlockeado(String email) {
        if (!bloqueioLogin.containsKey(email)) return false;

        LocalDateTime horarioBloqueio = bloqueioLogin.get(email);
        if (LocalDateTime.now().isAfter(horarioBloqueio.plusMinutes(MINUTOS_BLOQUEIO))) {
            bloqueioLogin.remove(email);
            tentativasLogin.remove(email);
            return false;
        }
        return true;
    }

    // RN-06: tenta resolver DNS do google para verificar internet
    private boolean isConectado() {
        try {
            java.net.InetAddress.getByName("google.com");
            return true;
        } catch (java.net.UnknownHostException e) {
            return false;
        }
    }
}