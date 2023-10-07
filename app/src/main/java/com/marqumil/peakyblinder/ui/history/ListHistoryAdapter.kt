package com.marqumil.peakyblinder.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.marqumil.peakyblinder.R
import com.marqumil.peakyblinder.remote.response.DataItem
import com.marqumil.peakyblinder.remote.response.GlucoseResponse

class ListHistoryAdapter(private val historyList: List<DataItem>) :
    RecyclerView.Adapter<ListHistoryAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tanggalTextView: TextView = itemView.findViewById(R.id.tv_tanngal)
        private val jamTextView: TextView = itemView.findViewById(R.id.tv_jam)
        private val glucoseTextView: TextView = itemView.findViewById(R.id.tv_blood)
        private val mealsTextView: TextView = itemView.findViewById(R.id.tv_meals)
        private val cvItemHistory: View = itemView.findViewById(R.id.cv_item_blood_glucose)

        fun bind(history: DataItem, position: Int) {
            // cut the string 2 digit after day in date
            val date = history.date
            val dateCut = date?.substring(0, 10)

            tanggalTextView.text = dateCut
            jamTextView.text = history.time
            glucoseTextView.text = history.value.toString() + " md/dl"
            mealsTextView.text = history.meal
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item, parent, false)
        return HistoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val currentArticle = historyList[position]
        holder.bind(currentArticle, position)
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

//    inner class HistoryViewHolder(private val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun bind(artikel: History) {
//            with(binding) {
//                tvJudul.text = artikel.nama_artikel
//                tvNama.text = artikel.nama_penulis
//                shapeableImageView.setImageResource(artikel.gambar)
//
//
//            }
//        }
//    }
}