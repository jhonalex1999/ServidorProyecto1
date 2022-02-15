/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto1.ServidorLaboratorio.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 *
 * @author julio
 */
@Data
public class UsuarioDTO {
    private String id;
    private int idUsuario;
    private String correo;
    private String rol;
    private String nombreCompleto;
    private ArrayList<String> Cursos;
}
