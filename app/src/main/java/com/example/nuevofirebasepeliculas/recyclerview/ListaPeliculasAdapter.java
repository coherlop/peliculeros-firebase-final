package com.example.nuevofirebasepeliculas.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.nuevofirebasepeliculas.R;
import com.example.nuevofirebasepeliculas.clases.Pelicula;
import com.example.nuevofirebasepeliculas.utilidades.ImagenesFirebase;
import java.util.ArrayList;

public class ListaPeliculasAdapter extends RecyclerView.Adapter<PeliculaViewHolder> {
    //constructores----------------
    public ListaPeliculasAdapter(Context contexto, ArrayList<Pelicula> Peliculas ) {
        this.contexto = contexto;
        this.Peliculas = Peliculas;
        inflate =  LayoutInflater.from(this.contexto);
    }

    // atributos----------------
    private Context contexto = null;
    private ArrayList<Pelicula> Peliculas = null;
    private LayoutInflater inflate = null;

    //setters--------------
    public void setPeliculas(ArrayList<Pelicula> Peliculas) {
        this.Peliculas = Peliculas;
        notifyDataSetChanged();
    }

    //getters----------------

    public Context getContexto() {
        return contexto;
    }

    public ArrayList<Pelicula> getPeliculas() {
        return Peliculas;
    }


    //metodos---------------

    @NonNull
    @Override
    public PeliculaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = inflate.inflate(R.layout.item_rv_pelicula,parent,false);

        PeliculaViewHolder evh = new PeliculaViewHolder(mItemView,this);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull PeliculaViewHolder holder, int position) {
        Pelicula p = this.getPeliculas().get(position);
        // cargo la imagen desde la base de datos
        String carpeta = p.getTitulo();
        ImageView imagen = holder.getImg_item_pelicula() ;
        ImagenesFirebase.descargarFoto(carpeta,p.getTitulo(),imagen);
        ImageView imagen1 = imagen;
        holder.setImg_item_pelicula(imagen1);
        holder.getTxt_item_titulo().setText(p.getTitulo()); //titulo mostrado en la card
        holder.getTxt_item_genero().setText(String.valueOf(p.getGenero())); //genero mostrado en la card
    }

    @Override
    public int getItemCount() {
        return this.Peliculas.size();
    }

}
