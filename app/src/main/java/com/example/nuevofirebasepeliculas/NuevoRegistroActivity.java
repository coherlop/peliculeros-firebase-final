package com.example.nuevofirebasepeliculas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nuevofirebasepeliculas.clases.Pelicula;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NuevoRegistroActivity extends AppCompatActivity {
    FirebaseDatabase database;
    private EditText edt_reg_email, edt_reg_clave;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_registro);

        edt_reg_email = (EditText)findViewById(R.id.edt_reg_email);
        edt_reg_clave = (EditText)findViewById(R.id.edt_reg_clave);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance(); // te conecta a la base de datos
        // CARGAR PELICULAS
        DatabaseReference myRefPeliculas2 = database.getReference("Peliculashashmap");
        myRefPeliculas2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> keys = new ArrayList<>();
                List<Pelicula> Peliculas = new ArrayList<>();
                for (DataSnapshot keynode : snapshot.getChildren()) {
                    keys.add(keynode.getKey());
                    Peliculas.add(keynode.getValue(Pelicula.class));
                }
                for (String k : keys) {
                    System.out.println(k);
                    Log.i("firebase1", "clave leida " + k);
                }
                for (Pelicula a : Peliculas) {
                    System.out.println(a.toString());
                    Log.i("firebase1", "pelicula leida " + a.toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.i( "firebase1", String.valueOf(error.toException()));
            }
        });
    }

    public void registro(View view) {
        String email = String.valueOf(edt_reg_email.getText()).trim();
        String password = String.valueOf(edt_reg_clave.getText());
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.i("firebase1", "Registro exitoso");
                            Toast.makeText(NuevoRegistroActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(NuevoRegistroActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Log.i("firebase1", "Registro fallido", task.getException());
                            Toast.makeText(NuevoRegistroActivity.this, "Registro fallido", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}