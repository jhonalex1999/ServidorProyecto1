package com.proyecto1.ServidorLaboratorio.service.impl;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.proyecto1.ServidorLaboratorio.dto.PostDTO;
import com.proyecto1.ServidorLaboratorio.firebase.FirebaseInitializer;
import com.proyecto1.ServidorLaboratorio.service.PostManagementService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class PostManagementServiceImpl implements PostManagementService {

    @Autowired
    private FirebaseInitializer firebase;

    @Override
    public List<PostDTO> list() {
        List<PostDTO> response = new ArrayList<>();
        PostDTO post;
        
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = getCollection().get();
        
        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                post = doc.toObject(PostDTO.class);
                post.setId(doc.getId());
                response.add(post);
            }
            return response;
        } catch (Exception e) {
            return null;
        }   
    }

    @Override
    public Boolean add(PostDTO post) {
        Map<String, Object> docData = getDocData(post);

        ApiFuture<WriteResult> writeResultApiFuture = getCollection().document().create(docData);

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
    public Boolean edit(String id, PostDTO post) {
        Map<String, Object> docData = getDocData(post);
        
        ApiFuture<WriteResult> writeResultApiFuture = getCollection().document(id).set(docData);
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
    public Boolean delete(String id) {
         ApiFuture<WriteResult> writeResultApiFuture = getCollection().document(id).delete();
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
    public Boolean crearPdf(){
        List<PostDTO> response = new ArrayList<>();
        PostDTO post;
        
        Document documento= new Document();
    
            String ruta = System.getProperty("user.home");
        try {
            PdfWriter.getInstance(documento, new FileOutputStream(ruta + "/Desktop/Prueba.pdf"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PostManagementServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DocumentException ex) {
            Logger.getLogger(PostManagementServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(PostManagementServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(PostManagementServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        for(int i =0; i<response.size();i++){
                tabla.addCell(response.get(i).getId());        
                tabla.addCell(response.get(i).getTitle());
                tabla.addCell(response.get(i).getContent());
                
        }       
        try { 
            documento.add(tabla);
        } catch (DocumentException ex) {
            Logger.getLogger(PostManagementServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
         documento.close();
        
     return Boolean.TRUE;
    }
    
    private CollectionReference getCollection(){
        return firebase.getFirestore().collection("post");
    }
    
    private Map<String, Object> getDocData(PostDTO post){
        Map<String, Object> docData = new HashMap<>();
        docData.put("title", post.getTitle());
        docData.put("content", post.getContent());
        return docData;
    } 
  
}
