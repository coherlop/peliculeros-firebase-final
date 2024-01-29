package com.example.nuevofirebasepeliculas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nuevofirebasepeliculas.clases.Pelicula;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    //atributos------------------------
    FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private EditText edt_login_email, edt_login_clave;

    @Override
    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edt_login_email = (EditText) findViewById(R.id.edt_reg_email);
        edt_login_clave = (EditText) findViewById(R.id.edt_reg_clave);

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

    public void login(View view) {
        String email = String.valueOf(edt_login_email.getText());
        String password = String.valueOf(edt_login_clave.getText());
        mAuth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.i("firebasedb", "¡Bienvenido!");
                    Toast.makeText(MainActivity.this, "¡Bienvenido!", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    Intent intent = new Intent(MainActivity.this, MostrarPeliculas.class);
                    startActivity(intent);
                } else {
                    Log.i("firebasedb", "Login fallido", task.getException());
                    Toast.makeText(MainActivity.this, "Login fallido", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void cerrarSesion(View view) {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(MainActivity.this, "Sesión cerrada correctamente", Toast.LENGTH_SHORT).show();
    }

    public void mostrarRegistro(View view){
        Intent intent = new Intent(MainActivity.this, NuevoRegistroActivity.class);
        startActivity(intent);
    }

}