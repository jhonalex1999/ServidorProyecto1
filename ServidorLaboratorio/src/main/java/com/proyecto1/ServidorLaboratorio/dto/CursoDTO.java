/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto1.ServidorLaboratorio.dto;

import lombok.Data;

/**
 *
 * @author julio
 */
@Data
public class CursoDTO {

    private String nombreCurso;
    private Integer codigoMatricula;
    private String fechaCreacion;
    private String fechaEliminacion;
    private Integer tamanoGrupo;
    private String nombreCompleto;
}
