/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package br.unipar.fish.invest;

import br.unipar.fish.invest.domains.CarteiraInvestimentos;
import br.unipar.fish.invest.domains.Cliente;
import br.unipar.fish.invest.domains.TipoInvestimento;
import br.unipar.fish.invest.repositories.CarteiraInvestimentosRepository;
import br.unipar.fish.invest.repositories.TipoInvestimentoRepository;
import br.unipar.fish.invest.services.ClienteService;
import br.unipar.fish.invest.services.TransacoesEDepositosService;

import javax.swing.JOptionPane;
import java.util.ArrayList;

/**
 *
 * @author matheus291107
 */


public class FishInvest {

    // ── Serviços e repositórios globais ───────────────────────────────────────
    private static final ClienteService               clienteService    = new ClienteService();
    private static final TransacoesEDepositosService  aporteService     = new TransacoesEDepositosService();
    private static final CarteiraInvestimentosRepository carteiraRepo   = new CarteiraInvestimentosRepository();
    private static final TipoInvestimentoRepository   tipoRepo          = new TipoInvestimentoRepository();

    // ── Sessão: cliente logado na sessão atual ────────────────────────────────
    private static Cliente clienteLogado = null;

    // =========================================================================
    // MAIN – MENU PRINCIPAL
    // =========================================================================
    public static void main(String[] args) {

        String[] opcoesPublicas = {"Efetuar Login", "Criar Conta", "Sair"};

        while (true) {
            // Se ninguém está logado, mostra menu público
            if (clienteLogado == null) {

                int escolha = JOptionPane.showOptionDialog(
                        null,
                        "Bem-vindo ao FishInvest!\nEscolha uma opção:",
                        "FishInvest – Menu Principal",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        opcoesPublicas,
                        opcoesPublicas[0]
                );

                if (escolha == 0)      efetuarLogin();
                else if (escolha == 1) criarConta();
                else                   sair();

            } else {
                // Cliente logado → menu logado
                menuLogado();
            }
        }
    }

