/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package br.unipar.fish.invest;

import br.unipar.fish.invest.domains.PerfilInvestidor;
import br.unipar.fish.invest.repositories.PerfilInvestidorRepository;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author matheus291107
 */

public class FishInvest {
    public static void main(String[] args) {
        PerfilInvestidorRepository repo = new PerfilInvestidorRepository();
        try {
            // ── DELETAR TODOS ─────────────────────────────────────────
            System.out.println("=== DELETAR TODOS ===");
            ArrayList<PerfilInvestidor> todos = repo.listarTodos();
            for (PerfilInvestidor p : todos) {
                repo.deletar(p.getId());
                System.out.println("Deletado ID: " + p.getId());
            }
            System.out.println("Todos deletados!\n");

            // ── LISTAR TODOS ─────────────────────────────────────────
            System.out.println("=== LISTAR TODOS ===");
            ArrayList<PerfilInvestidor> lista = repo.listarTodos();
            for (PerfilInvestidor p : lista) {
                System.out.println("ID: " + p.getId()
                        + " | Nome: " + p.getNomePerfil()
                        + " | Descrição: " + p.getDescricaoPerfil());
            }

            // ── FIND BY ID ───────────────────────────────────────────
            System.out.println("\n=== FIND BY ID ===");
            PerfilInvestidor encontrado = repo.findById(1);
            if (encontrado != null) {
                System.out.println(encontrado);
            } else {
                System.out.println("Perfil não encontrado.");
            }

            // ── ATUALIZAR ────────────────────────────────────────────
            System.out.println("\n=== ATUALIZAR ===");
            PerfilInvestidor paraAtualizar = repo.findById(1);
            if (paraAtualizar != null) {
                paraAtualizar.setNomePerfil("Conservador Moderado");
                paraAtualizar.setDescricaoPerfil("Baixo risco com pequena exposição a renda variável.");
                repo.atualizar(paraAtualizar);
                System.out.println("Atualizado: " + repo.findById(1));
            } else {
                System.out.println("Nenhum registro para atualizar.");
            }

            // ── DELETAR POR ID ────────────────────────────────────────
            System.out.println("\n=== DELETAR POR ID ===");
            PerfilInvestidor deletado = repo.findById(1);
            if (deletado != null) {
                repo.deletar(1);
                System.out.println("Deletado com sucesso!");
            } else {
                System.out.println("Nenhum registro para deletar.");
            }

            // ── LISTA FINAL ──────────────────────────────────────────
            System.out.println("\n=== LISTA FINAL ===");
            ArrayList<PerfilInvestidor> listaFinal = repo.listarTodos();
            if (listaFinal.isEmpty()) {
                System.out.println("Nenhum registro encontrado.");
            } else {
                for (PerfilInvestidor p : listaFinal) {
                    System.out.println("ID: " + p.getId() + " | " + p.getNomePerfil());
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro no banco: " + e.getMessage());
            e.printStackTrace();
        }
    }
}