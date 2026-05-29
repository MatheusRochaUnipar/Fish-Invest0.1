/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package br.unipar.fish.invest;

import br.unipar.fish.invest.domains.CarteiraInvestimentos;
import br.unipar.fish.invest.domains.Cliente;
import br.unipar.fish.invest.domains.SegurancaAcesso;
import br.unipar.fish.invest.domains.TipoInvestimento;
import br.unipar.fish.invest.domains.TransacoesEDepositos;
import br.unipar.fish.invest.repositories.CarteiraInvestimentosRepository;
import br.unipar.fish.invest.repositories.ClienteRepository;
import br.unipar.fish.invest.repositories.TipoInvestimentoRepository;
import br.unipar.fish.invest.repositories.TransacoesEDepositosRepository;
import br.unipar.fish.invest.services.ClienteService;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author matheus291107
 */


public class FishInvest {
    public static void main(String[] args) {

        ClienteService clienteService = new ClienteService();

        // ── MENU PRINCIPAL ────────────────────────────────────────────────────
        String[] opcoes = {"Efetuar Login", "Criar Conta", "Sair"};

        while (true) {
            int escolha = JOptionPane.showOptionDialog(
                    null,
                    "Bem-vindo ao FishInvest!\nEscolha uma opção:",
                    "FishInvest – Menu Principal",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    opcoes,
                    opcoes[0]
            );

            if (escolha == 0) {
                efetuarLogin(clienteService);
            } else if (escolha == 1) {
                criarConta(clienteService);
            } else {
                // Fechou a janela ou clicou Sair
                JOptionPane.showMessageDialog(
                        null,
                        "Sistema encerrado. Até logo!",
                        "FishInvest",
                        JOptionPane.INFORMATION_MESSAGE
                );
                System.exit(0);
            }
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // EFETUAR LOGIN
    // ═══════════════════════════════════════════════════════════════════════════
    private static void efetuarLogin(ClienteService clienteService) {

        // ── Leitura do e-mail ─────────────────────────────────────────────────
        String email = JOptionPane.showInputDialog(
                null,
                "Digite seu e-mail:",
                "FishInvest – Login",
                JOptionPane.PLAIN_MESSAGE
        );

        // Usuário cancelou ou fechou
        if (email == null) {
            return;
        }

        // ── Leitura da senha ──────────────────────────────────────────────────
        String senha = JOptionPane.showInputDialog(
                null,
                "Digite sua senha:",
                "FishInvest – Login",
                JOptionPane.PLAIN_MESSAGE
        );

        if (senha == null) {
            return;
        }

        // ── Chama o serviço e exibe resultado ─────────────────────────────────
        try {
            Cliente logado = clienteService.efetuarLogin(email.trim(), senha);

            JOptionPane.showMessageDialog(
                    null,
                    "✅ Login realizado com sucesso!\n\n"
                    + "Bem-vindo(a), " + logado.getNome() + "!",
                    "FishInvest – Login",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    null,
                    "❌ Falha no login:\n\n" + e.getMessage(),
                    "FishInvest – Login",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // CRIAR CONTA
    // ═══════════════════════════════════════════════════════════════════════════
    private static void criarConta(ClienteService clienteService) {

        // ── Nome ──────────────────────────────────────────────────────────────
        String nome = JOptionPane.showInputDialog(
                null,
                "Digite seu nome completo:",
                "FishInvest – Criar Conta",
                JOptionPane.PLAIN_MESSAGE
        );
        if (nome == null) return;

        // ── E-mail ────────────────────────────────────────────────────────────
        String email = JOptionPane.showInputDialog(
                null,
                "Digite seu e-mail:",
                "FishInvest – Criar Conta",
                JOptionPane.PLAIN_MESSAGE
        );
        if (email == null) return;

        // ── Senha ─────────────────────────────────────────────────────────────
        String senha = JOptionPane.showInputDialog(
                null,
                "Digite sua senha (mínimo 8 caracteres):",
                "FishInvest – Criar Conta",
                JOptionPane.PLAIN_MESSAGE
        );
        if (senha == null) return;

        // ── Telefone ──────────────────────────────────────────────────────────
        String telefone = JOptionPane.showInputDialog(
                null,
                "Digite seu telefone:",
                "FishInvest – Criar Conta",
                JOptionPane.PLAIN_MESSAGE
        );
        if (telefone == null) return;

        // ── Aceite dos Termos ─────────────────────────────────────────────────
        int aceitou = JOptionPane.showConfirmDialog(
                null,
                "Você leu e aceita os Termos de Uso e Política de Privacidade\ndo FishInvest?",
                "FishInvest – Termos de Uso",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        boolean aceitouTermos = (aceitou == JOptionPane.YES_OPTION);

        // ── Monta o objeto Cliente ────────────────────────────────────────────
        Cliente novo = new Cliente();
        novo.setNome(nome.trim());
        novo.setEmail(email.trim());
        novo.setSenha(senha);
        novo.setTelefone(telefone.trim());

        // ── Chama o serviço e exibe resultado ─────────────────────────────────
        try {
            Cliente criado = clienteService.criarConta(novo, aceitouTermos);

            JOptionPane.showMessageDialog(
                    null,
                    "✅ Conta criada com sucesso!\n\n"
                    + "Nome  : " + criado.getNome() + "\n"
                    + "E-mail: " + criado.getEmail() + "\n"
                    + "ID    : " + criado.getId(),
                    "FishInvest – Criar Conta",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    null,
                    "❌ Erro ao criar conta:\n\n" + e.getMessage(),
                    "FishInvest – Criar Conta",
                    JOptionPane.ERROR_MESSAGE
            );
  
        }
    }}


        
        
