/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto1.ServidorLaboratorio.service.impl;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.proyecto1.ServidorLaboratorio.dto.GrupoDTO;
import com.proyecto1.ServidorLaboratorio.dto.PostDTO;
import com.proyecto1.ServidorLaboratorio.firebase.FirebaseInitializer;
import com.proyecto1.ServidorLaboratorio.service.UsuarioManagementService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author admin
 */
@Service
public class UsuarioManagementServiceImpl implements UsuarioManagementService {

    @Autowired
    private FirebaseInitializer firebase;

    @Override
    public boolean prueba() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Boolean agregarUsuario(PostDTO post) {
        Map<String, Object> docData = getDocData(post);

        ApiFuture<WriteResult> writeResultApiFuture = getCollection("USUARIO").document().create(docData);

        try {
            if (null != writeResultApiFuture.get()) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    private CollectionReference getCollection(String Colecion) {
        return firebase.getFirestore().collection(Colecion);
    }

    @Override
    public Boolean buscarCodigoCurso(int idCurso) {
        GrupoDTO grupo;
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = firebase.getFirestore().collection("CURSO").whereEqualTo("idCurso", idCurso).get();

        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                grupo = doc.toObject(GrupoDTO.class);
                grupo.setId(doc.getId());
                String id = grupo.getId();
                return true;
            }
            return false;
        } catch (Exception e) {
            return null;
        }
    }

    private Map<String, Object> getDocData(PostDTO post) {
        Map<String, Object> docData = new HashMap<>();
        docData.put("correo", post.getCorreo());
        docData.put("rol", post.getRol());
        docData.put("nombreCompleto", post.getNombreCompleto());
        return docData;
    }

}
