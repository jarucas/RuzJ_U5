package com.example.audiolibros.fragments

import android.app.Activity
import android.app.AlertDialog
import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.*
import com.example.audiolibros.AdaptadorLibrosFiltro
import com.example.audiolibros.Aplicacion
import com.example.audiolibros.MainActivity
import com.example.audiolibros.R

class SelectorFragment : Fragment() {
    private var actividad: Activity? = null
    private var recyclerView: RecyclerView? = null
    private var adaptador: AdaptadorLibrosFiltro? = null
    //private Vector<Libro> vectorLibros;

    override fun onAttach(actividad: Activity) {
        super.onAttach(actividad)
        this.actividad = actividad
        val app = actividad.application as Aplicacion
        adaptador = app.adaptador
        //Vector<Libro> vectorLibros = app.getVectorLibros();
    }

    override fun onCreateView(inflador: LayoutInflater, contenedor: ViewGroup?, savedInstanceState: Bundle?): View? {
        val vista = inflador.inflate(R.layout.fragment_selector,
                contenedor, false)
        recyclerView = vista.findViewById<View>(
                R.id.recycler_view) as RecyclerView
        recyclerView!!.layoutManager = GridLayoutManager(actividad, 2)
        recyclerView!!.adapter = adaptador
        adaptador!!.setOnItemClickListener(View.OnClickListener { v ->
            (actividad as MainActivity).mostrarDetalle(
                    adaptador!!.getItemId(
                            recyclerView!!.getChildAdapterPosition(v)).toInt())
        })

        adaptador!!.setOnItemLongClickListener(View.OnLongClickListener { v ->
            val id = recyclerView!!.getChildAdapterPosition(v)
            val menu = AlertDialog.Builder(actividad)
            val opciones = arrayOf<CharSequence>("Compartir", "Borrar ", "Insertar")
            menu.setItems(opciones) { dialog, opcion ->
                when (opcion) {
                    0 //Compartir
                    -> {
                        //Libro libro = vectorLibros.elementAt(id);
                        val (titulo, _, _, urlAudio) = adaptador!!.getItem(id) //Faltaba esta línea
                        val i = Intent(Intent.ACTION_SEND)
                        i.type = "text/plain"
                        i.putExtra(Intent.EXTRA_SUBJECT, titulo)
                        i.putExtra(Intent.EXTRA_TEXT, urlAudio)
                        startActivity(Intent.createChooser(i, "Compartir"))
                    }
                    1 //Borrar
                    -> Snackbar.make(v, "¿Estás seguro?", Snackbar.LENGTH_LONG)
                            .setAction("SI") {
                                adaptador!!.borrar(id)
                                adaptador!!.notifyDataSetChanged()
                            }
                            .show()
                    2 //Insertar
                    -> {
                        val posicion = recyclerView!!.getChildLayoutPosition(v)
                        adaptador!!.insertar(adaptador!!.getItem(posicion))
                        adaptador!!.notifyDataSetChanged()
                        Snackbar.make(v, "Libro insertado", Snackbar.LENGTH_INDEFINITE)
                                .setAction("OK") { }
                                .show()
                    }
                }//Faltaba esta línea
            }
            menu.create().show()
            true
        })
        setHasOptionsMenu(true)
        return vista
    }

    override fun onResume() {
        (activity as MainActivity).mostrarElementos(true)
        super.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_selector, menu)
        val searchItem = menu.findItem(R.id.menu_buscar)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(
                object : SearchView.OnQueryTextListener {
                    override fun onQueryTextChange(query: String): Boolean {
                        adaptador!!.setBusqueda(query)
                        adaptador!!.notifyDataSetChanged()
                        return false
                    }

                    override fun onQueryTextSubmit(query: String): Boolean {
                        return false
                    }
                })
        MenuItemCompat.setOnActionExpandListener(searchItem,
                object : MenuItemCompat.OnActionExpandListener {
                    override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                        adaptador!!.setBusqueda("")
                        adaptador!!.notifyDataSetChanged()
                        return true  // Para permitir cierre
                    }

                    override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                        return true  // Para permitir expansión
                    }
                })
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.menu_ultimo) {
            (actividad as MainActivity).irUltimoVisitado()
            return true
        } else if (id == R.id.menu_buscar) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}