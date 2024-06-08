package RecyclerViewHelper

import Modelo.ClaseConexion
import Modelo.DataClassTicket
import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import edenilson.amaya.evaluacionedenilson.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID


data class Adaptador(private var Datos: List<DataClassTicket>) : RecyclerView.Adapter<ViewHolder>() {

    fun actualizarLista(nuevaLista:List<DataClassTicket>){
        Datos = nuevaLista
        notifyDataSetChanged()
    }

    fun actualizarListaDespuesDeActualizarDatos(uuid: String,nuevoTitulo:String){
        val index = Datos.indexOfFirst { it.uuid == uuid }
        Datos[index].titulo = nuevoTitulo
        notifyItemChanged(index)
    }

    fun eliminarRegistro(titulo: String,posicion: Int){

        val listaDatos =Datos.toMutableList()
        listaDatos.removeAt(posicion)

        GlobalScope.launch(Dispatchers.IO){
            val objConexion = ClaseConexion().cadenaConexion()
            val delTickets = objConexion?.prepareStatement("delete tbTicket where Titulo = ?")!!
            delTickets.setString(1,titulo)
            delTickets.executeUpdate()

            val commit = objConexion.prepareStatement("commit")
            commit.executeUpdate()
        }

        Datos = listaDatos.toList()
        notifyItemRemoved(posicion)
        notifyDataSetChanged()
    }

    fun actualizarTickets(titulo: String,uuid: String){
        GlobalScope.launch(Dispatchers.IO){
            val objConexion = ClaseConexion().cadenaConexion()
            val updateProducto = objConexion?.prepareStatement("update tbTicket set Titulo = ? where uuid = ?")!!
            updateProducto.setString(1,titulo)
            updateProducto.setString(2,uuid)
            updateProducto.executeUpdate()

            val commit = objConexion?.prepareStatement("commit")!!
            commit.executeUpdate()

            withContext(Dispatchers.Main){
                actualizarListaDespuesDeActualizarDatos(uuid,titulo)
            }
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHelper.ViewHolder {
        val vista =
            LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return ViewHolder(vista)
    }

    override fun getItemCount() = Datos.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ticket = Datos[position]
        holder.textView.text = ticket.titulo

        val item = Datos[position]

        holder.imgBorrar.setOnClickListener {
            val context =  holder.itemView.context

            val builder = AlertDialog.Builder(context)

            builder.setTitle("¿Estas seguro?")

            builder.setMessage("¿Desea eliminar el ticket?")


            builder.setNegativeButton("No"){dialog,which ->

            }

            builder.setPositiveButton("Si"){dialog,which ->
                eliminarRegistro(item.titulo, position)
            }



            val alertDialog = builder.create()

            alertDialog.show()
        }

        holder.imgEditar.setOnClickListener {
            val context = holder.itemView.context
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Editar nombre")

            val nuevoTitulo = EditText(context)
            nuevoTitulo.setHint(item.titulo)
            builder.setView(nuevoTitulo)

            builder.setPositiveButton("Actualizar"){dialog,wich ->
                actualizarTickets(nuevoTitulo.text.toString(),item.uuid)
            }

            builder.setNegativeButton("Cancelar"){dialog,wich ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }




    }
}