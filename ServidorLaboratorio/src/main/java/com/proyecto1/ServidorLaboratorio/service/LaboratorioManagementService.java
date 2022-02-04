/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto1.ServidorLaboratorio.service;

/**
 *
 * @author admin
 */
public interface LaboratorioManagementService {
        Boolean crearPdf();
        Boolean insertarHorario(String idFranjaHoraria, String idGrupo);
        Boolean buscarHorario(String idFranjaHoraria, String idGrupo);
        Boolean insertarProblema(String idLaboratorio,String problema);
        Boolean finalizarPractica(int codGrupal);
}
