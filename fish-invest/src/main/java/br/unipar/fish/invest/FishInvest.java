/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package br.unipar.fish.invest;

import br.unipar.fish.invest.domains.Usuario;
import br.unipar.fish.invest.services.UsuarioService;
import br.unipar.fish.invest.services.impl.UsuarioServiceImpl;


/**
 *
 * @author matheus291107
 */
public class FishInvest {

    public static void main(String[] args) {

        UsuarioService service =
                new UsuarioServiceImpl();

        Usuario usuario = new Usuario(
                "Matheus",
                "matheus@gmail.com",
                "matheus123",
                1000.0,
                true
        );

        try {

            service.cadastrar(usuario);

            Usuario login =
                    service.login(
                            "matheus@gmail.com",
                            "matheus123"
                    );

            System.out.println(
                    "LOGIN REALIZADO"
            );

            System.out.println(
                    login.getNome()
            );

        } catch (Exception e) {

            System.out.println(
                    e.getMessage()
            );
        }
    }
}