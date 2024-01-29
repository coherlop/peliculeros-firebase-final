package com.example.nuevofirebasepeliculas;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nuevofirebasepeliculas.clases.Pelicula;
import com.example.nuevofirebasepeliculas.recyclerview.ListaPeliculasAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MostrarPeliculas extends AppCompatActivity {
    //atributos--------------------------
    private RecyclerView rv_Peliculas = null;
    private DatabaseReference myRefPeliculas, myRefPeliculas1 = null;
    private ArrayList<Pelicula> Peliculas;
    private EditText edt_buscar_titulo1;
    public static int PETICION1 = 1;
    private FirebaseAuth mAuth;
    private ListaPeliculasAdapter adaptadorPeliculas = null;

    //metodos---------------------------
    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
        else{
            Toast.makeText(this, "Debe iniciar sesión", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_peliculas);
        rv_Peliculas = (RecyclerView) findViewById(R.id.rv_Peliculas);
        edt_buscar_titulo1 = (EditText) findViewById(R.id.edt_buscar_titulo2);
        mAuth = FirebaseAuth.getInstance();
        Peliculas = new ArrayList<>();
        adaptadorPeliculas = new ListaPeliculasAdapter(this,Peliculas);
        rv_Peliculas.setAdapter(adaptadorPeliculas);
        myRefPeliculas = FirebaseDatabase.getInstance().getReference("Peliculashashmap");
        myRefPeliculas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                adaptadorPeliculas.getPeliculas().clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Pelicula a = (Pelicula) dataSnapshot.getValue(Pelicula.class);
                    Peliculas.add(a);
                }
                    adaptadorPeliculas.setPeliculas(Peliculas);
                    adaptadorPeliculas.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.i( "firebase1", String.valueOf(error.toException()));
            }
        });

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // In landscape
            rv_Peliculas.setLayoutManager(new GridLayoutManager(this,2));
        } else {
            // In portrait
            rv_Peliculas.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    public void addPelicula(View view) {
        Intent intent = new Intent(this, AddPeliculaActivity.class);
        startActivity(intent);
    }
    public void buscarPeliculas1(View view) {
        String texto = String.valueOf(edt_buscar_titulo1.getText());
        myRefPeliculas1 = FirebaseDatabase.getInstance().getReference("Peliculashashmap");
        myRefPeliculas1.addValueEventListener(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> keys1 = new ArrayList<String>();
                ArrayList<Pelicula> Peliculas1 = new ArrayList<Pelicula>();
                for (DataSnapshot keynode : snapshot.getChildren()) {
                    keys1.add(keynode.getKey());
                    Pelicula a = keynode.getValue(Pelicula.class);
                    if(a.getTitulo().toLowerCase().contains(texto.toLowerCase())) {
                        Peliculas1.add(keynode.getValue(Pelicula.class));
                    }
                }
                adaptadorPeliculas.setPeliculas(Peliculas1);
                adaptadorPeliculas.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.i( "firebase1", String.valueOf(error.toException()));
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PETICION1 && resultCode == Activity.RESULT_OK) {
            int posicion = data.getIntExtra(ModificarPeliculaActivity.EXTRA_POSICION_DEVUELTA,-1);
            String tipo = data.getStringExtra(ModificarPeliculaActivity.EXTRA_TIPO);
           if(tipo.equalsIgnoreCase("edicion"))
           {
               adaptadorPeliculas.notifyItemChanged(posicion);
               adaptadorPeliculas.notifyDataSetChanged();
           }
           else if(tipo.equalsIgnoreCase("borrado"))
           {
               adaptadorPeliculas.notifyItemRemoved(posicion);
               adaptadorPeliculas.notifyDataSetChanged();
           }
           else{
               adaptadorPeliculas.notifyDataSetChanged();
           }
        }
    }

    @Override
    protected void onRestart() {
        this.recreate();
        super.onRestart();
    }

    //metodo para evitar que se pulse el botón atrás
    public void onBackPressed(){
    }

    //para refrescar el activity manualmente si firebase tarda en cargar las imágenes
    public void refrescar(View view) {
        finish();
        startActivity(getIntent());
        this.overridePendingTransition(0, 0);
    }

}