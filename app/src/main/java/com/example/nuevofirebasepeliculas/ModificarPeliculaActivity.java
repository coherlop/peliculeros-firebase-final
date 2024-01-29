package com.example.nuevofirebasepeliculas;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nuevofirebasepeliculas.clases.Pelicula;
import com.example.nuevofirebasepeliculas.recyclerview.PeliculaViewHolder;
import com.example.nuevofirebasepeliculas.utilidades.ImagenesBlobBitmap;
import com.example.nuevofirebasepeliculas.utilidades.ImagenesFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;


public class ModificarPeliculaActivity extends AppCompatActivity {
    //atributos--------------------------
    public static final String EXTRA_POSICION_DEVUELTA =  "com.example.nuevofirebasepeliculas.detallespeliculaactivity.posicion";
    public static final String EXTRA_TIPO = "com.example.nuevofirebasepeliculas.detallespeliculaactivity.tipo";
    EditText edt_detalles_titulo, edt_detalles_genero = null;
    String id_antiguo ="";
    int posicion = -1;
    public static final int NUEVA_IMAGEN = 1;
    Uri imagen_seleccionada = null;
    ImageView img_detalles_foto_pelicula = null;
    private FirebaseAuth mAuth;

    //metodos----------------------------

    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
        else{
            Toast.makeText(this, "Debes autenticarte primero", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_pelicula);
        edt_detalles_titulo = (EditText) findViewById(R.id.edt_detalles_titulo);
        edt_detalles_genero = (EditText) findViewById(R.id.edt_detalles_genero);
        img_detalles_foto_pelicula = (ImageView) findViewById(R.id.img_detalles_foto_pelicula);
        Intent intent = getIntent();
        if(intent != null)
        {
            Pelicula p = (Pelicula)intent.getSerializableExtra(PeliculaViewHolder.EXTRA_PELICULA_ITEM);
            edt_detalles_titulo.setText(p.getTitulo());
            edt_detalles_genero.setText(p.getGenero());
            id_antiguo = p.getTitulo();
            //cargo la foto
            byte[] fotobinaria = (byte[]) intent.getByteArrayExtra(PeliculaViewHolder.EXTRA_PELICULA_IMAGEN);
            Bitmap fotobitmap = ImagenesBlobBitmap.bytes_to_bitmap(fotobinaria, 200,200);
            img_detalles_foto_pelicula.setImageBitmap(fotobitmap);
            // obtengo la posicion
            posicion = intent.getIntExtra(PeliculaViewHolder.EXTRA_POSICION_CASILLA,-1);
        }

    }

    //--------------------------------------------------------------------------
    //--------CODIGO PARA CAMBIAR LA IMAGEN----------------
    public void cambiar_imagen(View view) {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Selecciona una imagen");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});
        startActivityForResult(chooserIntent, NUEVA_IMAGEN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NUEVA_IMAGEN && resultCode == Activity.RESULT_OK) {
            imagen_seleccionada = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imagen_seleccionada);
                img_detalles_foto_pelicula.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void detalles_borrar_pelicula(View view) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        String titulo = String.valueOf(edt_detalles_titulo.getText());
        String genero = String.valueOf(edt_detalles_genero.getText());
        Pelicula a = new Pelicula(titulo, genero);

        if(id_antiguo.equalsIgnoreCase(titulo))
        {
            myRef.child("Peliculashashmap").child(id_antiguo).removeValue();
            Toast.makeText(this,"pelicula borrado correctamente",Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this,"no se pudo borrar el pelicula",Toast.LENGTH_LONG).show();
        }

        // borramos la imagen del firebase store
        String carpeta = a.getTitulo();
        ImagenesFirebase.borrarFoto(carpeta,a.getTitulo());
        // cerramos la ventana y volvemos al recyclerview
        Intent replyIntent = new Intent();
        replyIntent.putExtra(EXTRA_POSICION_DEVUELTA, posicion);
        replyIntent.putExtra(EXTRA_TIPO, "borrado");
        setResult(RESULT_OK, replyIntent);
               finish();
    }
    public void detalles_editar_pelicula(View view) {
        String titulo = String.valueOf(edt_detalles_titulo.getText());
        String genero = String.valueOf(edt_detalles_genero.getText());
        Pelicula a = new Pelicula(titulo, genero);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child("Peliculashashmap").child(id_antiguo).removeValue();
        myRef.child("Peliculashashmap").child(a.getTitulo()).setValue(a);
        Toast.makeText(this,"pelicula editado correctamente",Toast.LENGTH_LONG).show();

        if(imagen_seleccionada != null || !id_antiguo.equalsIgnoreCase(a.getTitulo())) {
            String carpeta = a.getTitulo();
            ImagenesFirebase.borrarFoto(id_antiguo,id_antiguo);
            ImagenesFirebase.subirFoto(carpeta,a.getTitulo(), img_detalles_foto_pelicula);
        }
        Intent replyIntent = new Intent();
        replyIntent.putExtra(EXTRA_POSICION_DEVUELTA, posicion);
        replyIntent.putExtra(EXTRA_TIPO, "edicion");
        setResult(RESULT_OK, replyIntent);
        finish();

    }


}