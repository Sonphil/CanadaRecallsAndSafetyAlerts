package com.sonphil.canadarecallsandsafetyalerts.presentation.my_recalls

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sonphil.canadarecallsandsafetyalerts.R
import dagger.android.support.DaggerFragment

class MyRecallsFragment : DaggerFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_recalls, container, false)
    }


}
