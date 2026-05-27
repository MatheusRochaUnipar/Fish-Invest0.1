/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.unipar.fish.invest.repositories;

import br.unipar.fish.invest.domains.PerfilInvestidor;
import br.unipar.fish.invest.infraescture.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author matheus291107
 */
public class PerfilInvestidorRepository {

    private static final String INSERT =
            "INSERT INTO perfil_investidor (nome_perfil, descricao_perfil) "
            + "VALUES (?, ?);";
  
    private static final String UPDATE =
            "UPDATE perfil_investidor SET nome_perfil = ?, descricao_perfil = ? "
            + "WHERE id_perfil = ?;";

    private static final String DELETE =
            "DELETE FROM perfil_investidor WHERE id_perfil = ?;";

    private static final String FIND_BY_ID =
            "SELECT id_perfil, nome_perfil, descricao_perfil "
            + "FROM perfil_investidor WHERE id_perfil = ?;";

    private static final String FIND_ALL =
            "SELECT id_perfil, nome_perfil, descricao_perfil "
            + "FROM perfil_investidor ORDER BY nome_perfil;";

    public PerfilInvestidor inserir(PerfilInvestidor perfil) throws SQLException {

        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            conn = new ConnectionFactory().getConnection();

            pstm = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            pstm.setString(1, perfil.getNomePerfil());
            pstm.setString(2, perfil.getDescricaoPerfil());

            pstm.executeUpdate();

            rs = pstm.getGeneratedKeys();
            if (rs.next()) perfil.setId(rs.getInt(1));

        } finally {
            if (rs != null) rs.close();
            if (pstm != null) pstm.close();
            if (conn != null) conn.close();
        }

        return perfil;
    }


    public PerfilInvestidor atualizar(PerfilInvestidor perfil) throws SQLException {

        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            conn = new ConnectionFactory().getConnection();

            pstm = conn.prepareStatement(UPDATE);
            pstm.setString(1, perfil.getNomePerfil());
            pstm.setString(2, perfil.getDescricaoPerfil());
            pstm.setInt(3, perfil.getId());

            pstm.executeUpdate();

        } finally {
            if (pstm != null) pstm.close();
            if (conn != null) conn.close();
        }

        return perfil;
    }


    public void deletar(Integer id) throws SQLException {

        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            conn = new ConnectionFactory().getConnection();

            pstm = conn.prepareStatement(DELETE);
            pstm.setInt(1, id);

            pstm.executeUpdate();

        } finally {
            if (pstm != null) pstm.close();
            if (conn != null) conn.close();
        }
    }


    public PerfilInvestidor findById(Integer id) throws SQLException {

        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        PerfilInvestidor perfil = null;

        try {
            conn = new ConnectionFactory().getConnection();

            pstm = conn.prepareStatement(FIND_BY_ID);
            pstm.setInt(1, id);

            rs = pstm.executeQuery();

            if (rs.next()) {
                perfil = new PerfilInvestidor();
                perfil.setId(rs.getInt("id_perfil"));
                perfil.setNomePerfil(rs.getString("nome_perfil"));
                perfil.setDescricaoPerfil(rs.getString("descricao_perfil"));
            }

        } finally {
            if (rs != null) rs.close();
            if (pstm != null) pstm.close();
            if (conn != null) conn.close();
        }

        return perfil;
    }


    public ArrayList<PerfilInvestidor> listarTodos() throws SQLException {

        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        ArrayList<PerfilInvestidor> lista = new ArrayList<>();

        try {
            conn = new ConnectionFactory().getConnection();

            pstm = conn.prepareStatement(FIND_ALL);

            rs = pstm.executeQuery();

            while (rs.next()) {
                PerfilInvestidor perfil = new PerfilInvestidor();
                perfil.setId(rs.getInt("id_perfil"));
                perfil.setNomePerfil(rs.getString("nome_perfil"));
                perfil.setDescricaoPerfil(rs.getString("descricao_perfil"));

                lista.add(perfil);
            }

        } finally {
            if (rs != null) rs.close();
            if (pstm != null) pstm.close();
            if (conn != null) conn.close();
        }

        return lista;
    }

}