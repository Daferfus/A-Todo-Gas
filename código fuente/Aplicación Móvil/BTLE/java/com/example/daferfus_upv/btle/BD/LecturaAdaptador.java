// ----------------------------------------------------------
// David Fernández Fuster
// 2020-10-21
// ----------------------------------------------------------

package com.example.daferfus_upv.btle.BD;

// ------------------------------------------------------------------
// ------------------------------------------------------------------
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.daferfus_upv.btle.R;

import java.util.List;
// ------------------------------------------------------------------
// ------------------------------------------------------------------

public class LecturaAdaptador extends ArrayAdapter<Lectura> {

    private final List<Lectura> lecturas;
    private final Context context;

    // --------------------------------------------------------------
    //                  constructor() <-
    //                  <- Context, N, Lista<Lecturas>
    //
    // Invocado desde: MainActivity
    // Función: Inicializa y configura el adaptador para imprimir lecturas en el layout principal.
    // --------------------------------------------------------------
    public LecturaAdaptador(Context context, int resource, List<Lectura> lecturas) {
        super(context, resource, lecturas);
        this.context = context;
        this.lecturas = lecturas;
    }

    @Override
    public View getView(int posicion, View convertView, ViewGroup parent) {

        // Se coge el servicio para imprimir las lecturas en el layout...
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // ...se busca la vista donde imprimir dichas lecturas y sus subvistas...
        View listView = inflater.inflate(R.layout.listview_mediciones, null, true);
        TextView momentoTextView = (TextView) listView.findViewById(R.id.textViewName);
        ImageView estadoImageView = (ImageView) listView.findViewById(R.id.imageViewStatus);

        // ...se coge la lectura actual...
        Lectura lectura = lecturas.get(posicion);

        // ...y de ella se imprime el momento en el que se tomo dicha lectura...
        momentoTextView.setText(lectura.getMomento());

        // ...y una imagen que representa si esa lectura se encuentra ya en el servidor
        // (esta sincronizada con este).

        // Si no esta sincronizada, imprime el icono de Google grisaceo.
        if (lectura.getEstadoSincronizacionServidor() == 0){
            estadoImageView.setBackgroundResource(R.drawable.googleg_disabled_color_18);
        }
        // De lo contrario, imprime el icono de Google colorido.
        else {
            estadoImageView.setBackgroundResource(R.drawable.googleg_standard_color_18);
        } // if()
        return listView;
    } // ()
} // class
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------