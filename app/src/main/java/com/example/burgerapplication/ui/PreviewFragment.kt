package com.example.burgerapplication.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.burgerapplication.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PreviewFragment:Fragment(R.layout.preview_fragment) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<View>(R.id.goButton).setOnClickListener {
            findNavController().navigate(R.id.action_previewFragment_to_menuFragment)
        }
    }

}