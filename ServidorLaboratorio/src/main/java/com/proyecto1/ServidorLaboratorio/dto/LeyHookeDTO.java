/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto1.ServidorLaboratorio.dto;

import java.util.ArrayList;
import lombok.Data;



@Data
public class LeyHookeDTO {
    private ArrayList<String> elongaciones;
    private int nRep;
    private ArrayList<String> pesos;
}
