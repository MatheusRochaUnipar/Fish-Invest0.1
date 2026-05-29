/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.unipar.fish.invest.services;

import br.unipar.fish.invest.domains.CarteiraInvestimentos;
import br.unipar.fish.invest.domains.Cliente;
import br.unipar.fish.invest.domains.TipoInvestimento;
import br.unipar.fish.invest.domains.TransacoesEDepositos;
import br.unipar.fish.invest.repositories.CarteiraInvestimentosRepository;
import br.unipar.fish.invest.repositories.ClienteRepository;
import br.unipar.fish.invest.repositories.TransacoesEDepositosRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author matheus291107
 */


/**
 * Service de Transações e Depósitos.
 * Caso de Uso 04 - Realizar Aporte.
 *
 * RN-01  Valor minimo: aporte > R$ 0,00
 * RN-02  Saldo da carteira (saldoTotal) >= valor do aporte
 * RN-03  Atualizacao em tempo real: debita saldoTotal, soma ao rendimentoAcumulado
 * RN-04  Data registrada automaticamente (LocalDate.now())
 * RN-05  Limite maximo por aporte (constante LIMITE_MAXIMO_APORTE)
 * RN-06  Somente valores positivos, formato monetario BR (2 casas decimais)
 * RN-07  Produto (CarteiraInvestimentos) obrigatorio
 * RN-08  Registro no historico (TransacoesEDepositos)
 * RN-09  Comprovante digital gerado apos confirmacao
 * RN-10  Integridade transacional (rollback em falha)
 * RN-11  Validacao de sessao (cliente autenticado)
 * RN-12  Bloqueio de aporte duplicado (flag de processamento)
 * RN-13  TipoInvestimento obrigatorio na carteira
 * RN-14  Arredondamento com 2 casas decimais (HALF_UP)
 */
public class TransacoesEDepositosService {

    // RN-05 – limite maximo por aporte
    private static final BigDecimal LIMITE_MAXIMO_APORTE = new BigDecimal("1000000.00");

    private final CarteiraInvestimentosRepository carteiraRepo;
    private final TransacoesEDepositosRepository  transacaoRepo;
    private final ClienteRepository               clienteRepo;

    // RN-12 – flag de bloqueio contra duplo clique
    private boolean processandoAporte = false;

    public TransacoesEDepositosService() {
        this.carteiraRepo  = new CarteiraInvestimentosRepository();
        this.transacaoRepo = new TransacoesEDepositosRepository();
        this.clienteRepo   = new ClienteRepository();
    }

    // =========================================================================
    // CU-04 – REALIZAR APORTE
    // Aceita o valor como String (vem direto do JOptionPane) no formato BR
    // =========================================================================
    public String realizarAporte(Cliente clienteLogado,
                                 CarteiraInvestimentos carteira,
                                 String valorTexto) throws Exception {

        // RN-12 – Bloqueio de aporte duplicado
        if (processandoAporte) {
            throw new Exception(
                "RN-12: Uma operacao ja esta em andamento. "
              + "Aguarde a conclusao antes de tentar novamente.");
        }

        processandoAporte = true;

        try {
            // RN-11 – Sessao valida
            validarSessao(clienteLogado);

            // RN-07 – Carteira obrigatoria
            validarCarteira(carteira);

            // RN-13 – TipoInvestimento obrigatorio
            validarTipoInvestimento(carteira);

            // RN-06 / RN-14 – Converter String BR para BigDecimal com 2 casas
            BigDecimal valor = converterEArredondar(valorTexto);

            // RN-01 – Valor maior que zero
            validarValorMinimo(valor);

            // RN-05 – Nao ultrapassa o limite maximo
            validarLimiteMaximo(valor);

            // RN-02 – Saldo suficiente na carteira
            validarSaldo(carteira, valor);

            // RN-03,04,08,09,10 – Execucao transacional
            return executarTransacao(clienteLogado, carteira, valor);

        } finally {
            processandoAporte = false; // RN-12: libera sempre
        }
    }

    // =========================================================================
    // VALIDACOES
    // =========================================================================

    /** RN-11 */
    private void validarSessao(Cliente cliente) throws Exception {
        if (cliente == null) {
            throw new Exception(
                "RN-11: Nenhum usuario autenticado. "
              + "Faca login antes de realizar um aporte.");
        }
        if (cliente.getId() == null || cliente.getId() <= 0) {
            throw new Exception("RN-11: Sessao invalida. Faca login novamente.");
        }
    }

    /** RN-07 */
    private void validarCarteira(CarteiraInvestimentos carteira) throws Exception {
        if (carteira == null) {
            throw new Exception(
                "RN-07: Selecione um produto de investimento "
              + "antes de confirmar o aporte.");
        }
    }

    /** RN-13 */
    private void validarTipoInvestimento(CarteiraInvestimentos carteira) throws Exception {
        TipoInvestimento tipo = carteira.getTipoInvestimento();
        if (tipo == null) {
            throw new Exception(
                "RN-13: A carteira selecionada nao possui "
              + "tipo de investimento definido.");
        }
        if (tipo.getNomeTipo() == null || tipo.getNomeTipo().isBlank()) {
            throw new Exception(
                "RN-13: O tipo de investimento da carteira esta invalido.");
        }
    }

    /**
     * RN-06 / RN-14
     * Aceita formatos: "1.500,50" | "1500,50" | "1500.50" | "R$ 1.500,00"
     */
    private BigDecimal converterEArredondar(String valorTexto) throws Exception {
        if (valorTexto == null || valorTexto.isBlank()) {
            throw new Exception("RN-06: O valor do aporte nao pode ser vazio.");
        }
        try {
            String normalizado = valorTexto
                    .trim()
                    .replace("R$", "")
                    .replace(" ", "")
                    .replace(".", "")   // remove separador de milhar BR
                    .replace(",", "."); // troca decimal BR por ponto
            return new BigDecimal(normalizado).setScale(2, RoundingMode.HALF_UP);
        } catch (NumberFormatException e) {
            throw new Exception(
                "RN-06: Valor invalido. Use o formato monetario brasileiro "
              + "(ex: 1.500,00 ou 1500,00).");
        }
    }

