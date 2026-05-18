/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.unipar.fish.invest.services;

import br.unipar.fish.invest.domains.Usuario;

/**
 *
 * @author matheus291107
 */
public interface UsuarioService {

    void cadastrar(Usuario usuario);

    Usuario login(String email, String senha);
}