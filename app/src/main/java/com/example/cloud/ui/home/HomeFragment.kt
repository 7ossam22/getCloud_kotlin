package com.example.cloud.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cloud.R
import com.example.cloud.databinding.FragmentHomeBinding
import com.example.cloud.viewModels.ViewModelFactory
import com.example.cloud.viewModels.HomeViewModel

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
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        val manager = GridLayoutManager(activity, 2)
        adapter = HomeAdapter()
        binding.recyclerView1.layoutManager = manager
        binding.recyclerView1.adapter = adapter
        viewModel.cloudList.observe(viewLifecycleOwner, {
            adapter.data = it
            adapter.notifyDataSetChanged()
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

        //menu
//        setHasOptionsMenu(true)
        return binding.root
    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu, inflater)
//        inflater.inflate(R.menu.home_menu,menu)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return NavigationUI.onNavDestinationSelected(item, requireView().findNavController())
//                || super.onOptionsItemSelected(item)
//    }
}