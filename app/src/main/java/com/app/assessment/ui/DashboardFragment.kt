package com.app.assessment.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.assessment.MedicineAdapter
import com.app.assessment.databinding.FragmentDashboardBinding
import com.app.assessment.viewModel.MedicineViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private lateinit var binding: FragmentDashboardBinding
    private lateinit var medicineAdapter: MedicineAdapter
    private val viewModel: MedicineViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = DashboardFragmentArgs.fromBundle(requireArguments())
        val username = args.username
        binding.username = "${getGreetingMessage()}, $username"

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        viewModel.medications.observe(viewLifecycleOwner, Observer { medications ->
            medicineAdapter = MedicineAdapter(medications) { medication ->
                val action = DashboardFragmentDirections.actionDashboardFragmentToMedicineDetailFragment(medication.id?:0)
                findNavController().navigate(action)
            }
            binding.recyclerView.adapter = medicineAdapter
        })
        viewModel.isLoading.observe(requireActivity()) { isLoading ->
            if (isLoading) {
                binding.loadingIndicator.visibility = View.VISIBLE
            } else {
                binding.loadingIndicator.visibility = View.GONE
            }
        }
        viewModel.error.observe(requireActivity()) { errMessage ->
            Toast.makeText(requireContext(), errMessage, Toast.LENGTH_SHORT).show()
            binding.loadingIndicator.visibility = View.GONE
        }

        viewModel.fetchMedicines()
    }

    private fun getGreetingMessage(): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (hour) {
            in 0..11 -> "Good Morning"
            in 12..17 -> "Good Afternoon"
            else -> "Good Evening"
        }
    }
}
