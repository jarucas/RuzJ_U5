package com.example.audiolibros

import java.util.ArrayList
import java.util.Vector

class Libro(var titulo: String, var autor: String, //public int recursoImagen;
            var urlImagen: String,
            var urlAudio: String, var genero: String   // Género literario
            , var novedad: Boolean? // Es una novedad
            , var leido: Boolean?   // Leído por el usuario
) {
    companion object {
        val G_TODOS = "Todos los géneros"
        val G_EPICO = "Poema épico"
        val G_S_XIX = "Literatura siglo XIX"
        val G_SUSPENSE = "Suspense"
        val G_ARRAY = arrayOf(G_TODOS, G_EPICO, G_S_XIX, G_SUSPENSE)

        fun ejemploLibros(): List<Libro> {
            val SERVIDOR = "http://mmoviles.upv.es/audiolibros/"
            val libros = ArrayList<Libro>()
            libros.add(Libro("Kappa", "Akutagawa",
                    SERVIDOR + "kappa.jpg", SERVIDOR + "kappa.mp3",
                    Libro.G_S_XIX, false, false))
            libros.add(Libro("Avecilla", "Alas Clarín, Leopoldo",
                    SERVIDOR + "avecilla.jpg", SERVIDOR + "avecilla.mp3",
                    Libro.G_S_XIX, true, false))
            libros.add(Libro("Divina Comedia", "Dante",
                    SERVIDOR + "divina_comedia.jpg", SERVIDOR + "divina_comedia.mp3",
                    Libro.G_EPICO, true, false))
            libros.add(Libro("Viejo Pancho, El", "Alonso y Trelles, José",
                    SERVIDOR + "viejo_pancho.jpg", SERVIDOR + "viejo_pancho.mp3",
                    Libro.G_S_XIX, true, true))
            libros.add(Libro("Canción de Rolando", "Anónimo",
                    SERVIDOR + "cancion_rolando.jpg", SERVIDOR + "cancion_rolando.mp3",
                    Libro.G_EPICO, false, true))
            libros.add(Libro("Matrimonio de sabuesos", "Agata Christie",
                    SERVIDOR + "matrim_sabuesos.jpg", SERVIDOR + "matrim_sabuesos.mp3",
                    Libro.G_SUSPENSE, false, true))
            libros.add(Libro("La iliada", "Homero",
                    SERVIDOR + "la_iliada.jpg", SERVIDOR + "la_iliada.mp3",
                    Libro.G_EPICO, true, false))
            return libros
        }
    }
}