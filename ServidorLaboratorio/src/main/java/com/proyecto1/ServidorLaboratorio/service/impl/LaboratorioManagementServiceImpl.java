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
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
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
            PdfWriter.getInstance(documento, new FileOutputStream(ruta + "/Desktop/Prueba.pdf"));
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
            
            
        
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = getCollection().get();
        
      
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
        
    private CollectionReference getCollection(){
        return firebase.getFirestore().collection("post");
    }
}
