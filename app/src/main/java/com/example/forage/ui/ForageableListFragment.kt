package com.example.forage.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.forage.BaseApplication
import com.example.forage.R
import com.example.forage.databinding.FragmentForageableListBinding
import com.example.forage.ui.adapter.ForageableListAdapter
import com.example.forage.ui.viewmodel.ForageableViewModel
import com.example.forage.ui.viewmodel.ForageableViewModelFactory

class ForageableListFragment : Fragment() {

    private val viewModel: ForageableViewModel by activityViewModels() {
        ForageableViewModelFactory(
            (activity?.application as BaseApplication).database.forageableDao())
    }

    private var _binding: FragmentForageableListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentForageableListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ForageableListAdapter { forageable ->
            val action = ForageableListFragmentDirections
                .actionForageableListFragmentToForageableDetailFragment(forageable.id)
            findNavController().navigate(action)
        }

        viewModel.allItems.observe(this.viewLifecycleOwner) {
            forageable -> forageable.let {
                adapter.submitList(it)
            }
        }

        binding.apply {
            recyclerView.adapter = adapter
            addForageableFab.setOnClickListener {
                findNavController().navigate(
                    R.id.action_forageableListFragment_to_addForageableFragment
                )
            }
        }
    }
}
