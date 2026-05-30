/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.unipar.fish.invest.repositories.interfaces;

import br.unipar.fish.invest.domains.TransacoesEDepositos;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author matheus291107
 */
public interface TransacoesEDepositosRepositoryInterface {
    
    public TransacoesEDepositos inserir(TransacoesEDepositos transacao) throws SQLException;
    
    public TransacoesEDepositos atualizar(TransacoesEDepositos transacao) throws SQLException;
    
    public void deletar(int id) throws SQLException;
    
    public TransacoesEDepositos FindById(int id) throws SQLException;
    public ArrayList<TransacoesEDepositos> ListarTodos() throws SQLException;
}