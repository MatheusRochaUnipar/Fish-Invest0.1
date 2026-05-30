/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.unipar.fish.invest.repositories.interfaces;

import br.unipar.fish.invest.domains.CarteiraInvestimentos;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author matheus291107
 */
public interface CarteiraInvestimentosRepositoryInterface {
    
    public CarteiraInvestimentos inserir(CarteiraInvestimentos carteira) throws SQLException;
    
    public CarteiraInvestimentos atualizar(CarteiraInvestimentos carteira) throws SQLException;
    
    public void deletar(int id) throws SQLException;
    
    public CarteiraInvestimentos FindById(int id) throws SQLException;
    
    public ArrayList<CarteiraInvestimentos> ListarTodos() throws SQLException;
}