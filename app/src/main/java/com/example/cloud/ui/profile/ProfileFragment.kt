@file:Suppress("DEPRECATION")

package com.example.cloud.ui.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.cloud.R
import com.example.cloud.databinding.FragmentProfileBinding
import com.example.cloud.viewModels.profileViewModel.ProfileViewModel


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewmodel: ProfileViewModel
    private lateinit var uri: Uri
    private lateinit var name: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        viewmodel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        binding.changeProfile.setOnClickListener {
            viewmodel.selectingImageFromStorage()
            viewmodel.onSelectingImageFromStorageComplete()
        }

        viewmodel.imageSelecting.observe(viewLifecycleOwner) {
            if (it) {
                val intent = Intent()
                intent.type = "image/'"
                intent.action = Intent.ACTION_GET_CONTENT
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                startActivityForResult(intent, 86)
                viewmodel.uploadPicture(uri, name)
            }
        }
        return binding.root
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 86 && resultCode == Activity.RESULT_OK && data != null) {
            uri = data.data!!
            val path = data.data!!.path
            val x = path!!.lastIndexOf('/')
            name = path.substring(x + 1)
        }
    }
}