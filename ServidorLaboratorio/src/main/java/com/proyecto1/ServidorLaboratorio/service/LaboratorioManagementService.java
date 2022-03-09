/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto1.ServidorLaboratorio.service;

import com.proyecto1.ServidorLaboratorio.dto.CaidaLibreDTO;
import com.proyecto1.ServidorLaboratorio.dto.AgendamientoDTO;
import com.proyecto1.ServidorLaboratorio.dto.GrupoDTO;
import com.proyecto1.ServidorLaboratorio.dto.LeyHookeDTO;
import com.proyecto1.ServidorLaboratorio.dto.MovimientoParabolicoDTO;
import com.proyecto1.ServidorLaboratorio.dto.ParticipantesDTO;
import com.proyecto1.ServidorLaboratorio.dto.PostDTO;
import com.proyecto1.ServidorLaboratorio.dto.PracticaDTO;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author admin
 */
public interface LaboratorioManagementService {

    List<AgendamientoDTO> listarAgendamiento(int codigoPlanta);
    
    List<LeyHookeDTO> listarDatosHardwareLeyDeHooke();
    
    List<MovimientoParabolicoDTO> listarDatosHardwareMovimientoParabolico();
    
    List<CaidaLibreDTO> listarDatosHardwareCaidaLibre();

    //Boolean crearPdf();
    
    Integer agregarParticipantes(ArrayList<String> participantes,int idFranja);


    Boolean buscarHorario(int idAgendamiento, int codGrupal);

    Boolean insertarProblema(String idLaboratorio, String problema);

    Boolean finalizarPractica(int codGrupal);
    
    Boolean buscarCompletitudEstudiantes(int codGrupal);
    
    String buscarQuienEsLider(String correo);
    
    Integer saberCodigoGrupo(String correo);

    Boolean cambiarEstadoParticipanteEntrada(String correo);
    
    Boolean cambiarEstadoParticipanteSalida(String correo);
    
    ArrayList<String> listar_Altura_CL(int codigo_planta);
    
    ArrayList<String> listar_Elongacion_LH(int codigo_planta);
    
    ArrayList<String> listar_Fuerza_LH(int codigo_planta);
    
    ArrayList<String> listar_Angulo_MP(int codigo_planta);
    
    ArrayList<String> listar_Velocidad_MP(int codigo_planta);
   
}
