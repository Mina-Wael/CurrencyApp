package com.example.currencyapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.currencyapp.databinding.FragmentHomeBinding
import com.example.currencyapp.utils.Constants.API_KEY
import com.example.currencyapp.utils.Resource
import com.example.currencyapp.utils.ResultState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val homeViewModel: HomeViewModel by viewModels()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.getSymbols(API_KEY)
        listenToSymbolsStateFlow()
        setupSwapButton()
        setListenerToAmountInputText()

    }

    private fun listenToSymbolsStateFlow() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.symbolsStateFlow.collect { result ->
                    when (result) {
                        is ResultState.Success -> {
                            hideProgressBar()
                            showAllFields()
                            setupSpinnerFrom(result.data.symbols.keys.toList())
                            setupSpinnerTo(result.data.symbols.keys.toList())
                        }
                        is ResultState.Fail -> {
                            hideAllFields()
                            hideProgressBar()

                            binding.tvMessage.visibility = View.VISIBLE
                            binding.tvMessage.text = result.message
                        }
                        is ResultState.Loading -> {
                            hideAllFields()
                            showProgressBar()
                        }
                    }
                }

            }
        }
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

    private fun showAllFields() {
        binding.apply {
            spFrom.visibility = View.VISIBLE
            spTo.visibility = View.VISIBLE
            btnDetails.visibility = View.VISIBLE
            etAmount.visibility = View.VISIBLE
            etValue.visibility = View.VISIBLE
            btnSwap.visibility = View.VISIBLE
        }
    }

    private fun hideAllFields() {
        binding.apply {
            spFrom.visibility = View.GONE
            spTo.visibility = View.GONE
            btnDetails.visibility = View.GONE
            btnSwap.visibility = View.GONE
            etAmount.visibility = View.GONE
            etValue.visibility = View.GONE
        }
    }


    private fun setupSpinnerFrom(spinnerList: List<String>) {

        binding.spFrom.apply {
            adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, spinnerList)
        }

    }

    private fun setupSpinnerTo(spinnerList: List<String>) {
        binding.spTo.apply {
            adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, spinnerList)
        }
    }

    private fun setupSwapButton() {
        binding.btnSwap.setOnClickListener {
            var spFromPosition = binding.spFrom.selectedItemPosition
            binding.spFrom.setSelection(binding.spTo.selectedItemPosition)
            binding.spTo.setSelection(spFromPosition)
        }
    }

    private fun setListenerToAmountInputText() {
        binding.etAmount.addTextChangedListener {
                if (it.isNullOrEmpty()&&it.toString()!="") {
                    homeViewModel.convert(
                        binding.spFrom.selectedItem.toString(),
                        binding.spTo.selectedItem.toString(),
                        it.toString().toDouble()
                    )
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}