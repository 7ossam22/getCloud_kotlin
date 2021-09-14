package com.example.cloud.ui.createAccount

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.cloud.R
import com.example.cloud.databinding.FragmentCreateAccountBinding
import com.example.cloud.viewModels.ViewModelFactory
import com.example.cloud.viewModels.createAccountViewModel.CreateAccountViewModel

class CreateAccountFragment : Fragment() {

    private lateinit var binding: FragmentCreateAccountBinding
    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: CreateAccountViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_create_account,container,false)
        viewModelFactory = ViewModelFactory()
        viewModel = ViewModelProvider(this,viewModelFactory).get(CreateAccountViewModel::class.java)
        binding.lifecycleOwner = this
        binding.existAccount.setOnClickListener {
            findNavController().navigate(R.id.action_createAccountFragment_to_loginFragment)
        }
        binding.signup.setOnClickListener {
            if (binding.username.text.isEmpty() && binding.email.text.isEmpty() && binding.password.text.isEmpty()) {
                Toast.makeText(context, "Fill your data", Toast.LENGTH_LONG).show()
            } else if (binding.username.text.isEmpty()) {
                binding.username.requestFocus()
            }
              else if (binding.email.text.isEmpty()) {
                binding.email.requestFocus()
            } else if (binding.password.text.isEmpty()) {
                binding.password.requestFocus()
            } else {
                viewModel.register(binding.username.text.toString().trim(), binding.email.text.toString().trim(), binding.password.text.toString().trim())
            }
            }

        viewModel.registerSuccess.observe(viewLifecycleOwner,{
            if (it) {
                Toast.makeText(context, "Account registration successful", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "registration failed", Toast.LENGTH_LONG).show()
            }
        })

        viewModel.spinner.observe(viewLifecycleOwner,{
            if (it) {
                binding.progressBar1.visibility = View.VISIBLE
                binding.signup.visibility = View.INVISIBLE
            }
            else {
                binding.progressBar1.visibility = View.INVISIBLE
                binding.signup.visibility = View.VISIBLE
            }
        })
        return binding.root
    }

}