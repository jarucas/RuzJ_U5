package com.example.audiolibros

import android.content.Context
import java.util.*

class AdaptadorLibrosFiltro(contexto: Context,
                            private val vectorSinFiltro: MutableList<Libro>// Vector con todos los libros
) : AdaptadorLibros(contexto, vectorSinFiltro) {
    private lateinit var indiceFiltro: MutableList<Int>
    private var busqueda = ""         // Búsqueda sobre autor o título
    private var genero = ""           // Género seleccionado
    private var novedad = false      // Si queremos ver solo novedades
    private var leido = false        // Si queremos ver solo leidos

    init {
        recalculaFiltro()
    }

    fun setBusqueda(busqueda: String) {
        this.busqueda = busqueda.toLowerCase()
        recalculaFiltro()
    }

    fun setGenero(genero: String) {
        this.genero = genero
        recalculaFiltro()
    }

    fun setNovedad(novedad: Boolean) {
        this.novedad = novedad
        recalculaFiltro()
    }

    fun setLeido(leido: Boolean) {
        this.leido = leido
        recalculaFiltro()
    }

    fun recalculaFiltro() {
        listaLibros = Vector()
        indiceFiltro = Vector()
        for (i in vectorSinFiltro.indices) {
            val libro = vectorSinFiltro[i]
            if ((libro.titulo.toLowerCase().contains(busqueda) || libro.autor.toLowerCase().contains(busqueda))
                    && libro.genero.startsWith(genero)
                    && (!novedad || novedad && libro.novedad)
                    && (!leido || leido && libro.leido)) {
                (listaLibros as Vector<Libro>).add(libro)
                indiceFiltro.add(i)
            }
        }
    }

    fun getItem(posicion: Int): Libro = vectorSinFiltro[indiceFiltro[posicion]]

    override fun getItemId(posicion: Int): Long = indiceFiltro[posicion].toLong()

    fun borrar(posicion: Int) {
        vectorSinFiltro.removeAt(getItemId(posicion).toInt())
        recalculaFiltro()
    }

    fun insertar(libro: Libro) {
        vectorSinFiltro.add(libro)
        recalculaFiltro()
    }
}
