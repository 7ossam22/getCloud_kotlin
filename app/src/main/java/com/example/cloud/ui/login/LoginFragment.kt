package com.example.cloud.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.cloud.R
import com.example.cloud.api.LoginOperation
import com.example.cloud.databinding.FragmentLoginBinding
import com.example.cloud.viewModels.LoginViewModel
import com.example.cloud.viewModels.ViewModelFactory


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var viewModelFactory: ViewModelFactory


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        val application = requireNotNull(this.activity).application!!
        viewModelFactory = ViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)
        binding.lifecycleOwner = this
        binding.newaccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_createAccountFragment)
        }
        binding.signin.setOnClickListener {
            if (binding.email.text.isEmpty() && binding.password.text.isEmpty()) {
                Toast.makeText(context, "Fill your data", Toast.LENGTH_LONG).show()
            } else if (binding.email.text.isEmpty()) {
                binding.email.requestFocus()
            } else if (binding.password.text.isEmpty()) {
                binding.password.requestFocus()
            } else {
                viewModel.login(
                    binding.email.text.toString().trim(),
                    binding.password.text.toString().trim()
                )
            }
        }

        //Init your Observers
        viewModel.loginSuccess.observe(viewLifecycleOwner, {
            when {
                it.equals(LoginOperation.Ok) -> {
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                    Toast.makeText(context, "Logged in", Toast.LENGTH_LONG).show()
                }
                it.equals(LoginOperation.Failed) -> {
                    Toast.makeText(context, "Login failed", Toast.LENGTH_LONG).show()
                }
                else -> {
                    Log.v("op", it.toString())
                }
            }
        })
        viewModel.spinner.observe(viewLifecycleOwner, {
            if (it) {
                binding.progressBar2.visibility = View.VISIBLE
                binding.signin.visibility = View.INVISIBLE
            } else {
                binding.progressBar2.visibility = View.INVISIBLE
                binding.signin.visibility = View.VISIBLE
            }
        })


        return binding.root
    }

}