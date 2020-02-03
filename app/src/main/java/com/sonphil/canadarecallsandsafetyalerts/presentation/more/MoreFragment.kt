package com.sonphil.canadarecallsandsafetyalerts.presentation.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sonphil.canadarecallsandsafetyalerts.R
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.activity_main.*

class MoreFragment : DaggerFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_more, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().iv_section_icon.setImageDrawable(null)
    }
}