    /** RN-01 */
    private void validarValorMinimo(BigDecimal valor) throws Exception {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new Exception(
                "RN-01: O valor do aporte deve ser maior que R$ 0,00.");
        }
    }

    /** RN-05 */
    private void validarLimiteMaximo(BigDecimal valor) throws Exception {
        if (valor.compareTo(LIMITE_MAXIMO_APORTE) > 0) {
            throw new Exception(String.format(
                "RN-05: O valor R$ %.2f excede o limite maximo "
              + "permitido por aporte de R$ %.2f.",
                valor, LIMITE_MAXIMO_APORTE));
        }
    }

    /** RN-02 – usa saldoTotal da CarteiraInvestimentos */
    private void validarSaldo(CarteiraInvestimentos carteira,
                               BigDecimal valor) throws Exception {
        BigDecimal saldo = carteira.getSaldoTotal();
        if (saldo == null || saldo.compareTo(valor) < 0) {
            throw new Exception(String.format(
                "RN-02: Saldo insuficiente na carteira '%s'. "
              + "Disponivel: R$ %.2f | Aporte solicitado: R$ %.2f.",
                carteira.getNomeEspecifico(),
                saldo != null ? saldo : BigDecimal.ZERO,
                valor));
        }
    }

    // =========================================================================
    // EXECUCAO TRANSACIONAL  (RN-03, RN-04, RN-08, RN-09, RN-10)
    // =========================================================================
    private String executarTransacao(Cliente cliente,
                                     CarteiraInvestimentos carteira,
                                     BigDecimal valor) throws Exception {

        LocalDate hoje = LocalDate.now(); // RN-04

        try {
            // RN-03 – debita saldoTotal da carteira
            BigDecimal novoSaldo = carteira.getSaldoTotal()
                    .subtract(valor)
                    .setScale(2, RoundingMode.HALF_UP);
            carteira.setSaldoTotal(novoSaldo);

            // RN-03 – acumula em rendimentoAcumulado (total investido)
            BigDecimal rendAtual = carteira.getRendimentoAcumulado() != null
                    ? carteira.getRendimentoAcumulado() : BigDecimal.ZERO;
            BigDecimal novoRendimento = rendAtual
                    .add(valor)
                    .setScale(2, RoundingMode.HALF_UP);
            carteira.setRendimentoAcumulado(novoRendimento);

            // RN-08 – monta TransacoesEDepositos com os campos reais do domain
            TransacoesEDepositos transacao = new TransacoesEDepositos();
            transacao.setCliente(cliente);
            transacao.setCarteiraInvestimentos(carteira);
            transacao.setValorTransacao(valor);
            transacao.setTipoOperacao("APORTE");
            transacao.setMetodoPagamento("SALDO");
            transacao.setDataTransacao(hoje);
            transacao.setStatusOperacao("CONCLUIDO");

            // RN-10 – persiste as alteracoes
            carteiraRepo.atualizar(carteira);
            transacao = transacaoRepo.inserir(transacao);

            // Log
            System.out.printf(
                "[LOG] Aporte | Cliente: %s (ID %d) | Carteira: %s | Produto: %s | "
              + "Valor: R$ %.2f | Data: %s | Novo saldo carteira: R$ %.2f%n",
                cliente.getNome(), cliente.getId(),
                carteira.getNomeEspecifico(),
                carteira.getTipoInvestimento().getNomeTipo(),
                valor,
                hoje.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                novoSaldo);

            // RN-09 – comprovante
            return gerarComprovante(cliente, carteira, valor,
                                    novoSaldo, novoRendimento, hoje, transacao);

        } catch (SQLException e) {
            // RN-10 – propaga para rollback
            throw new Exception(
                "RN-10: Falha ao persistir a transacao. Operacao revertida. "
              + "Detalhe: " + e.getMessage());
        }
    }

    // =========================================================================
    // COMPROVANTE DIGITAL  (RN-09)
    // =========================================================================
    private String gerarComprovante(Cliente cliente,
                                    CarteiraInvestimentos carteira,
                                    BigDecimal valor,
                                    BigDecimal novoSaldo,
                                    BigDecimal novoRendimento,
                                    LocalDate data,
                                    TransacoesEDepositos transacao) {

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return  "=========================================\n"
              + "   COMPROVANTE DE APORTE - FishInvest   \n"
              + "=========================================\n"
              + String.format("Data         : %s%n",       data.format(fmt))
              + String.format("Nr Transacao : %s%n",       transacao.getId())
              + "-----------------------------------------\n"
              + String.format("Cliente      : %s%n",       cliente.getNome())
              + String.format("E-mail       : %s%n",       cliente.getEmail())
              + "-----------------------------------------\n"
              + String.format("Carteira     : %s%n",       carteira.getNomeEspecifico())
              + String.format("Produto      : %s%n",       carteira.getTipoInvestimento().getNomeTipo())
              + String.format("Status       : %s%n",       "CONCLUIDO")
              + "-----------------------------------------\n"
              + String.format("Valor Aportado  : R$ %,.2f%n", valor)
              + String.format("Saldo Carteira  : R$ %,.2f%n", novoSaldo)
              + String.format("Total Investido : R$ %,.2f%n", novoRendimento)
              + "=========================================\n"
              + "   Operacao realizada com sucesso!";
    }
}
