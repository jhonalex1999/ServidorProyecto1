/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto1.ServidorLaboratorio.service.impl;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.proyecto1.ServidorLaboratorio.dto.GrupoDTO;
import com.proyecto1.ServidorLaboratorio.dto.PostDTO;
import com.proyecto1.ServidorLaboratorio.firebase.FirebaseInitializer;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.proyecto1.ServidorLaboratorio.service.LaboratorioManagementService;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author admin
 */
@Service
public class LaboratorioManagementServiceImpl implements LaboratorioManagementService {
   @Autowired
    private FirebaseInitializer firebase;
   
    @Override
    public Boolean crearPdf(){
        List<PostDTO> response = new ArrayList<>();
        PostDTO post;
        
        Document documento= new Document();
    
            String ruta = System.getProperty("user.home");
        try {
            PdfWriter.getInstance(documento, new FileOutputStream(ruta + "/Downloads/Prueba.pdf"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PracticaManagementServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DocumentException ex) {
            Logger.getLogger(PracticaManagementServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
            documento.open();
            
            PdfPTable tabla = new PdfPTable(3);
            tabla.addCell("id");
            tabla.addCell("titulo");
            tabla.addCell("contenido");
            
            
        
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = getCollection("post").get();
        
      
        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                post = doc.toObject(PostDTO.class);
                post.setId(doc.getId());
                response.add(post);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(PracticaManagementServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(PracticaManagementServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        for(int i =0; i<response.size();i++){
                tabla.addCell(response.get(i).getId());        
                tabla.addCell(response.get(i).getTitle());
                tabla.addCell(response.get(i).getContent());
                
        }       
        try { 
            documento.add(tabla);
        } catch (DocumentException ex) {
            Logger.getLogger(PracticaManagementServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
         documento.close();
        
     return Boolean.TRUE;
    }
        
   
    @Override
    public Boolean insertarHorario(String idFranjaHoraria, String idGrupo) {
        Map<String, Object> docData = new HashMap<>();
        docData.put("idFranjaHoraria", idFranjaHoraria);
        docData.put("idGrupo", idGrupo);
       
        ApiFuture<WriteResult> writeResultApiFuture = getCollection("AGENDA").document().create(docData);

        try {
            if (null != writeResultApiFuture.get()) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } catch (Exception e) {
            return Boolean.FALSE;
        } 
    }
    @Override
    public Boolean buscarHorario(String idFranjaHoraria, String idGrupo) {
      ApiFuture<QuerySnapshot> querySnapshotApiFutur =firebase.getFirestore().collection("AGENDA").whereEqualTo("idFranjaHoraria",idFranjaHoraria).whereEqualTo("idGrupo", idGrupo).get();
      
      try {
           if(querySnapshotApiFutur.get().isEmpty()){
               return false;
           }
       } catch (InterruptedException ex) {
           Logger.getLogger(LaboratorioManagementServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
       } catch (ExecutionException ex) {
           Logger.getLogger(LaboratorioManagementServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
       }
      return true;
       
    }
    


    @Override
    public Boolean insertarProblema(String idLaboratorio, String problema) {
       Map<String, Object> docData = new HashMap<>();
       docData.put("idLaboratorio", idLaboratorio);
       docData.put("problema", problema);
        ApiFuture<WriteResult> writeResultApiFuture = getCollection("PROBLEMAS").document().create(docData);

        try {
            if (null != writeResultApiFuture.get()) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } catch (Exception e) {
            return Boolean.FALSE;
        } 
    }

    @Override
    public Boolean finalizarPractica(int codGrupal) {
        
        String id= buscarGrupo(codGrupal);
        
        if(id.equals("no existe")){
              return false;
        }else{
        ApiFuture<WriteResult> writeResultApiFuture = getCollection("GRUPO").document(id).delete();
        try {
            if (null != writeResultApiFuture.get()) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } catch (Exception e) {
            return Boolean.FALSE;
        } 
        }
    }
    private String buscarGrupo(int codGrupal){
       GrupoDTO grupo;
       ApiFuture<QuerySnapshot> querySnapshotApiFuture =firebase.getFirestore().collection("GRUPO").whereEqualTo("codGrupal",codGrupal).get();
     
       try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                grupo = doc.toObject(GrupoDTO.class);
                grupo.setId(doc.getId());
                String id=grupo.getId();
                return id;
            }
            return "no existe";
        } catch (Exception e) {
            return "nullo";
        } 
    }
    private CollectionReference getCollection(String Colecion){
        return firebase.getFirestore().collection(Colecion);
    }

}
