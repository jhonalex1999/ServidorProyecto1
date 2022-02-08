package com.proyecto1.ServidorLaboratorio.service.impl;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.proyecto1.ServidorLaboratorio.dto.PostDTO;
import com.proyecto1.ServidorLaboratorio.firebase.FirebaseInitializer;
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
import com.proyecto1.ServidorLaboratorio.dto.PracticaDTO;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.proyecto1.ServidorLaboratorio.service.PracticaManagementService;

@Service
public class PracticaManagementServiceImpl implements PracticaManagementService {

    @Autowired
    private FirebaseInitializer firebase;

    @Override
    public List<PracticaDTO> listarPracticas() {
        List<PracticaDTO> response = new ArrayList<>();
        PracticaDTO post;

        ApiFuture<QuerySnapshot> querySnapshotApiFuture = getCollection("PRACTICA").get();

        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                post = doc.toObject(PracticaDTO.class);
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

        ApiFuture<WriteResult> writeResultApiFuture = getCollection("post").document().create(docData);

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

        ApiFuture<WriteResult> writeResultApiFuture = getCollection("post").document(id).set(docData);
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
        ApiFuture<WriteResult> writeResultApiFuture = getCollection("post").document(id).delete();
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

    private Map<String, Object> getDocData(PostDTO post) {
        Map<String, Object> docData = new HashMap<>();
        docData.put("title", post.getTitle());
        docData.put("content", post.getContent());
        return docData;
    }


}
