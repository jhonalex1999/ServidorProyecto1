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
public class AgendamientoDTO {
    private Integer idAgendamiento;
    private String idFranja;
    private Integer codGrupal;
    private String horaInicio;
    private String horaFin;
    private Integer codigoPlanta;
    private Boolean estadoDisposicion;
    private String fecha;
}
