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

/**
 *
 * @author matheus291107
 */


public class FishInvest {
    public static void main(String[] args) {


        // ── TESTE DO LOGIN ───────────────────────────────────────────────────────
        ClienteService clienteService = new ClienteService();

        System.out.println("=== TESTE DE LOGIN ===\n");

        // Teste 1: login com sucesso
        try {
            Cliente logado = clienteService.efetuarLogin("Daniele.teste@email.com", "senha123");
            System.out.println("✅ Login OK! Bem-vindo, " + logado.getNome());
        } catch (Exception e) {
            System.out.println("❌ " + e.getMessage());
        }

        // Teste 2: senha errada (3x para acionar o bloqueio)
        System.out.println("\n--- Testando bloqueio por tentativas ---");
        for (int i = 1; i <= 4; i++) {
            try {
                clienteService.efetuarLogin("Daniele.teste@email.com", "senhaErrada");
            } catch (Exception e) {
                System.out.println("Tentativa " + i + ": " + e.getMessage());
            }
        }

        // Teste 3: e-mail inválido
        System.out.println("\n--- Testando e-mail inválido ---");
        try {
            clienteService.efetuarLogin("emailinvalido", "senha123");
        } catch (Exception e) {
            System.out.println("❌ " + e.getMessage());
        }    

    
    
    }
        
        
}
