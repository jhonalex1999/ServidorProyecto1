/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto1.ServidorLaboratorio.service;

import com.proyecto1.ServidorLaboratorio.dto.CursoDTO;
import com.proyecto1.ServidorLaboratorio.dto.PostDTO;
import com.proyecto1.ServidorLaboratorio.dto.UsuarioDTO;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author admin
 */
public interface UsuarioManagementService {

    boolean prueba();

    public ArrayList<String> buscarCursosMatriculados(String correo);

    public Boolean agregarCurso(String correo_institucional, String codigo_curso);

    public Boolean ingresarUsuario(String correo_institucional, String nombre);

    Boolean cambiarEstadoParticipanteEntrada(String correo);

    Boolean cambiarEstadoParticipanteSalida(String correo);
}
