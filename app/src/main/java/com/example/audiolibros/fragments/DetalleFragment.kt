package com.example.audiolibros.fragments

import android.app.Fragment
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.TextView
import com.android.volley.toolbox.NetworkImageView
import com.example.audiolibros.Aplicacion
import com.example.audiolibros.MainActivity
import com.example.audiolibros.R
import java.io.IOException

class DetalleFragment : Fragment(), View.OnTouchListener, MediaPlayer.OnPreparedListener, MediaController.MediaPlayerControl {
    internal var mediaPlayer: MediaPlayer? = null
    internal lateinit var mediaController: MediaController

    override fun onCreateView(inflador: LayoutInflater, contenedor: ViewGroup?, savedInstanceState: Bundle?): View? {
        val vista = inflador.inflate(R.layout.fragment_detalle,
                contenedor, false)
        val args = arguments
        if (args != null) {
            val position = args.getInt(ARG_ID_LIBRO)
            ponInfoLibro(position, vista)
        } else {
            ponInfoLibro(0, vista)
        }
        return vista
    }

    override fun onResume() {
        val detalleFragment = fragmentManager.findFragmentById(R.id.detalle_fragment) as DetalleFragment?
        if (detalleFragment == null) {
            (activity as MainActivity).mostrarElementos(false)
        }
        super.onResume()
    }

    private fun ponInfoLibro(id: Int, vista: View?) {
        val (titulo, autor, urlImagen, urlAudio) = (activity.application as Aplicacion)
                .listaLibros!![id]
        (vista!!.findViewById<View>(R.id.titulo) as TextView).text = titulo
        (vista.findViewById<View>(R.id.autor) as TextView).text = autor
        //((ImageView) vista.findViewById(R.id.portada)).setImageResource(libro.recursoImagen);
        val aplicacion = activity.application as Aplicacion
        (vista.findViewById<View>(R.id.portada) as NetworkImageView).setImageUrl(
                urlImagen, aplicacion.lectorImagenes)

        vista.setOnTouchListener(this)
        if (mediaPlayer != null) {
            mediaPlayer!!.release()
        }
        mediaPlayer = MediaPlayer()
        mediaPlayer!!.setOnPreparedListener(this)
        mediaController = MediaController(activity)
        val audio = Uri.parse(urlAudio)
        try {
            mediaPlayer!!.setDataSource(activity, audio)
            mediaPlayer!!.prepareAsync()
        } catch (e: IOException) {
            Log.e("Audiolibros", "ERROR: No se puede reproducir $audio", e)
        }

    }

    fun ponInfoLibro(id: Int) {
        ponInfoLibro(id, view)
    }

    override fun onPrepared(mediaPlayer: MediaPlayer) {
        Log.d("Audiolibros", "Entramos en onPrepared de MediaPlayer")
        mediaPlayer.start()
        mediaController.setMediaPlayer(this)
        mediaController.setAnchorView(view!!.findViewById(
                R.id.fragment_detalle))
        //        mediaController.setPadding(0, 0, 0, 110);
        mediaController.isEnabled = true
        mediaController.show()
    }

    override fun onTouch(vista: View, evento: MotionEvent): Boolean {
        mediaController.show()
        return false
    }

    override fun onStop() {
        mediaController.hide()
        try {
            mediaPlayer!!.stop()
            mediaPlayer!!.release()
        } catch (e: Exception) {
            Log.d("Audiolibros", "Error en mediaPlayer.stop()")
        }

        super.onStop()
    }

    override fun canPause(): Boolean {
        return true
    }

    override fun canSeekBackward(): Boolean {
        return true
    }

    override fun canSeekForward(): Boolean {
        return true
    }

    override fun getBufferPercentage(): Int {
        return 0
    }

    override fun getCurrentPosition(): Int {
        try {
            return mediaPlayer!!.currentPosition
        } catch (e: Exception) {
            return 0
        }

    }

    override fun getDuration(): Int {
        return mediaPlayer!!.duration
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer!!.isPlaying
    }

    override fun pause() {
        mediaPlayer!!.pause()
    }

    override fun seekTo(pos: Int) {
        mediaPlayer!!.seekTo(pos)
    }

    override fun start() {
        mediaPlayer!!.start()
    }

    override fun getAudioSessionId(): Int {
        return 0
    }

    companion object {
        var ARG_ID_LIBRO = "id_libro"
    }
}
