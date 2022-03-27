package com.example.conec

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ListAdapter (context: Context, val pantalla: Int, val datos: List<lista>): ArrayAdapter<lista>(context,R.layout.activity_main,datos){

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {

        val layoutInflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = layoutInflater.inflate(pantalla, null )

        val data = datos[position]

        val name = view.findViewById<TextView>(R.id.txtnamepdf)
        val path = view.findViewById<TextView>(R.id.txtpaht)

        name.text = data.name
        path.text = data.paht
        // view.imageView.setImageResource(R.drawable.ic_pdf)


        return view
    }
}