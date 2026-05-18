/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.unipar.fish.invest.repositories;

import br.unipar.fish.invest.domains.Usuario;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author matheus291107
 */
public class UsuarioRepository {

    private final List<Usuario> usuarios =
            new ArrayList<>();

    public void salvar(Usuario usuario) {

        usuarios.add(usuario);
    }

    public Usuario buscarPorEmail(String email) {

        for (Usuario usuario : usuarios) {

            if (usuario.getEmail()
                    .equalsIgnoreCase(email)) {

                return usuario;
            }
        }

        return null;
    }

    public List<Usuario> listarTodos() {

        return usuarios;
    }
}