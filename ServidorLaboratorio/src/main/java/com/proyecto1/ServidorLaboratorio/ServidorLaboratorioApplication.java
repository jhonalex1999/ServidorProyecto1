package com.proyecto1.ServidorLaboratorio;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServidorLaboratorioApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(ServidorLaboratorioApplication.class, args);
            conectarReal();
        } catch (IOException ex) {
            Logger.getLogger(ServidorLaboratorioApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void conectarReal() throws IOException {
    // Fetch the service account key JSON file contents
        FileInputStream serviceAccount = new FileInputStream("src/main/resources/RealTime.json");

    // Initialize the app with a service account, granting admin privileges
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                // The database URL depends on the location of the database
                .setDatabaseUrl("https://plantas-db84a-default-rtdb.firebaseio.com")
                .build();
        FirebaseApp prueba = FirebaseApp.initializeApp(options,"secondary");

    // As an admin, the app has access to read and write all data, regardless of Security Rules
        DatabaseReference ref = FirebaseDatabase.getInstance(prueba)
                .getReference();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object document = dataSnapshot.getValue();
                System.out.println(document);
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

}
