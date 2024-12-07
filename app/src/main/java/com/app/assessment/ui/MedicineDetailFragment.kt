package com.app.assessment.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.app.assessment.databinding.FragmentMedicineDetailBinding
import com.app.assessment.viewModel.MedicineViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MedicineDetailFragment : Fragment() {
    private val viewModel: MedicineViewModel by viewModels()
    private lateinit var binding: FragmentMedicineDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMedicineDetailBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = MedicineDetailFragmentArgs.fromBundle(requireArguments())
        val id = args.idMedicine

        viewModel.fetchMedicineById(id)
        viewModel.medicineById.observe(viewLifecycleOwner) { medicine ->
            if (medicine != null) {
                binding.medicine = medicine
            } else {
                Log.d("MedicineDetails", "No medicine found with the given ID")
            }
        }
    }
}

