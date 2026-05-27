/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.unipar.fish.invest.infraescture;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author matheus291107
 */

public class ConnectionFactory {
    
    public Connection getConnection() throws SQLException{
        return DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/FishInvest",
            "postgres",
            "unipar");
    }
}
