/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto1.ServidorLaboratorio.service;

import com.proyecto1.ServidorLaboratorio.dto.AgendamientoDTO;
import com.proyecto1.ServidorLaboratorio.dto.PostDTO;
import com.proyecto1.ServidorLaboratorio.dto.PracticaDTO;
import java.util.List;

public interface PracticaManagementService {
    
    List<PracticaDTO> listarPracticas();
    Boolean verificarAgendamiento(int codGrupal, int codigoPlanta);
    Boolean add(PostDTO post);
    Boolean edit(String id, PostDTO post);
    Boolean delete(String id);

}
