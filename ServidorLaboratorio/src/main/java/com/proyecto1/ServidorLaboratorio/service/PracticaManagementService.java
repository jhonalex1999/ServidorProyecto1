/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto1.ServidorLaboratorio.service;

import com.proyecto1.ServidorLaboratorio.dto.PostDTO;
import java.util.List;

public interface PracticaManagementService {
    
    List<PostDTO> list();
    
    Boolean add(PostDTO post);
    Boolean edit(String id, PostDTO post);
    Boolean delete(String id);

}
