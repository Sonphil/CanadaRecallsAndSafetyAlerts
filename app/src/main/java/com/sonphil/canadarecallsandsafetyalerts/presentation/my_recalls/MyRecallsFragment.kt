package com.sonphil.canadarecallsandsafetyalerts.presentation.my_recalls

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sonphil.canadarecallsandsafetyalerts.R

class MyRecallsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_recalls, container, false)
    }


}
