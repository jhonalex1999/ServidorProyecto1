/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto1.ServidorLaboratorio.service;

import com.proyecto1.ServidorLaboratorio.dto.CaidaLibreDTO;
import com.proyecto1.ServidorLaboratorio.dto.FranjaHorariaDTO;
import com.proyecto1.ServidorLaboratorio.dto.GrupoDTO;
import com.proyecto1.ServidorLaboratorio.dto.LeyHookeDTO;
import com.proyecto1.ServidorLaboratorio.dto.MovimientoParabolicoDTO;
import com.proyecto1.ServidorLaboratorio.dto.ParticipantesDTO;
import com.proyecto1.ServidorLaboratorio.dto.PostDTO;
import com.proyecto1.ServidorLaboratorio.dto.PracticaDTO;
import java.util.List;

/**
 *
 * @author admin
 */
public interface LaboratorioManagementService {

    List<FranjaHorariaDTO> listarFranjaHoraria();
    
    List<LeyHookeDTO> listarDatosHardwareLeyDeHooke();
    
    List<MovimientoParabolicoDTO> listarDatosHardwareMovimientoParabolico();
    
    List<CaidaLibreDTO> listarDatosHardwareCaidaLibre();

    Boolean crearPdf();
    
    Boolean agregarParticipantes(ParticipantesDTO post);

    Boolean insertarHorario(String idFranjaHoraria, String idGrupo);

    Boolean buscarHorario(String idFranjaHoraria, String idGrupo);

    Boolean insertarProblema(String idLaboratorio, String problema);

    Boolean finalizarPractica(int codGrupal);
    
    Boolean buscarCompletitudEstudiantes(int codGrupal);
}
