package com.yjdev.propinapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.yjdev.propinapp.databinding.ActivityMainBinding
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {
    /**
     * Se activo el viewbinding en build.gradle, para crear un objeto binding
     * */
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
         /**
          * Se asigna un evento on click al boton calcular y se llama a la
          * funcion calcularPropina()
          * */
        binding.botonCalcular.setOnClickListener { calcularPropina() }
        binding.costoDelServicio.setOnKeyListener{ view, keyCode, _ -> handleKeyEvent(view, keyCode)}
    }

    /**
     * Funcion que recibe un double y lo imprime en el binding.resultado
     * formateado como moneda
     * */
    private fun mostrarResultado(propina:Double){
        val formateoPropina = NumberFormat.getCurrencyInstance().format(propina)
        /*Manipular string directo a los recursos de la App, Clase -> R.string, se ocupa método getString()*/
        binding.resultado.text = getString(R.string.monto_total, formateoPropina)
    }

    /**
     * Funcion para calular la propina
     * */
    private fun calcularPropina(){

        /** Se obtiene el monto del binding.costodelServicio en string
         * y el costo se convierte a nulo (si no hay dato) o double
         **/
        val montoIngresado = binding.costoDelServicio.text.toString()
        val costo = montoIngresado.toDoubleOrNull()

        /**
         *  Se obtiene el elemento seleccionado de las opciones en el binding.opciones y
         *  se compara con los id de los elementos resource de la app(Clase -> R.id)
         * */
        val porcentajeAsignado = when(binding.opciones.checkedRadioButtonId) {
            R.id.quince_porciento -> .15
            R.id.dieciocho_porciento -> .18
            else -> .20
        }

        /**
         * Si el costo regresa Null, se retorna y muestra el
         * resultado 0.0 para que la app no crashee, en caso contrario se obtiene la propina
         * */
        if (costo == null) {
            mostrarResultado(0.0)
            return
        } else{
            var propina = costo * porcentajeAsignado
            if(binding.redondearSwitch.isChecked){
                propina = kotlin.math.ceil(propina)
            }
            mostrarResultado(propina)
        }

    }
    private fun handleKeyEvent(view: View, keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // Hide the keyboard
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            return true
        }
        return false
    }
}

