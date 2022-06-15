package com.example.cloud.ui.home

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cloud.R
import com.example.cloud.adapter.homeAdapter.HomeAdapter
import com.example.cloud.databinding.FragmentHomeBinding
import com.example.cloud.viewModels.ViewModelFactory
import com.example.cloud.viewModels.homeViewModel.HomeViewModel


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var popupMenu: PopupMenu
    private lateinit var alertDialogBuilder: AlertDialog.Builder
    private lateinit var viewModel: HomeViewModel
    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var alertDialog: AlertDialog

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        viewModelFactory = ViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        val manager = GridLayoutManager(activity, 2)
        val adapter = HomeAdapter()
        binding.recyclerView1.layoutManager = manager
        binding.recyclerView1.adapter = adapter
        viewModel.cloudList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        viewModel.showMenu.observe(viewLifecycleOwner) {
            if (it) {
                view?.let { it1 -> showMenu(it1) }
            }
        }
        binding.menu.setOnClickListener {
            viewModel.showPopUpMenu()
        }
        setHasOptionsMenu(true)
        viewModel.showData(binding.userimg)
        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.home_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, requireView().findNavController())
    }

    private fun showMenu(it: View) {
        popupMenu = PopupMenu(this.context, binding.menu)
        popupMenu.menuInflater.inflate(R.menu.home_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.Logout -> {
                    alertDialogBuilder = AlertDialog.Builder(this.context)
                    alertDialogBuilder.setTitle("Logout Confirmation...")
                    alertDialogBuilder.setIcon(R.drawable.ic_baseline_exit)
                    alertDialogBuilder.setMessage("Are you sure you want to logout?!")
                    alertDialogBuilder.setCancelable(false)
                    alertDialogBuilder.setPositiveButton("Yes") { _, _ ->
                        it.findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
                    }
                    alertDialogBuilder.setNegativeButton("No") { _, _ ->
                        alertDialogBuilder.setCancelable(
                            true
                        )
                    }
                    alertDialog = alertDialogBuilder.create()
                    alertDialog.show()
                }
                R.id.profileFragment -> it.findNavController()
                    .navigate(R.id.action_homeFragment_to_profileFragment)
                R.id.about -> Toast.makeText(
                    this.context,
                    "You Clicked to View About Fragment",
                    Toast.LENGTH_LONG
                ).show()
            }
            true
        }
        popupMenu.show()
    }
}