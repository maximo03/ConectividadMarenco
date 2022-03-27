package com.example.conec

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.itextpdf.text.Document
import com.itextpdf.text.Image
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import kotlinx.android.synthetic.main.form.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class Form : AppCompatActivity() {

    private val STORAGE_CODE: Int = 100;
    val ruta = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS + "/PdfMarenco/").toString()
    val dir: File = File(ruta)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.form)
        setSupportActionBar(toolbar)

        /***********************************************************************************/
        loadFiles()
        /***********************************************************************************/
        lstView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent?): Boolean {
                v.getParent().requestDisallowInterceptTouchEvent(true)
                return false
            }
        })
        /********************************************************************************/
        toolbar.setNavigationOnClickListener {
            finish()
        }
        /***********************************************************************************/
        btnPdf.setOnClickListener {
          //  onPermit()

            var aplicar = true
            if (tx_1nomcoti .text.toString().trim { it <= ' ' }.length == 0 && aplicar) {
                showAlert("Falta el nombre del Cliente")
                tx_2asunto.requestFocus()
                aplicar = false
            }
            if (tx_2asunto.text.toString().trim { it <= ' ' }.length == 0 && aplicar) {
                showAlert("Falta los trabajos a realizar")
                tx_3materiales.requestFocus()
                aplicar = false
            }
            if (tx_3materiales.text.toString().trim { it <= ' ' }.length == 0 && aplicar) {
                showAlert("Falta Materiales")
                tx_4precio_mate.requestFocus()
                aplicar = false
            }
            if (tx_4precio_mate.text.toString().trim { it <= ' ' }.length == 0 && aplicar) {
                showAlert("Falta el costo de materiales")
                tx_5precio_obra.requestFocus()
                aplicar = false
            }
            if (tx_5precio_obra.text.toString().trim { it <= ' ' }.length == 0 && aplicar) {
                showAlert("Falta el costo total de obra")
                btnPdf.requestFocus()
                aplicar = false
            }
            if (aplicar) {
                btnPdf.isEnabled = false
                onPermit()
            }

        }
        /***********************************************************************************/
        lstView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                // Obtiene el texto de dicha posición
                val name = (lstView.getItemAtPosition(position) as lista).name
                val path = (lstView.getItemAtPosition(position) as lista).paht
                val intent = Intent(applicationContext, viewpdf2 ::class.java)
                intent.putExtra("namepdf", name)
                intent.putExtra("pahtpdf", path)
                startActivity(intent)
            }
        /***********************************************************************************/
        btnClear.setOnClickListener {
            clearFiels()
        }

    //end onCreate
    }

    /*********************************************************************************************/
    fun savePdf() {
        //create object of Document class
        val mDoc = Document()

        //pdf file name
        val mFileName = SimpleDateFormat("dd-MM-yy_HH-mm-ss", Locale.getDefault()).format(System.currentTimeMillis())

        //pdf file path to save
        val mFilePath = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS + "/PdfMarenco/"),
            "/" + mFileName + "_Proforma_" + tx_1nomcoti.text.toString() + ".pdf"
        )

        //get backgroumd and transfor img from document
        val d: Drawable = this.getResources().getDrawable(R.drawable.plantillapdf)
        val bitDw = d as BitmapDrawable
        val bmp = bitDw.bitmap
        val stream = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val img: Image = Image.getInstance(stream.toByteArray())

        //build pdf text
        val t_saludo = "!Cordial Salaludo¡"
        val t_cotizacion = "Conectividad Marenco le envía su cotización"
        val t_cliente = "Cliente: "+tx_1nomcoti.text.toString()
        val t_analisis = "Según el análisis realizado los trabajos a realizar seran"
        val t_aspectos = "Aspectos Para Mencionar: Este se estará revisando con el cliente por algún detalle para que las ambas partes puedan salir satisfecha."
        val t_materiales = "Materiales:"
        val t_atencion = "Atención: \n (Si el contrato sufre alguna modificación adicional, el precio en la cotización se verá afectado)"
        val t_trealizar = tx_2asunto.text.toString()
        val t_inmateril = tx_3materiales.text.toString()
        val t_premate = "Precio Materiales: "+tx_4precio_mate.text.toString()
        val t_preobra = "Costo Total de la Obra: "+tx_5precio_obra.text.toString()

        //declare style Paragraphs from documents
        val p_saludo = Paragraph()
        p_saludo.add(t_saludo)
        p_saludo.spacingAfter = 2f
        p_saludo.spacingBefore = 130f

        val p_cotizacion = Paragraph()
        p_cotizacion.add(t_cotizacion)
        p_cotizacion.spacingAfter = 1f
        p_cotizacion.spacingBefore = 4f

        val p_cliente = Paragraph()
        p_cliente.add(t_cliente)
        p_cliente.spacingAfter = 1f
        p_cliente.spacingBefore = 4f

        val p_analisis = Paragraph()
        p_analisis.add(t_analisis)
        p_analisis.spacingAfter = 1f
        p_analisis.spacingBefore = 4f

        val p_trealizar = Paragraph()
        p_trealizar.add(t_trealizar)
        p_trealizar.spacingAfter = 1f
        p_trealizar.spacingBefore = 4f

        val p_aspectos = Paragraph()
        p_aspectos.add(t_aspectos)
        p_aspectos.spacingAfter = 1f
        p_aspectos.spacingBefore = 4f

        val p_materiales = Paragraph()
        p_materiales.add(t_materiales)
        p_materiales.spacingAfter = 1f
        p_materiales.spacingBefore = 4f

        val p_inmaterial = Paragraph()
        p_inmaterial.add(t_inmateril)
        p_inmaterial.spacingAfter = 1f
        p_inmaterial.spacingBefore = 4f

        val p_premate = Paragraph()
        p_premate.add(t_premate)
        p_premate.spacingAfter = 1f
        p_premate.spacingBefore = 4f

        val p_preobra = Paragraph()
        p_preobra.add(t_preobra)
        p_preobra.spacingAfter = 1f
        p_preobra.spacingBefore = 4f

        val p_atencion = Paragraph()
        p_atencion.add(t_atencion)
        p_atencion.spacingAfter = 1f
        p_atencion.spacingBefore = 4f

        try {

            //create instance of PdfWriter class
            PdfWriter.getInstance(mDoc, FileOutputStream(mFilePath))

            //open the document for writing
            mDoc.open()

            //prepare img background
            img.setAlignment(Image.UNDERLYING)
            img.setAbsolutePosition(0f, 0f)
            img.scaleAbsolute(594f, 840f)

            //add author of the document (metadata)
            mDoc.addAuthor("ConectividadMarenco")

            //add background
            mDoc.add(img)

            //add paragraph to the document
            mDoc.add(Paragraph(p_saludo))
            mDoc.add(Paragraph(p_cotizacion))
            mDoc.add(Paragraph(p_cliente))
            mDoc.add(Paragraph(p_analisis))
            mDoc.add(Paragraph(p_trealizar))
            mDoc.add(Paragraph(p_aspectos))
            mDoc.add(Paragraph(p_materiales))
            mDoc.add(Paragraph(p_inmaterial))
            mDoc.add(Paragraph(p_premate))
            mDoc.add(Paragraph(p_preobra))
            mDoc.add(Paragraph(p_atencion))

            //close document
            mDoc.close()

            //show file saved message with file name and path
            Toast.makeText(this, "$mFileName.pdf\nis saved to\n$mFilePath", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            //if anything goes wrong causing exception, get and show exception message
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }
    /*********************************************************************************************/
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
   /***********************************************************************************************/
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
    fun onPermit(){
       //we need to handle runtime permission for devices with marshmallow and above
       if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
           //system OS >= Marshmallow(6.0), check permission is enabled or not
           if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
               == PackageManager.PERMISSION_DENIED
           ) {
               //permission was not granted, request it
               val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
               requestPermissions(permissions, STORAGE_CODE)

           } else {
               //permission already granted, call savePdf() method
               //saveBtn.isEnabled=false
               savePdf()
               loadFiles()
           }
       } else {
           //system OS < marshmallow, call savePdf() method
           // saveBtn.isEnabled=false
           savePdf()
           loadFiles()
       }
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
         btnPdf.isEnabled=true
    }
    /****************************************************************************************************/
    fun delAll(){
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS + "/PdfMarenco").toString()
        File(path).deleteRecursively()
        mkDir()
        loadFiles()

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

    fun showAlert(message: String) {

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Revisa tus Datos!")
        builder.setMessage(message)
        val dialog = builder.create()
        dialog.show()

    }

    fun clearFiels(){
        tx_1nomcoti.setText("")
        tx_2asunto.setText("")
        tx_3materiales.setText("")
        tx_4precio_mate.setText("")
        tx_5precio_obra.setText("")
    }

//end class
}