    // =========================================================================
    // MENU PÓS-LOGIN
    // =========================================================================
    private static void menuLogado() {

        String[] opcoes = {"Novo Aporte", "Depositar", "Sair da Conta"};

        int escolha = JOptionPane.showOptionDialog(
                null,
                "Olá, " + clienteLogado.getNome() + "!\nO que deseja fazer?",
                "FishInvest – Menu",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                opcoes,
                opcoes[0]
        );

        if (escolha == 0)      telaNovoAporte();
        else if (escolha == 1) telaDepositar();
        else {
            clienteLogado = null; // logout
            JOptionPane.showMessageDialog(null,
                    "Você saiu da conta. Até logo!",
                    "FishInvest", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // =========================================================================
    // TELA NOVO APORTE – escolha do tipo (mockup "Tela de aportes")
    // =========================================================================
    private static void telaNovoAporte() {

        String[] tipos = {"Renda Fixa", "Reserva de Emergência", "Ações", "Cancelar"};

        int escolha = JOptionPane.showOptionDialog(
                null,
                "Novo Aporte\nSelecione o tipo de investimento:",
                "FishInvest – Novo Aporte",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                tipos,
                tipos[0]
        );

        if (escolha == 0)      aporteRendaFixa();
        else if (escolha == 1) aporteReservaEmergencia();
        else if (escolha == 2) aporteAcoes();
        // escolha == 3 ou fechou → volta ao menu
    }

    // =========================================================================
    // APORTE – RENDA FIXA  (mockup "Renda Fixa")
    // =========================================================================
    private static void aporteRendaFixa() {

        // ── Valor ─────────────────────────────────────────────────────────────
        String valorStr = JOptionPane.showInputDialog(
                null,
                "Renda Fixa\n\nValor do aporte (ex: 1500,00):\nR$",
                "FishInvest – Renda Fixa",
                JOptionPane.PLAIN_MESSAGE
        );
        if (valorStr == null) return;

        // ── Tipo de investimento (CDB, LCI, LCA…) ────────────────────────────
        String[] subtipos = {"CDB", "LCI", "LCA", "Debentures"};
        int subEscolha = JOptionPane.showOptionDialog(
                null,
                "Renda Fixa\n\nDetalhes do investimento:\nTipo de Investimento:",
                "FishInvest – Renda Fixa",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                subtipos,
                subtipos[0]
        );
        if (subEscolha < 0) return;
        String subtipo = subtipos[subEscolha];

        // ── Prazo ─────────────────────────────────────────────────────────────
        String[] prazos = {"6 meses", "12 meses", "24 meses", "36 meses"};
        int prazoEscolha = JOptionPane.showOptionDialog(
                null,
                "Renda Fixa – " + subtipo + "\n\nPrazo:",
                "FishInvest – Renda Fixa",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                prazos,
                prazos[1]
        );
        if (prazoEscolha < 0) return;
        String prazo = prazos[prazoEscolha];

        // ── Rentabilidade esperada (informativo) ──────────────────────────────
        String rentabilidade = subtipo.equals("CDB") ? "100% do CDI"
                             : subtipo.equals("LCI") ? "95% do CDI"
                             : subtipo.equals("LCA") ? "92% do CDI"
                             : "IPCA + 6% a.a.";

        int confirma = JOptionPane.showConfirmDialog(
                null,
                "Renda Fixa – " + subtipo + "\n\n"
              + "Valor      : R$ " + valorStr + "\n"
              + "Prazo      : " + prazo + "\n"
              + "Rentabilidade esperada: " + rentabilidade + "\n\n"
              + "Confirmar aporte?",
                "FishInvest – Confirmar Aporte",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        if (confirma != JOptionPane.YES_OPTION) return;

        // ── Busca ou cria carteira de Renda Fixa do cliente ───────────────────
        executarAporte("Renda Fixa – " + subtipo, valorStr);
    }

    // =========================================================================
    // APORTE – RESERVA DE EMERGÊNCIA  (mockup "Reserva de Emergência")
    // =========================================================================
    private static void aporteReservaEmergencia() {

        // ── Valor ─────────────────────────────────────────────────────────────
        String valorStr = JOptionPane.showInputDialog(
                null,
                "Reserva de Emergência\n\nObjetivo: 6 a 12 meses de despesas\n\n"
              + "Valor do aporte (ex: 500,00):\nR$",
                "FishInvest – Reserva de Emergência",
                JOptionPane.PLAIN_MESSAGE
        );
        if (valorStr == null) return;

        // ── Produto da reserva ────────────────────────────────────────────────
        String[] produtos = {"Conta de liquidez diária", "Tesouro Selic", "Fundos DI"};
        int prodEscolha = JOptionPane.showOptionDialog(
                null,
                "Reserva de Emergência\n\nEscolha o produto:",
                "FishInvest – Reserva de Emergência",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                produtos,
                produtos[0]
        );
        if (prodEscolha < 0) return;
        String produto = produtos[prodEscolha];

        int confirma = JOptionPane.showConfirmDialog(
                null,
                "Reserva de Emergência\n\n"
              + "Produto : " + produto + "\n"
              + "Valor   : R$ " + valorStr + "\n\n"
              + "Adicionar à reserva?",
                "FishInvest – Confirmar Aporte",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        if (confirma != JOptionPane.YES_OPTION) return;

        executarAporte("Reserva de Emergência – " + produto, valorStr);
    }

    // =========================================================================
    // APORTE – AÇÕES  (mockup "Ações")
    // =========================================================================
    private static void aporteAcoes() {

        // ── Valor ─────────────────────────────────────────────────────────────
        String valorStr = JOptionPane.showInputDialog(
                null,
                "Ações\n\nValor do aporte (ex: 200,00):\nR$",
                "FishInvest – Ações",
                JOptionPane.PLAIN_MESSAGE
        );
        if (valorStr == null) return;

        // ── Selecionar ação ───────────────────────────────────────────────────
        String[] acoes = {
            "PETR4  – Petrobras   – R$ 36,50",
            "VALE3  – Vale S.A.   – R$ 72,90",
            "ITUB4  – Itaú Unibanco – R$ 28,90"
        };
        int acaoEscolha = JOptionPane.showOptionDialog(
                null,
                "Ações\n\nSelecionar Ação:",
                "FishInvest – Ações",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                acoes,
                acoes[0]
        );
        if (acaoEscolha < 0) return;

        String[] tickers = {"PETR4", "VALE3", "ITUB4"};
        String ticker = tickers[acaoEscolha];

        int confirma = JOptionPane.showConfirmDialog(
                null,
                "Ações\n\n"
              + "Ação  : " + ticker + "\n"
              + "Valor : R$ " + valorStr + "\n\n"
              + "Confirmar aporte?",
                "FishInvest – Confirmar Aporte",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        if (confirma != JOptionPane.YES_OPTION) return;

        executarAporte("Ações – " + ticker, valorStr);
    }

    // =========================================================================
    // TELA DEPOSITAR  (mockup "Depositar")
    // =========================================================================
    private static void telaDepositar() {

        String valorStr = JOptionPane.showInputDialog(
                null,
                "Depositar\n\nValor do depósito (ex: 1000,00):\nR$",
                "FishInvest – Depositar",
                JOptionPane.PLAIN_MESSAGE
        );
        if (valorStr == null) return;

        // ── Método de depósito ────────────────────────────────────────────────
        String[] metodos = {"TED", "Boleto", "Cartão de Crédito", "PIX (QR Code)"};
        int metodoEscolha = JOptionPane.showOptionDialog(
                null,
                "Depositar\n\nMétodos de Depósito:",
                "FishInvest – Depositar",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                metodos,
                metodos[0]
        );
        if (metodoEscolha < 0) return;
        String metodo = metodos[metodoEscolha];

        // PIX: mostra chave
        if (metodo.equals("PIX (QR Code)")) {
            JOptionPane.showMessageDialog(null,
                    "Depositar via PIX\n\n"
                  + "Chave PIX: 123456789\n"
                  + "Banco: 341 | Ag: 0234 | CC: 56789\n\n"
                  + "Escaneie o QR Code no seu banco para completar.",
                    "FishInvest – PIX",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirma = JOptionPane.showConfirmDialog(
                null,
                "Depositar\n\n"
              + "Valor  : R$ " + valorStr + "\n"
              + "Método : " + metodo + "\n\n"
              + "Confirmar depósito?",
                "FishInvest – Confirmar Depósito",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        if (confirma != JOptionPane.YES_OPTION) return;

        // Depósito usa a mesma carteira principal do cliente
        executarAporte("Depósito – " + metodo, valorStr);
    }

    // =========================================================================
    // EXECUTOR DE APORTE – busca carteira e chama o service
    // =========================================================================
    private static void executarAporte(String nomeCarteira, String valorStr) {
        try {
            // Busca todas as carteiras do cliente e usa a primeira compatível,
            // ou usa a primeira disponível (simplificação sem filtro por nome).
            ArrayList<CarteiraInvestimentos> carteiras = carteiraRepo.listarTodos();

            CarteiraInvestimentos carteira = null;
            for (CarteiraInvestimentos c : carteiras) {
                if (c.getCliente() != null
                        && c.getCliente().getId().equals(clienteLogado.getId())) {
                    carteira = c;
                    break;
                }
            }

            if (carteira == null) {
                // Nenhuma carteira encontrada: orienta o usuário
                JOptionPane.showMessageDialog(null,
                        "❌ Nenhuma carteira encontrada para sua conta.\n\n"
                      + "Solicite a criação de uma carteira ao administrador.",
                        "FishInvest – Aporte",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Chama o service com o nome da operação e o valor digitado
            String comprovante = aporteService.realizarAporte(
                    clienteLogado, carteira, valorStr);

            JOptionPane.showMessageDialog(null,
                    comprovante,
                    "FishInvest – Comprovante",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "❌ Erro ao realizar aporte:\n\n" + e.getMessage(),
                    "FishInvest – Aporte",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // =========================================================================
    // EFETUAR LOGIN
    // =========================================================================
    private static void efetuarLogin() {

        String email = JOptionPane.showInputDialog(null,
                "Digite seu e-mail:", "FishInvest – Login",
                JOptionPane.PLAIN_MESSAGE);
        if (email == null) return;

        String senha = JOptionPane.showInputDialog(null,
                "Digite sua senha:", "FishInvest – Login",
                JOptionPane.PLAIN_MESSAGE);
        if (senha == null) return;

        try {
            clienteLogado = clienteService.efetuarLogin(email.trim(), senha);

            JOptionPane.showMessageDialog(null,
                    "✅ Login realizado com sucesso!\n\nBem-vindo(a), "
                    + clienteLogado.getNome() + "!",
                    "FishInvest – Login",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "❌ Falha no login:\n\n" + e.getMessage(),
                    "FishInvest – Login",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // =========================================================================
    // CRIAR CONTA
    // =========================================================================
    private static void criarConta() {

        String nome = JOptionPane.showInputDialog(null,
                "Digite seu nome completo:", "FishInvest – Criar Conta",
                JOptionPane.PLAIN_MESSAGE);
        if (nome == null) return;

        String email = JOptionPane.showInputDialog(null,
                "Digite seu e-mail:", "FishInvest – Criar Conta",
                JOptionPane.PLAIN_MESSAGE);
        if (email == null) return;

        String senha = JOptionPane.showInputDialog(null,
                "Digite sua senha (mínimo 8 caracteres):", "FishInvest – Criar Conta",
                JOptionPane.PLAIN_MESSAGE);
        if (senha == null) return;

        String telefone = JOptionPane.showInputDialog(null,
                "Digite seu telefone:", "FishInvest – Criar Conta",
                JOptionPane.PLAIN_MESSAGE);
        if (telefone == null) return;

        int aceitou = JOptionPane.showConfirmDialog(null,
                "Você leu e aceita os Termos de Uso e Política de Privacidade do FishInvest?",
                "FishInvest – Termos de Uso",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        Cliente novo = new Cliente();
        novo.setNome(nome.trim());
        novo.setEmail(email.trim());
        novo.setSenha(senha);
        novo.setTelefone(telefone.trim());

        try {
            Cliente criado = clienteService.criarConta(novo, aceitou == JOptionPane.YES_OPTION);

            JOptionPane.showMessageDialog(null,
                    "✅ Conta criada com sucesso!\n\n"
                    + "Nome  : " + criado.getNome() + "\n"
                    + "E-mail: " + criado.getEmail() + "\n"
                    + "ID    : " + criado.getId(),
                    "FishInvest – Criar Conta",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "❌ Erro ao criar conta:\n\n" + e.getMessage(),
                    "FishInvest – Criar Conta",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // =========================================================================
    // SAIR DO SISTEMA
    // =========================================================================
    private static void sair() {
        JOptionPane.showMessageDialog(null,
                "Sistema encerrado. Até logo!",
                "FishInvest", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }
}


        
        
