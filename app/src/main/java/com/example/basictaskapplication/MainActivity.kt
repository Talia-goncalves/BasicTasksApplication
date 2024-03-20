package com.example.basictaskapplication

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.navigation.ui.navigateUp
import com.example.basictaskapplication.databinding.ActivityMainBinding

class MainActivity<View> : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val tasks = mutableListOf<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            showTitlePopup(view)
        }
    }

    private fun showTitlePopup(view: android.view.View) {
        var titulo = ""
        val popupTitulo = AlertDialog.Builder(this)
        val textTitulo = EditText(this)

        popupTitulo.setTitle("Título")
        popupTitulo.setView(textTitulo)
        popupTitulo.setPositiveButton("Ok") { dialog, _ ->
            titulo = textTitulo.text.toString()
            if (titulo.isNotEmpty()) {
                showDescriptionPopup(view, titulo)
            } else {
                Snackbar.make(view, "Título não pode estar vazio", Snackbar.LENGTH_SHORT).show()
            }
        }
        popupTitulo.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.cancel()
        }
        popupTitulo.show()
    }

    private fun showDescriptionPopup(view: android.view.View, titulo: String) {
        var descricao = ""
        val popupDescricao = AlertDialog.Builder(this)
        val textDescricao = EditText(this)
        val statusOptions = arrayOf("Pendente", "Concluída")

        popupDescricao.setTitle("Descrição")
        popupDescricao.setView(textDescricao)

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.addView(textDescricao)

        val spinner = Spinner(this)
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, statusOptions)
        layout.addView(spinner)

        popupDescricao.setView(layout)

        popupDescricao.setPositiveButton("Ok") { dialog, _ ->
            descricao = textDescricao.text.toString()
            val selectedStatus = statusOptions[spinner.selectedItemPosition]
            if (titulo.isNotEmpty() && descricao.isNotEmpty()) {
                val task = Task(titulo, descricao, selectedStatus)
                tasks.add(task)
                Snackbar.make(view, task.toString(), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            } else {
                Snackbar.make(view, "Por favor, preencha todos os campos", Snackbar.LENGTH_SHORT).show()
            }
        }
        popupDescricao.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.cancel()
        }
        popupDescricao.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}