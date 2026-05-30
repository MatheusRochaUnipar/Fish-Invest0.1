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
 
    private static final ClienteService clienteService = new ClienteService();
    private static final TransacoesEDepositosService aporteService = new TransacoesEDepositosService();
    private static final CarteiraInvestimentosRepository carteiraRepo  = new CarteiraInvestimentosRepository();
 
    // Cliente autenticado na sessão atual
    private static Cliente clienteLogado = null;
 
    // =========================================================================
    // MAIN
    // =========================================================================
    public static void main(String[] args) {
 
        while (true) {
            if (clienteLogado == null) {
                menuPublico();
            } else {
                menuLogado();
            }
        }
    }
 
    // =========================================================================
    // MENU PÚBLICO (sem login)
    // =========================================================================
    private static void menuPublico() {
        String[] opcoes = {"Efetuar Login", "Criar Conta", "Sair"};
        int escolha = JOptionPane.showOptionDialog(null,
                "Bem-vindo ao FishInvest!\nEscolha uma opcao:",
                "FishInvest – Menu Principal",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, opcoes, opcoes[0]);
 
        if      (escolha == 0) efetuarLogin();
        else if (escolha == 1) criarConta();
        else                   sair();
    }
 
    // =========================================================================
    // MENU PÓS-LOGIN
    // =========================================================================
    private static void menuLogado() {
        String[] opcoes = {"Novo Aporte", "Depositar", "Sair da Conta"};
        int escolha = JOptionPane.showOptionDialog(null,
                "Ola, " + clienteLogado.getNome() + "!\nO que deseja fazer?",
                "FishInvest – Menu",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, opcoes, opcoes[0]);
 
        if      (escolha == 0) telaNovoAporte();
        else if (escolha == 1) telaDepositar();
        else {
            clienteLogado = null;
            JOptionPane.showMessageDialog(null, "Voce saiu da conta. Ate logo!",
                    "FishInvest", JOptionPane.INFORMATION_MESSAGE);
        }
    }
 
    // =========================================================================
    // TELA NOVO APORTE – escolha do tipo
    // =========================================================================
    private static void telaNovoAporte() {
        String[] tipos = {"Renda Fixa", "Reserva de Emergencia", "Acoes", "Cancelar"};
        int escolha = JOptionPane.showOptionDialog(null,
                "Novo Aporte\nSelecione o tipo de investimento:",
                "FishInvest – Novo Aporte",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, tipos, tipos[0]);
 
        if      (escolha == 0) aporteRendaFixa();
        else if (escolha == 1) aporteReservaEmergencia();
        else if (escolha == 2) aporteAcoes();
        // escolha == 3 ou fechou → volta
    }
 
    // =========================================================================
    // APORTE – RENDA FIXA
    // =========================================================================
    private static void aporteRendaFixa() {
 
        String valorStr = JOptionPane.showInputDialog(null,
                "Renda Fixa\n\nValor do aporte (ex: 1500,00):\nR$",
                "FishInvest – Renda Fixa", JOptionPane.PLAIN_MESSAGE);
        if (valorStr == null) return;
 
        String[] subtipos = {"CDB", "LCI", "LCA", "Debentures"};
        int subIdx = JOptionPane.showOptionDialog(null,
                "Renda Fixa\n\nTipo de Investimento:",
                "FishInvest – Renda Fixa",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, subtipos, subtipos[0]);
        if (subIdx < 0) return;
 
        String[] prazos = {"6 meses", "12 meses", "24 meses", "36 meses"};
        int prazoIdx = JOptionPane.showOptionDialog(null,
                "Renda Fixa – " + subtipos[subIdx] + "\n\nPrazo:",
                "FishInvest – Renda Fixa",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, prazos, prazos[1]);
        if (prazoIdx < 0) return;
 
        String rentabilidade = switch (subtipos[subIdx]) {
            case "CDB"        -> "100% do CDI";
            case "LCI"        -> "95% do CDI";
            case "LCA"        -> "92% do CDI";
            default           -> "IPCA + 6% a.a.";
        };
 
        int confirma = JOptionPane.showConfirmDialog(null,
                "Renda Fixa – " + subtipos[subIdx] + "\n\n"
              + "Valor             : R$ " + valorStr + "\n"
              + "Prazo             : " + prazos[prazoIdx] + "\n"
              + "Rentabilidade     : " + rentabilidade + "\n\n"
              + "Confirmar aporte?",
                "FishInvest – Confirmar Aporte",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (confirma != JOptionPane.YES_OPTION) return;
 
        executarAporte(valorStr, "APORTE", "SALDO");
    }
 
    // =========================================================================
    // APORTE – RESERVA DE EMERGÊNCIA
    // =========================================================================
    private static void aporteReservaEmergencia() {
 
        String valorStr = JOptionPane.showInputDialog(null,
                "Reserva de Emergencia\n\nObjetivo: 6 a 12 meses de despesas\n\n"
              + "Valor do aporte (ex: 500,00):\nR$",
                "FishInvest – Reserva de Emergencia", JOptionPane.PLAIN_MESSAGE);
        if (valorStr == null) return;
 
        String[] produtos = {"Conta de liquidez diaria", "Tesouro Selic", "Fundos DI"};
        int prodIdx = JOptionPane.showOptionDialog(null,
                "Reserva de Emergencia\n\nEscolha o produto:",
                "FishInvest – Reserva de Emergencia",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, produtos, produtos[0]);
        if (prodIdx < 0) return;
 
        int confirma = JOptionPane.showConfirmDialog(null,
                "Reserva de Emergencia\n\n"
              + "Produto : " + produtos[prodIdx] + "\n"
              + "Valor   : R$ " + valorStr + "\n\n"
              + "Adicionar a reserva?",
                "FishInvest – Confirmar Aporte",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (confirma != JOptionPane.YES_OPTION) return;
 
        executarAporte(valorStr, "APORTE", "SALDO");
    }
 
    // =========================================================================
    // APORTE – AÇÕES
    // =========================================================================
    private static void aporteAcoes() {
 
        String valorStr = JOptionPane.showInputDialog(null,
                "Acoes\n\nValor do aporte (ex: 200,00):\nR$",
                "FishInvest – Acoes", JOptionPane.PLAIN_MESSAGE);
        if (valorStr == null) return;
 
        String[] acoes = {
            "PETR4  – Petrobras     – R$ 36,50",
            "VALE3  – Vale S.A.     – R$ 72,90",
            "ITUB4  – Itau Unibanco – R$ 28,90"
        };
        int acaoIdx = JOptionPane.showOptionDialog(null,
                "Acoes\n\nSelecionar Acao:",
                "FishInvest – Acoes",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, acoes, acoes[0]);
        if (acaoIdx < 0) return;
 
        String[] tickers = {"PETR4", "VALE3", "ITUB4"};
 
        int confirma = JOptionPane.showConfirmDialog(null,
                "Acoes\n\n"
              + "Acao  : " + tickers[acaoIdx] + "\n"
              + "Valor : R$ " + valorStr + "\n\n"
              + "Confirmar aporte?",
                "FishInvest – Confirmar Aporte",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (confirma != JOptionPane.YES_OPTION) return;
 
        executarAporte(valorStr, "APORTE", "SALDO");
    }
 
    // =========================================================================
    // TELA DEPOSITAR
    // =========================================================================
    private static void telaDepositar() {
 
        String valorStr = JOptionPane.showInputDialog(null,
                "Depositar\n\nValor do deposito (ex: 1000,00):\nR$",
                "FishInvest – Depositar", JOptionPane.PLAIN_MESSAGE);
        if (valorStr == null) return;
 
        String[] metodos = {"TED", "Boleto", "Cartao de Credito", "PIX (QR Code)"};
        int metIdx = JOptionPane.showOptionDialog(null,
                "Depositar\n\nMetodos de Deposito:",
                "FishInvest – Depositar",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, metodos, metodos[0]);
        if (metIdx < 0) return;
 
        // PIX: mostra chave e encerra
        if (metIdx == 3) {
            JOptionPane.showMessageDialog(null,
                    "Depositar via PIX\n\n"
                  + "Chave PIX  : 123456789\n"
                  + "Banco: 341 | Ag: 0234 | CC: 56789\n\n"
                  + "Escaneie o QR Code no seu banco para completar o deposito.",
                    "FishInvest – PIX", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
 
        int confirma = JOptionPane.showConfirmDialog(null,
                "Depositar\n\n"
              + "Valor  : R$ " + valorStr + "\n"
              + "Metodo : " + metodos[metIdx] + "\n\n"
              + "Confirmar deposito?",
                "FishInvest – Confirmar Deposito",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (confirma != JOptionPane.YES_OPTION) return;
 
        executarAporte(valorStr, "DEPOSITO", metodos[metIdx].toUpperCase());
    }
 
    // =========================================================================
    // EXECUTOR CENTRAL – busca carteira e chama o service
    // =========================================================================
    private static void executarAporte(String valorStr,
                                       String tipoOperacao,
                                       String metodoPagamento) {
        try {
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
                JOptionPane.showMessageDialog(null,
                        "Nenhuma carteira encontrada para sua conta.\n\n"
                      + "Solicite a criacao de uma carteira ao administrador.",
                        "FishInvest – Aporte", JOptionPane.ERROR_MESSAGE);
                return;
            }
 
            String comprovante = aporteService.realizarAporte(
                    clienteLogado, carteira, valorStr);
 
            JOptionPane.showMessageDialog(null,
                    comprovante,
                    "FishInvest – Comprovante",
                    JOptionPane.INFORMATION_MESSAGE);
 
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Erro ao realizar operacao:\n\n" + e.getMessage(),
                    "FishInvest – Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
 
    // =========================================================================
    // EFETUAR LOGIN
    // =========================================================================
    private static void efetuarLogin() {
 
        String email = JOptionPane.showInputDialog(null,
                "Digite seu e-mail:", "FishInvest – Login", JOptionPane.PLAIN_MESSAGE);
        if (email == null) return;
 
        String senha = JOptionPane.showInputDialog(null,
                "Digite sua senha:", "FishInvest – Login", JOptionPane.PLAIN_MESSAGE);
        if (senha == null) return;
 
        try {
            clienteLogado = clienteService.efetuarLogin(email.trim(), senha);
            JOptionPane.showMessageDialog(null,
                    "Login realizado com sucesso!\n\nBem-vindo(a), "
                    + clienteLogado.getNome() + "!",
                    "FishInvest – Login", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Falha no login:\n\n" + e.getMessage(),
                    "FishInvest – Login", JOptionPane.ERROR_MESSAGE);
        }
    }
 
    // =========================================================================
    // CRIAR CONTA  (agora coleta CPF)
    // =========================================================================
    private static void criarConta() {
 
        String nome = JOptionPane.showInputDialog(null,
                "Digite seu nome completo:",
                "FishInvest – Criar Conta", JOptionPane.PLAIN_MESSAGE);
        if (nome == null) return;
 
        String cpf = JOptionPane.showInputDialog(null,
                "Digite seu CPF (formato 000.000.000-00):",
                "FishInvest – Criar Conta", JOptionPane.PLAIN_MESSAGE);
        if (cpf == null) return;
 
        String email = JOptionPane.showInputDialog(null,
                "Digite seu e-mail:",
                "FishInvest – Criar Conta", JOptionPane.PLAIN_MESSAGE);
        if (email == null) return;
 
        String senha = JOptionPane.showInputDialog(null,
                "Digite sua senha (minimo 8 caracteres, com letras e numeros):",
                "FishInvest – Criar Conta", JOptionPane.PLAIN_MESSAGE);
        if (senha == null) return;
 
        String telefone = JOptionPane.showInputDialog(null,
                "Digite seu telefone:",
                "FishInvest – Criar Conta", JOptionPane.PLAIN_MESSAGE);
        if (telefone == null) return;
 
        int aceitou = JOptionPane.showConfirmDialog(null,
                "Voce leu e aceita os Termos de Uso e\nPolitica de Privacidade do FishInvest?",
                "FishInvest – Termos de Uso",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
 
        Cliente novo = new Cliente();
        novo.setNome(nome.trim());
        novo.setCpf(cpf.trim());
        novo.setEmail(email.trim());
        novo.setSenha(senha);
        novo.setTelefone(telefone.trim());
 
        try {
            Cliente criado = clienteService.criarConta(novo, aceitou == JOptionPane.YES_OPTION);
            JOptionPane.showMessageDialog(null,
                    "Conta criada com sucesso!\n\n"
                    + "Nome  : " + criado.getNome() + "\n"
                    + "CPF   : " + criado.getCpf()  + "\n"
                    + "E-mail: " + criado.getEmail() + "\n"
                    + "ID    : " + criado.getId(),
                    "FishInvest – Criar Conta", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Erro ao criar conta:\n\n" + e.getMessage(),
                    "FishInvest – Criar Conta", JOptionPane.ERROR_MESSAGE);
        }
    }
 
    // =========================================================================
    // SAIR
    // =========================================================================
    private static void sair() {
        JOptionPane.showMessageDialog(null,
                "Sistema encerrado. Ate logo!",
                "FishInvest", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }
}

        
        
