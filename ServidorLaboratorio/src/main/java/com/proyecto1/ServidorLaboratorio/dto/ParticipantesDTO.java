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
public class ParticipantesDTO {
    private String id;
    private Integer codGrupal;
    private String correo;
    private String rol;
    private Integer estado;
}
