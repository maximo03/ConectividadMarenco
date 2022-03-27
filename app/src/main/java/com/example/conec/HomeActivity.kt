package com.example.conec

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_home.*
import java.io.File
import java.util.*

class HomeActivity : AppCompatActivity() {

    private val STORAGE_CODE: Int = 100;
    val ruta = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS + "/PdfMarenco/").toString()
    val dir: File = File(ruta)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(main_toolbar)
        permission()
        /********************************************************************************/
        lstView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent?): Boolean {
                v.getParent().requestDisallowInterceptTouchEvent(true)
                return false
            }
        })
        /********************************************************************************/
        btncot.setOnClickListener(){
           val intent = Intent(applicationContext, Form ::class.java)
           startActivity(intent)
        }
        /********************************************************************************/
        lstView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                // Obtiene el texto de dicha posición
                val name = (lstView.getItemAtPosition(position) as lista).name
                val path = (lstView.getItemAtPosition(position) as lista).paht
                val intent = Intent(applicationContext, viewpdf::class.java)
                intent.putExtra("namepdf", name)
                intent.putExtra("pahtpdf", path)
                startActivity(intent)
            }
        /***********************************************************************************/

    //end onCreate
    }

    /****************************************************************************************************/
    fun permission(){

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {

            //system OS >= Marshmallow(6.0), check permission is enabled or not
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                //permission was not granted, request it
                val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                requestPermissions(permissions, STORAGE_CODE)

            } else {
                //permission already granted, call savePdf() method
                if (dir.exists() && dir.isDirectory()) {

                    Toast.makeText(this, "El directorio PdfMarenco ya existe", Toast.LENGTH_SHORT).show()
                    loadFiles()

                } else {//run when does directory  not exist
                    mkDir()
                    Toast.makeText(this, "El directorio PdfMarenco se a creado en /Documentos/PdfMarenco", Toast.LENGTH_SHORT).show()
                    loadFiles()
                }

            }

        } else {
            //system OS < marshmallow, call savePdf() method
            if (dir.exists() && dir.isDirectory()) {
                Toast.makeText(this, "El directorio PdfMarenco ya existe", Toast.LENGTH_SHORT).show()
                loadFiles()
            } else {//Se ejecuta si no existe el directorio
                mkDir()
                Toast.makeText(this, "El directorio PdfMarenco se a creado en /Documentos/PdfMarenco", Toast.LENGTH_SHORT).show()
                loadFiles()
            }

        }
    }
   /****************************************************************************************************/
    fun mkDir(): File? {
        //declare val path
        val directorio = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
                "PdfMarenco"
        )
        //view a messages be dont create directory
        if (!directorio.mkdirs()){
            Toast.makeText(this, "Error: Dont create directory", Toast.LENGTH_SHORT).show()
        }
        return directorio
    }
    /****************************************************************************************************/
    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        when (requestCode) {
            STORAGE_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission from popup was granted, call savePdf() method
                    // savePdf()
                } else {
                    //permission from popup was denied, show error message
                    Toast.makeText(this, "Permission denied...!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    /****************************************************************************************************/
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_borra_lista, menu)

        return true
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var itemview = item.itemId

        when(itemview){
            R.id.vaciar -> delAll()
            R.id.refrescar -> loadFiles()

        }

        return false
    }
    /****************************************************************************************************/
    fun loadFiles(){

        val listValores: MutableList<lista> = ArrayList()

        //get Path master directory
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS + "/PdfMarenco/").toString() + File.separator
        val f = File(path)
        //get files
        val file = f.listFiles()

        for (i in file.indices) {

            //Add names files from list adapter
            val regis = lista(file[i].name, file[i].absolutePath)

            listValores.add(regis)
        }
        //Create content adapter
        val modelAdapter = ListAdapter(applicationContext, R.layout.multidata, listValores)

        //load listview
        lstView.adapter = modelAdapter
       // saveBtn.isEnabled=true
    }
    /****************************************************************************************************/
    fun delAll(){
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS + "/PdfMarenco").toString()
        File(path).deleteRecursively()
        mkDir()
        loadFiles()

    }





//end class
}