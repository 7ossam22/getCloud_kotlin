package com.example.cloud.ui.profile

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.cloud.R
import com.example.cloud.databinding.FragmentProfileBinding
import com.example.cloud.viewModels.ProfileViewModel
import com.example.cloud.viewModels.ViewModelFactory

class ProfileFragment : Fragment() {
    private lateinit var binding:FragmentProfileBinding
    private lateinit var application: Application
    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: ProfileViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_profile,container,false)
        application = requireNotNull(this.activity).application!!
        viewModelFactory = ViewModelFactory(application)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        //UI binding***
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.onDisplayingUserData(binding.userImg)

        //ViewModel observers
        viewModel.progress.observe(viewLifecycleOwner,{
            if (it){
                binding.progressBar4.visibility = View.VISIBLE
            }
            else{
                binding.progressBar4.visibility = View.GONE
            }
        })

        return binding.root
    }

}