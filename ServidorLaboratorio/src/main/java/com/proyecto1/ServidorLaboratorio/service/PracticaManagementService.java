/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto1.ServidorLaboratorio.service;

import com.proyecto1.ServidorLaboratorio.dto.AgendamientoDTO;
import com.proyecto1.ServidorLaboratorio.dto.PostDTO;
import com.proyecto1.ServidorLaboratorio.dto.PracticaDTO;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public interface PracticaManagementService {

    Boolean descargarArchivoProfesor(int codigo_planta) throws MalformedURLException, IOException, Exception;

    List<AgendamientoDTO> listarAgendamiento(int codigoPlanta);

    Integer agregarParticipantes(ArrayList<String> participantes, int idFranja);

    Boolean buscarHorario(int idAgendamiento, int codGrupal);

    List<PracticaDTO> listarPracticas();

    Boolean verificarAgendamiento(int codGrupal, int codigoPlanta);

    String descripcionProfesorPractica(int codigo_planta);

}
