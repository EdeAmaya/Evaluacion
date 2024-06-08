package edenilson.amaya.evaluacionedenilson

import Modelo.ClaseConexion
import Modelo.DataClassTicket
import RecyclerViewHelper.Adaptador
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class Ticket : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ticket)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtTitulo = findViewById<EditText>(R.id.txtTitulo)
        val txtDescripcion = findViewById<EditText>(R.id.txtDescripcion)
        val txtAutor = findViewById<EditText>(R.id.txtAutor)
        val txtEmail = findViewById<EditText>(R.id.txtEmail)
        val txtFechaCreacion = findViewById<EditText>(R.id.txtFechaC)
        val txtEstado = findViewById<EditText>(R.id.txtEstado)
        val txtFechaFinalizacion = findViewById<EditText>(R.id.txtFechaF)
        val btnEnviar = findViewById<Button>(R.id.btnEnviar)

        fun  limpiar(){
            txtTitulo.setText("")
            txtDescripcion.setText("")
            txtAutor.setText("")
            txtEmail.setText("")
            txtFechaCreacion.setText("")
            txtEstado.setText("")
            txtFechaFinalizacion.setText("")
        }


        val rcvTicket = findViewById<RecyclerView>(R.id.rcvTicket)
        rcvTicket.layoutManager = LinearLayoutManager(this)

        fun obtenerDatos(): List<DataClassTicket>{
            val objConexion = ClaseConexion().cadenaConexion()

            val statement = objConexion?.createStatement()
            val resultset = statement?.executeQuery("select * from tbTicket")!!


            val tickets = mutableListOf<DataClassTicket>()
            while (resultset.next()){
                val uuid = resultset.getString("uuid")
                val titulo = resultset.getString("titulo")
                val descripcion = resultset.getString("descripcion")
                val autor = resultset.getString("autor")
                val email = resultset.getString("email")
                val fechaCreacion = resultset.getString("fechaCreacion")
                val estado = resultset.getString("estado")
                val fechaFinalizacion = resultset.getString("fechaFinalizacion")
                val ticket = DataClassTicket(uuid,titulo,descripcion,autor,email,fechaCreacion,estado,fechaFinalizacion)
                tickets.add(ticket)
            }
            return tickets
        }


        CoroutineScope(Dispatchers.IO).launch {
            val ticketsDB = obtenerDatos()
            withContext(Dispatchers.Main){
                val miAdapter = Adaptador(ticketsDB)
                rcvTicket.adapter = miAdapter
            }
        }



        btnEnviar.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO){
                val claseConexion = ClaseConexion().cadenaConexion()

                val addTicket = claseConexion?.prepareStatement("insert into tbTicket(uuid,Titulo,Descripcion,Autor,Email,FechaCreacion,Estado,FechaFinalizacion)values(?,?,?,?,?,?,?,?)")!!
                addTicket.setString(1, UUID.randomUUID().toString())
                addTicket.setString(2, txtTitulo.text.toString())
                addTicket.setString(3, txtDescripcion.text.toString())
                addTicket.setString(4, txtAutor.text.toString())
                addTicket.setString(5, txtEmail.text.toString())
                addTicket.setString(6, txtFechaCreacion.text.toString())
                addTicket.setString(7, txtEstado.text.toString())
                addTicket.setString(8, txtFechaFinalizacion.text.toString())
                addTicket.executeUpdate()

                val nuevosTickets = obtenerDatos()

                withContext(Dispatchers.Main){
                    (rcvTicket.adapter as? Adaptador)?.actualizarLista(nuevosTickets)
                }

                withContext(Dispatchers.Main){
                    limpiar()
                }


            }

        }


    }
}