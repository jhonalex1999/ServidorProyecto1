/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto1.ServidorLaboratorio.dto;

import java.util.ArrayList;
import lombok.Data;

/**
 *
 * @author julio
 */
@Data
public class LaboratorioDTO {

    private Integer check;
    private Boolean datos;
    private ArrayList<Integer> elongaciones;
    private Boolean finalizado;
    private Boolean iniciar;
    private Integer nRep;
    private Integer peso;
    private ArrayList<Integer> pesos;
}
