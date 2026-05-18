/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.unipar.fish.invest.services.impl;

import br.unipar.fish.invest.domains.Usuario;
import br.unipar.fish.invest.exceptions.BusinessException;
import br.unipar.fish.invest.repositories.UsuarioRepository;
import br.unipar.fish.invest.services.UsuarioService;
import java.time.LocalDateTime;

/**
 *
 * @author matheus291107
 */
public class UsuarioServiceImpl
        implements UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioServiceImpl() {

        repository = new UsuarioRepository();
    }

    @Override
    public void cadastrar(Usuario usuario) {

        validarNome(usuario.getNome());

        validarEmail(usuario.getEmail());

        validarSenha(usuario.getSenha());

        validarTermos(usuario.getAceitouTermos());

        validarEmailDuplicado(usuario.getEmail());

        repository.salvar(usuario);

        System.out.println(
                "Usuário cadastrado com sucesso!"
        );
    }

    @Override
    public Usuario login(String email,
            String senha) {

        Usuario usuario =
                repository.buscarPorEmail(email);

        if (usuario == null) {

            throw new BusinessException(
                    "E-mail ou senha incorretos"
            );
        }

        validarBloqueio(usuario);

        if (!usuario.getSenha().equals(senha)) {

            usuario.setTentativasLogin(
                    usuario.getTentativasLogin() + 1
            );

            if (usuario.getTentativasLogin() >= 3) {

                usuario.setBloqueadoAte(
                        LocalDateTime.now()
                                .plusMinutes(15)
                );

                throw new BusinessException(
                        "Usuário bloqueado por 15 minutos"
                );
            }

            throw new BusinessException(
                    "E-mail ou senha incorretos"
            );
        }

        usuario.setTentativasLogin(0);

        return usuario;
    }

    // ==========================
    // REGRAS DE NEGÓCIO
    // ==========================

    private void validarNome(String nome) {

        if (nome == null || nome.isBlank()) {

            throw new BusinessException(
                    "Nome obrigatório"
            );
        }
    }

    private void validarEmail(String email) {

        if (email == null || email.isBlank()) {

            throw new BusinessException(
                    "Email obrigatório"
            );
        }

        if (!email.matches(
                "^[A-Za-z0-9+_.-]+@(.+)$")) {

            throw new BusinessException(
                    "Formato de email inválido"
            );
        }
    }

    private void validarSenha(String senha) {

        if (senha == null || senha.isBlank()) {

            throw new BusinessException(
                    "Senha obrigatória"
            );
        }

        if (!senha.matches(
                "^(?=.*[A-Za-z])(?=.*\\d).{8,}$")) {

            throw new BusinessException(
                    "Senha deve conter no mínimo 8 caracteres, letras e números"
            );
        }
    }

    private void validarTermos(
            Boolean aceitouTermos) {

        if (aceitouTermos == null
                || !aceitouTermos) {

            throw new BusinessException(
                    "É obrigatório aceitar os termos"
            );
        }
    }

    private void validarEmailDuplicado(
            String email) {

        Usuario usuarioExistente =
                repository.buscarPorEmail(email);

        if (usuarioExistente != null) {

            throw new BusinessException(
                    "Email já cadastrado"
            );
        }
    }

    private void validarBloqueio(
            Usuario usuario) {

        if (usuario.getBloqueadoAte() != null
                && usuario.getBloqueadoAte()
                        .isAfter(LocalDateTime.now())) {

            throw new BusinessException(
                    "Usuário temporariamente bloqueado"
            );
        }
    }
}