package com.qbo.appcomponentes

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.qbo.appcomponentes.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener, View.OnClickListener {

    private lateinit var binding : ActivityMainBinding
    private var estadoCivil=""

    private var listaPreferencia=ArrayList<String>()

    private val listaPersonas=ArrayList<String>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Adapter es un elemento que nos permite añadir informacion a los elemtos de lista
        ArrayAdapter.createFromResource(
            this,
            R.array.estado_civil_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spEstadoCivil.adapter=adapter
        }
        //metodo para seleccionar el estado civil
        binding.spEstadoCivil.onItemSelectedListener=this

        binding.btnRegistro.setOnClickListener(this)
        binding.btnListar.setOnClickListener(this)

        binding.chDeporte.setOnClickListener(this)
        binding.chCine.setOnClickListener(this)
        binding.chDibujo.setOnClickListener(this)
    }


    //metodo para ir a otro activity y pasar los parametros
    fun irlistaPersonas(){
        val intentLista= Intent(
            this,ListaActivity::class.java
        ).apply {
            putExtra("listaPersonas",listaPersonas)
        }
        startActivity(intentLista)
    }


    fun resgitrarPersona(vista:View){
        if(validarFormulario(vista)){
            val infoPersona=binding.etNombre.text.toString()+ " " +
                    binding.etApellido.text.toString() +" "+
                    obtenerGeneroSeleccionado() +" "+
                    obtenerPreferenciaSeleccionada() + " "+
                    estadoCivil + " "+
                    binding.swEmail.isChecked
            listaPersonas.add(infoPersona)
            limpiarcontroles()
        }
    }

    fun limpiarcontroles(){
        listaPreferencia.clear()
        binding.etNombre.setText("")
        binding.etApellido.setText("")
        binding.swEmail.isChecked=false
        binding.chDibujo.isChecked=false
        binding.chCine.isChecked=false
        binding.chDeporte.isChecked=false
        binding.radioGroup.clearCheck()
        binding.spEstadoCivil.setSelection(0)
        binding.etNombre.isFocusableInTouchMode=true
        binding.etNombre.requestFocus()
    }



    //metodo para agragar o quitar la preferencia
    fun agragarPreferencia(vista:View){
        val checBox=vista as CheckBox
        if(checBox.isChecked){

            listaPreferencia.add(checBox.text.toString())
           /* when(checBox.id){
                R.id.chDeporte->listaPreferencia.add(checBox.text.toString())
                R.id.chCine->listaPreferencia.add(checBox.text.toString())
                R.id.chDibujo->listaPreferencia.add(checBox.text.toString())
            }*/
        }else{
            listaPreferencia.remove(checBox.text.toString())
        }
    }

    fun obtenerPreferenciaSeleccionada():String{
        var preferencia=""
        for(pref in listaPreferencia){
            preferencia+="$pref -"
        }
        return preferencia
    }




    fun obtenerGeneroSeleccionado():String{
        var genero=""
        when(binding.radioGroup.checkedRadioButtonId){
            R.id.rbMasculino->{
                genero=binding.rbMasculino.text.toString()
            }
            R.id.rbFemenino->{
                genero=binding.rbFemenino.text.toString()
            }
        }
        return genero
    }

    //ESTA ES LA FUNCION QUE PERMITE ENVIAR LOS MENSAJES DE ERROR A LA VISTA DEL MAIN ACTIVITY
    fun enviarMensajeError(vista:View,mensajeError:String){
        val snackbar=Snackbar.make(vista,mensajeError,Snackbar.LENGTH_LONG)
        val snackbarView:View=snackbar.view
        snackbarView.setBackgroundColor(ContextCompat.getColor(this,
        R.color.snakbarerror))
        snackbar.show()

    }

    //metodo general para validar el formulario
    fun validarFormulario(vista:View):Boolean{
        var respuesta=false
        if(!validarNombre()){
            enviarMensajeError(vista,getString(R.string.errorNombre))
        }else if(!validarApellido()){
            enviarMensajeError(vista,getString(R.string.errorApellido))
        }else if(!validarGenero()){
            enviarMensajeError(vista,getString(R.string.errorGenero))
        }else if(!validarEstadoCivil()){
            enviarMensajeError(vista,getString(R.string.errorEstadoCivil))
        }else if(!validarPreferencias()){
            enviarMensajeError(vista,getString(R.string.errorPreferencia))
        }else{
            respuesta=true
        }
        return respuesta
    }


    //METODO PARA VALIDAR EL GENERO
    fun validarGenero():Boolean{
        var respuesta=true
        if(binding.radioGroup.checkedRadioButtonId==-1){
            respuesta=false
        }
        return respuesta
    }


    //METODO PARA VALIDAR EL ESTADO CIVIL
    fun validarEstadoCivil():Boolean{
        var respuesta=true
        if(estadoCivil==""){
            respuesta=false
        }
        return respuesta
    }

    //VALIDACION DE LAS PREFERENCIAS
    fun validarPreferencias():Boolean{
        var respuesta=false
        if(binding.chDeporte.isChecked || binding.chDibujo.isChecked || binding.chCine.isChecked){
            respuesta=true
        }
        return respuesta

    }


    //FUNCION PARA VALIDAR EL NOMBRE Y EL APELLIDO
    fun validarNombre():Boolean{
        var respuesta=true
        if(binding.etNombre.text.toString().trim().isEmpty()){
            binding.etNombre.isFocusableInTouchMode= true
            binding.etNombre.requestFocus()
            respuesta= false
        }
        return respuesta
    }

    fun validarApellido():Boolean{
        var respuesta=true
        if(binding.etApellido.text.toString().trim().isEmpty()){
            binding.etApellido.isFocusableInTouchMode=true
            binding.etApellido.requestFocus()
            respuesta= false
        }
        return respuesta
    }


    //METODOS DE LA IMPLEMENTACION
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        estadoCivil=if(position>0){

            //con el doble signo de admiracion se confirma que sea un adapter view
            parent!!.getItemAtPosition(position).toString()
        }else{
            ""
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onClick(v: View?) {
        if(v!! is CheckBox){
            agragarPreferencia(v!!)
        }else{

          when(v!!.id){
        R.id.btnRegistro->resgitrarPersona(v!!)
        R.id.btnListar->irlistaPersonas()

    }
        }

    }
}