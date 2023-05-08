package com.example.onppe_v1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView


class SuiviSignalementAdapter(val context: Context, var data:List<Signalement_local>): RecyclerView.Adapter<SuiviSignalementAdapter.MyViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.signalement_layout, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.apply {

            holder.datesignalement.text = data[position].datesignalement
            holder.statut.text =  data[position].statut
            holder.numbersignalement.text = (position + 1).toString()
        }

        holder.itemView.setOnClickListener{view: View ->
            val data = bundleOf("position" to position)
            view.findNavController().navigate(R.id.action_mainFragment_to_detailFragment, data)
        }
        val detailsButton = holder.itemView.findViewById<android.widget.Button>(R.id.details)
        detailsButton.setOnClickListener {
            val data = bundleOf("position" to position)
            it.findNavController().navigate(R.id.action_mainFragment_to_detailFragment, data)
        }


    }

    override fun getItemCount(): Int {
        return data.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val datesignalement = view.findViewById (R.id.datesignalement) as TextView
        val statut = view.findViewById (R.id.statevalue) as TextView
        val numbersignalement = view.findViewById (R.id.numbersignalement) as TextView
    }
}
