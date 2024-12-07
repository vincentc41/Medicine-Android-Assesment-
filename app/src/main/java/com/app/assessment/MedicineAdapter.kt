package com.app.assessment

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.assessment.databinding.ItemMedicineBinding
import com.app.assessment.model.MedicalRecord
import com.bumptech.glide.Glide


class MedicineAdapter(
    private val medicines: List<MedicalRecord.Drug>,
    private val onItemClick: (MedicalRecord.Drug) -> Unit
) : RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicineViewHolder {
        val binding = ItemMedicineBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MedicineViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MedicineViewHolder, position: Int) {
        holder.bind(medicines[position])
        holder.itemView.setOnClickListener {
            onItemClick(medicines[position])
        }
    }

    override fun getItemCount(): Int = medicines.size

    class MedicineViewHolder(private val binding: ItemMedicineBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(medicine: MedicalRecord.Drug) {
            binding.medicine = medicine
            binding.executePendingBindings()
        }
    }
}

