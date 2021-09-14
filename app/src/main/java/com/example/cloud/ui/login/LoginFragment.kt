package com.example.cloud.ui.login

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
import com.example.cloud.databinding.FragmentLoginBinding
import com.example.cloud.viewModels.ViewModelFactory
import com.example.cloud.viewModels.loginViewModel.LoginViewModel


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
        viewModelFactory = ViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)
        binding.lifecycleOwner = this
        binding.newaccount.setOnClickListener{
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

            //Init your Observers
            viewModel.loginSuccess.observe(viewLifecycleOwner, {
                if (it) {
                    Toast.makeText(context, "Logged in", Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)

                } else {
                    Toast.makeText(context, "Login failed", Toast.LENGTH_LONG).show()
                    findNavController().popBackStack()
//                    viewModel.navigateMeFailed()
                }
            })
            viewModel.spinner.observe(viewLifecycleOwner, {
                if (it) {
                    binding.progressBar2.visibility = View.VISIBLE
                    binding.signin.visibility = View.INVISIBLE
                }
                else {
                    binding.progressBar2.visibility = View.INVISIBLE
                    binding.signin.visibility = View.VISIBLE
                }
            })

        }

        return binding.root
    }

}