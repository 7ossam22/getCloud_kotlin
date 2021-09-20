package com.example.cloud.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cloud.R
import com.example.cloud.databinding.FragmentHomeBinding
import com.example.cloud.viewModels.HomeViewModel
import com.example.cloud.viewModels.ViewModelFactory

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var adapter: HomeAdapter

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val application = requireNotNull(this.activity).application!!
        viewModelFactory = ViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        //UI binding***
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        val manager = GridLayoutManager(activity, 2)
        adapter = HomeAdapter()
        binding.recyclerView1.layoutManager = manager
        binding.recyclerView1.adapter = adapter
        binding.menu.setOnClickListener {
            val popupMenu = PopupMenu(this.context,binding.menu)
            popupMenu.menuInflater.inflate(R.menu.home_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.profileFragment -> findNavController().navigate(R.id.action_homeFragment_to_profileFragment2).equals(true)
                    R.id.aboutFragment ->true
                    R.id.logout -> true
                    else-> false
                }
            }
            popupMenu.show()
        }
        //ViewModel observers
        viewModel.cloudList.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })
        viewModel.progressListener.observe(viewLifecycleOwner, {
            when {
                it -> binding.progressBar2.visibility = View.VISIBLE
                else -> binding.progressBar2.visibility = View.GONE
            }
        })
        viewModel.listChecker.observe(viewLifecycleOwner, {
            binding.itemAnnotation.text = "No data to load"
            when {
                it -> binding.itemAnnotation.visibility = View.VISIBLE
                else -> binding.itemAnnotation.visibility = View.GONE
            }
        })
        viewModel.showData(binding.userimg)

        //Fragment root
        return binding.root
    }

}