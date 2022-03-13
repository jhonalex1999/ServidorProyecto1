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
public class Laboratorio_Movimiento_ParabolicoDTO {

    private String id;
    private Integer codigo_planta;
    private ArrayList<Integer> x;
    private ArrayList<Integer> y;
}
