package com.proyecto1.ServidorLaboratorio.service;

import com.proyecto1.ServidorLaboratorio.dto.Variable_CaidaLibreDTO;
import com.proyecto1.ServidorLaboratorio.dto.AgendamientoDTO;
import com.proyecto1.ServidorLaboratorio.dto.GrupoDTO;
import com.proyecto1.ServidorLaboratorio.dto.Variable_LeyHookeDTO;
import com.proyecto1.ServidorLaboratorio.dto.Variable_MovimientoParabolicoDTO;
import com.proyecto1.ServidorLaboratorio.dto.ParticipantesDTO;
import com.proyecto1.ServidorLaboratorio.dto.PostDTO;
import com.proyecto1.ServidorLaboratorio.dto.PracticaDTO;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author admin
 */
public interface LaboratorioManagementService {
    
    Boolean descargarDatos(int codigo_planta);  
    
    List<Variable_LeyHookeDTO> listarDatosHardwareLeyDeHooke();
    
    List<Variable_MovimientoParabolicoDTO> listarDatosHardwareMovimientoParabolico();
    
    List<Variable_CaidaLibreDTO> listarDatosHardwareCaidaLibre();

    Boolean insertarProblema(String idLaboratorio, String problema);

    Boolean finalizarPractica(int codGrupal);
    
    Boolean buscarCompletitudEstudiantes(int codGrupal);
    
    String buscarQuienEsLider(String correo);
    
    Integer saberCodigoGrupo(String correo);
    
    ArrayList<String> listar_Altura_CL(int codigo_planta);
    
    ArrayList<String> listar_Elongacion_LH(int codigo_planta);
    
    ArrayList<String> listar_Fuerza_LH(int codigo_planta);
    
    ArrayList<String> listar_Angulo_MP(int codigo_planta);
    
    ArrayList<String> listar_Velocidad_MP(int codigo_planta);
    
    Boolean iniciarProceso(String planta);
       
    Boolean finalizarProceso(String planta);
    
    Boolean GuardarCaidaLibre();//2
    Boolean GuardarLeyHooke();//1
    Boolean GuardarMovimientoParabolico();//3
    
    ArrayList<Integer> retornarAltura(int id_planta);
   
}
