/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.unipar.fish.invest.repositories;

import br.unipar.fish.invest.domains.Cliente;
import br.unipar.fish.invest.domains.SegurancaAcesso;
import br.unipar.fish.invest.infraescture.ConnectionFactory;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

/**
 *
 * @author matheus291107
 */

public class ClienteRepository {

    private static final String INSERT =
            "INSERT INTO cliente (nome, email, senha, telefone, "
            + "pin_acesso, biometria_ativada, data_cadastro) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?);";

    private static final String UPDATE =
            "UPDATE cliente SET nome = ?, email = ?, senha = ?, telefone = ?, "
            + "pin_acesso = ?, biometria_ativada = ?, data_cadastro = ? "
            + "WHERE id_cliente = ?;";

    private static final String DELETE =
            "DELETE FROM cliente WHERE id_cliente = ?;";

    private static final String FIND_BY_ID =
            "SELECT id_cliente, nome, email, senha, telefone, "
            + "pin_acesso, biometria_ativada, data_cadastro "
            + "FROM cliente "
            + "WHERE id_cliente = ?;";

    private static final String FIND_ALL =
            "SELECT id_cliente, nome, email, senha, telefone, "
            + "pin_acesso, biometria_ativada, data_cadastro "
            + "FROM cliente "
            + "ORDER BY nome;";

    private static final String FIND_BY_EMAIL =
            "SELECT id_cliente, nome, email, senha, telefone, "
            + "pin_acesso, biometria_ativada, data_cadastro "
            + "FROM cliente "
            + "WHERE email = ?;";

    private static final String EXISTS_BY_EMAIL =
            "SELECT COUNT(*) FROM cliente WHERE email = ?;";


    public Cliente inserir(Cliente cliente) throws SQLException {

        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            conn = new ConnectionFactory().getConnection();

            pstm = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            pstm.setString(1, cliente.getNome());
            pstm.setString(2, cliente.getEmail());
            pstm.setString(3, cliente.getSenha());
            pstm.setString(4, cliente.getTelefone());

            if (cliente.getSegurancaAcesso() != null) {
                pstm.setString(5, cliente.getSegurancaAcesso().getPinAcesso());
                pstm.setBoolean(6, cliente.getSegurancaAcesso().getBiometriaAtiva());
            } else {
                pstm.setNull(5, Types.VARCHAR);
                pstm.setBoolean(6, false);
            }

            pstm.setDate(7, Date.valueOf(cliente.getDataCadastro()));

            pstm.executeUpdate();

            rs = pstm.getGeneratedKeys();
            if (rs.next()) cliente.setId(rs.getInt(1));

        } finally {
            if (rs != null) rs.close();
            if (pstm != null) pstm.close();
            if (conn != null) conn.close();
        }

        return cliente;
    }


    public Cliente atualizar(Cliente cliente) throws SQLException {

        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            conn = new ConnectionFactory().getConnection();

            pstm = conn.prepareStatement(UPDATE);
            pstm.setString(1, cliente.getNome());
            pstm.setString(2, cliente.getEmail());
            pstm.setString(3, cliente.getSenha());
            pstm.setString(4, cliente.getTelefone());

            if (cliente.getSegurancaAcesso() != null) {
                pstm.setString(5, cliente.getSegurancaAcesso().getPinAcesso());
                pstm.setBoolean(6, cliente.getSegurancaAcesso().getBiometriaAtiva());
            } else {
                pstm.setNull(5, Types.VARCHAR);
                pstm.setBoolean(6, false);
            }

            pstm.setDate(7, Date.valueOf(cliente.getDataCadastro()));
            pstm.setInt(8, cliente.getId());

            pstm.executeUpdate();

        } finally {
            if (pstm != null) pstm.close();
            if (conn != null) conn.close();
        }

        return cliente;
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


    public Cliente findById(Integer id) throws SQLException {

        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Cliente cliente = null;

        try {
            conn = new ConnectionFactory().getConnection();

            pstm = conn.prepareStatement(FIND_BY_ID);
            pstm.setInt(1, id);

            rs = pstm.executeQuery();

            if (rs.next()) {
                cliente = montarCliente(rs);
            }

        } finally {
            if (rs != null) rs.close();
            if (pstm != null) pstm.close();
            if (conn != null) conn.close();
        }

        return cliente;
    }


    public Cliente findByEmail(String email) throws SQLException {

        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Cliente cliente = null;

        try {
            conn = new ConnectionFactory().getConnection();

            pstm = conn.prepareStatement(FIND_BY_EMAIL);
            pstm.setString(1, email);

            rs = pstm.executeQuery();

            if (rs.next()) {
                cliente = montarCliente(rs);
            }

        } finally {
            if (rs != null) rs.close();
            if (pstm != null) pstm.close();
            if (conn != null) conn.close();
        }

        return cliente;
    }


    public boolean existsByEmail(String email) throws SQLException {

        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            conn = new ConnectionFactory().getConnection();

            pstm = conn.prepareStatement(EXISTS_BY_EMAIL);
            pstm.setString(1, email);

            rs = pstm.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } finally {
            if (rs != null) rs.close();
            if (pstm != null) pstm.close();
            if (conn != null) conn.close();
        }

        return false;
    }


    public ArrayList<Cliente> listarTodos() throws SQLException {

        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        ArrayList<Cliente> lista = new ArrayList<>();

        try {
            conn = new ConnectionFactory().getConnection();

            pstm = conn.prepareStatement(FIND_ALL);

            rs = pstm.executeQuery();

            while (rs.next()) {
                lista.add(montarCliente(rs));
            }

        } finally {
            if (rs != null) rs.close();
            if (pstm != null) pstm.close();
            if (conn != null) conn.close();
        }

        return lista;
    }


    private Cliente montarCliente(ResultSet rs) throws SQLException {

        SegurancaAcesso seguranca = new SegurancaAcesso();
        seguranca.setPinAcesso(rs.getString("pin_acesso"));
        seguranca.setBiometriaAtiva(rs.getBoolean("biometria_ativada"));

        Cliente cliente = new Cliente();
        cliente.setId(rs.getInt("id_cliente"));
        cliente.setNome(rs.getString("nome"));
        cliente.setEmail(rs.getString("email"));
        cliente.setSenha(rs.getString("senha"));
        cliente.setTelefone(rs.getString("telefone"));
        cliente.setDataCadastro(rs.getDate("data_cadastro").toLocalDate());
        cliente.setSegurancaAcesso(seguranca);

        return cliente;
    }
} 