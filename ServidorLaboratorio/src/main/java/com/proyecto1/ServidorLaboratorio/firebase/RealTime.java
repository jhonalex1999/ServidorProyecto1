/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto1.ServidorLaboratorio.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Acl.User;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.proyecto1.ServidorLaboratorio.dto.Variable_CaidaLibreDTO;
import com.proyecto1.ServidorLaboratorio.dto.LaboratorioDTO;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;

/**
 *
 * @author admin
 */
@Service
public class RealTime {

    DataSnapshot Consultas;
    Object Coleccion;
    String peso2;
    DatabaseReference ref;

    @PostConstruct
    private void conectarReal() throws IOException {

        // Fetch the service account key JSON file contents
        FileInputStream serviceAccount = new FileInputStream("src/main/resources/RealTime.json");

        // Initialize the app with a service account, granting admin privileges
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                // The database URL depends on the location of the database
                .setDatabaseUrl("https://plantas-db84a-default-rtdb.firebaseio.com")
                .build();
        FirebaseApp prueba = FirebaseApp.initializeApp(options, "secondary");

        // As an admin, the app has access to read and write all data, regardless of Security Rules
        ref = FirebaseDatabase.getInstance(prueba).getReference();
        
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Consultas = dataSnapshot;
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    public Object consultas(String padre,String hijo) {
         Object document = Consultas.child(padre).child(hijo);
         return document;
    }
    
    public void iniciar(String planta) {
        DatabaseReference hopperRef = ref.child(planta);
        Map<String, Object> hopperUpdates = new HashMap<>();
        hopperUpdates.put("iniciar", true);
        hopperRef.updateChildrenAsync(hopperUpdates);
    }
    
    public void finalizarProceso(String planta){
        DatabaseReference hopperRef = ref.child(planta);
        Map<String, Object> hopperUpdates = new HashMap<>();
        hopperUpdates.put("finalizado", true);
        hopperRef.updateChildrenAsync(hopperUpdates);
    }

}
