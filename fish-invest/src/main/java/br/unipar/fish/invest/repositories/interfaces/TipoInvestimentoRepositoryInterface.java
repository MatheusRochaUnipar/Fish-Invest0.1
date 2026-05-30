/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.unipar.fish.invest.repositories.interfaces;

import br.unipar.fish.invest.domains.TipoInvestimento;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author matheus291107
 */
public interface TipoInvestimentoRepositoryInterface {
    
    public TipoInvestimento inserir(TipoInvestimento tipoInvestimento) throws SQLException;
    
    public TipoInvestimento atualizar(TipoInvestimento tipoInvestimento) throws SQLException;
    
    public void deletar(int id) throws SQLException;
    
    public TipoInvestimento FindById(int id) throws SQLException;
    public ArrayList<TipoInvestimento> ListarTodos() throws SQLException;
}