package com.example.audiolibros

import android.app.Application
import android.graphics.Bitmap
import android.util.LruCache
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.Volley

class Aplicacion : Application() {

    lateinit var listaLibros: List<Libro>
        private set
    lateinit var adaptador: AdaptadorLibrosFiltro
        private set
    lateinit var colaPeticiones: RequestQueue
        private set
    lateinit var lectorImagenes: ImageLoader
        private set

    override fun onCreate() {
        super.onCreate()
        listaLibros = ejemploLibros()
        adaptador = AdaptadorLibrosFiltro(this, listaLibros as MutableList<Libro>)
        colaPeticiones = Volley.newRequestQueue(this)
        lectorImagenes = ImageLoader(colaPeticiones,
                object : ImageLoader.ImageCache {
                    private val cache = LruCache<String, Bitmap>(10)

                    override fun putBitmap(url: String, bitmap: Bitmap) {
                        cache.put(url, bitmap)
                    }

                    override fun getBitmap(url: String): Bitmap? = cache.get(url)
                })
    }

}
