package com.example.audiolibros

import android.content.Context
import android.support.v7.graphics.Palette
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.android.volley.VolleyError
import com.android.volley.toolbox.ImageLoader

open class AdaptadorLibros(private val contexto: Context, protected var listaLibros: List<Libro> //Vector con libros a visualizar
) : RecyclerView.Adapter<AdaptadorLibros.ViewHolder>() {
    private val inflador: LayoutInflater      //Crea Layouts a partir del XML
    private var onClickListener: View.OnClickListener? = null
    private var onLongClickListener: View.OnLongClickListener? = null

    init {
        inflador = contexto
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    //Creamos nuestro ViewHolder, con los tipos de elementos a modificar
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var portada: ImageView
        var titulo: TextView

        init {
            portada = itemView.findViewById<View>(R.id.portada) as ImageView
            //portada.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            titulo = itemView.findViewById<View>(R.id.titulo) as TextView
        }
    }

    // Creamos el ViewHolder con las vista de un elemento sin personalizar
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflamos la vista desde el xml
        val v = inflador.inflate(R.layout.elemento_selector, null)
        v.setOnClickListener(onClickListener)
        v.setOnLongClickListener(onLongClickListener)
        return ViewHolder(v)
    }

    // Usando como base el ViewHolder y lo personalizamos
    override fun onBindViewHolder(holder: ViewHolder, posicion: Int) {
        val (titulo, _, urlImagen) = listaLibros[posicion]
        //holder.portada.setImageResource(libro.recursoImagen);
        holder.titulo.text = titulo
        val aplicacion = contexto.applicationContext as Aplicacion
        aplicacion.lectorImagenes?.get(urlImagen,
                object : ImageLoader.ImageListener {
                    override fun onResponse(response: ImageLoader.ImageContainer, isImmediate: Boolean) {
                        val bitmap = response.bitmap
                        if (bitmap != null) {

                            holder.portada.setImageBitmap(bitmap)

                            //Extraemos el color principal de un bitmap
                            val palette = Palette.from(bitmap).generate()
                            holder.itemView.setBackgroundColor(palette.getLightMutedColor(0))
                            holder.titulo.setBackgroundColor(palette.getLightVibrantColor(0))

                            holder.portada.invalidate()
                        }

                    }

                    override fun onErrorResponse(error: VolleyError) {
                        holder.portada.setImageResource(R.drawable.books)
                    }
                })

    }

    // Indicamos el número de elementos de la lista
    override fun getItemCount(): Int {
        return listaLibros.size
    }

    fun setOnItemClickListener(onClickListener: View.OnClickListener) {
        this.onClickListener = onClickListener
    }

    fun setOnItemLongClickListener(onLongClickListener: View.OnLongClickListener) {
        this.onLongClickListener = onLongClickListener
    }

}
