package com.example.conec

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_viewpdf.*
import java.io.File


class viewpdf2 : AppCompatActivity() {

    var name : String? = ""
    var path : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewpdf2)
        setSupportActionBar(pdf_toolbar)

        val objItem: Intent = intent
        name = objItem.getStringExtra("namepdf")
        path = objItem.getStringExtra("pahtpdf")


        pdf_toolbar.setNavigationOnClickListener {
            fin()
        }

       val myPath = File(path)

        pdfview.fromFile(myPath).enableSwipe(true).swipeHorizontal(true).load()

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_listamaterias, menu)

        return true
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var itemview = item.itemId

        when(itemview){
            R.id.listadd -> share()
            R.id.del -> delete()
        }

        return false
    }

    fun fin(){
        finish()
    }


    fun share() {



        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"   //"*/*" will accepts all types of files, if you want specific then change it on your need.
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra(
                Intent.EXTRA_SUBJECT,
                "Sharing file from the AppName"
            )
            putExtra(
                Intent.EXTRA_TEXT,
                "Sharing file from the AppName with some description"
            )
            val fileURI = FileProvider.getUriForFile(
                getApplicationContext()!!, getApplicationContext()!!.packageName + ".provider",
                File(path)
            )
            putExtra(Intent.EXTRA_STREAM, fileURI)
        }
        startActivity(shareIntent)

    }

    fun delete(){
        File(path).delete()
        fin()
    }


//end class
}