/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package br.unipar.fish.invest;

import br.unipar.fish.invest.domains.CarteiraInvestimentos;
import br.unipar.fish.invest.domains.Cliente;
import br.unipar.fish.invest.domains.PerfilInvestidor;
import br.unipar.fish.invest.domains.SegurancaAcesso;
import br.unipar.fish.invest.domains.TipoInvestimento;
import br.unipar.fish.invest.domains.TransacoesEDepositos;
import br.unipar.fish.invest.repositories.CarteiraInvestimentosRepository;
import br.unipar.fish.invest.repositories.ClienteRepository;
import br.unipar.fish.invest.repositories.PerfilInvestidorRepository;
import br.unipar.fish.invest.repositories.TipoInvestimentoRepository;
import br.unipar.fish.invest.repositories.TransacoesEDepositosRepository;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author matheus291107
 */


public class FishInvest {
    public static void main(String[] args) {
        
    TransacoesEDepositosRepository repository = new TransacoesEDepositosRepository();

        try {
            System.out.println("=================================================");
            System.out.println(" INICIANDO TESTES: TRANSAÇÕES E DEPÓSITOS ");
            System.out.println("=================================================\n");

            // --- PREPARAÇÃO DAS CHAVES ESTRANGEIRAS ---
            // ATENÇÃO: Os IDs abaixo DEVEM existir no seu banco de dados!
            Cliente clienteFicticio = new Cliente();
            clienteFicticio.setId(2); // Mude para um ID de Cliente válido no seu DB

            CarteiraInvestimentos carteiraFicticia = new CarteiraInvestimentos();
            carteiraFicticia.setId(4); // Mude para um ID de Carteira válido no seu DB


            // 1. TESTE DE INSERT
            System.out.println("--- 1. TESTANDO INSERT ---");
            TransacoesEDepositos novaTransacao = new TransacoesEDepositos();
            novaTransacao.setCliente(clienteFicticio);
            novaTransacao.setCarteiraInvestimentos(carteiraFicticia);
            novaTransacao.setValorTransacao(new BigDecimal("500.50"));
            novaTransacao.setTipoOperacao("DEPÓSITO");
            novaTransacao.setMetodoPagamento("PIX");
            novaTransacao.setDataTransacao(LocalDate.now());
            novaTransacao.setStatusOperacao("PENDENTE");

            novaTransacao = repository.inserir(novaTransacao);
            System.out.println("✅ Transação inserida com sucesso! ID gerado: " + novaTransacao.getId());


            // 2. TESTE DE FIND_ALL
            System.out.println("\n--- 2. TESTANDO FIND_ALL ---");
            ArrayList<TransacoesEDepositos> listaTransacoes = repository.listarTodos();
            System.out.println("Total de transações no banco: " + listaTransacoes.size());
            for (TransacoesEDepositos t : listaTransacoes) {
                System.out.println(" -> ID: " + t.getId() 
                        + " | Operação: " + t.getTipoOperacao() 
                        + " | Valor: R$ " + t.getValorTransacao()
                        + " | Status: " + t.getStatusOperacao()
                        + " | Cliente Dono: " + t.getCliente().getNome());
            }


            // 3. TESTE DE UPDATE
            System.out.println("\n--- 3. TESTANDO UPDATE ---");
            novaTransacao.setStatusOperacao("CONCLUÍDO"); // Simulando a aprovação do pagamento
            repository.atualizar(novaTransacao);
            System.out.println("✅ Transação ID " + novaTransacao.getId() + " atualizada para o status: " + novaTransacao.getStatusOperacao());


            // 4. TESTE DE FIND_BY_ID
            System.out.println("\n--- 4. TESTANDO FIND_BY_ID ---");
            TransacoesEDepositos transacaoBuscada = repository.findById(novaTransacao.getId());
            if (transacaoBuscada != null) {
                System.out.println("✅ Transação Encontrada:");
                System.out.println(" -> Tipo da Operação.: " + transacaoBuscada.getTipoOperacao());
                System.out.println(" -> Método de Pagto..: " + transacaoBuscada.getMetodoPagamento());
                System.out.println(" -> Status Atual.....: " + transacaoBuscada.getStatusOperacao());
            } else {
                System.out.println("❌ Transação não encontrada!");
            }


            // Verificação extra para garantir que foi apagado
            TransacoesEDepositos transacaoDeletada = repository.findById(novaTransacao.getId());
            if (transacaoDeletada == null) {
                System.out.println("✅ Confirmação: A transação foi removida do banco de dados.");
            } else {
                System.out.println("❌ Erro: A transação ainda existe no banco.");
            }

        } catch (SQLException e) {
            System.err.println("\n❌ Ocorreu um erro de SQL! Lembra da Chave Estrangeira? Verifique se o Cliente e a Carteira existem no banco:");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("\n❌ Ocorreu um erro inesperado:");
            e.printStackTrace();
        } 
        
        
        
    }
        
        
    }
