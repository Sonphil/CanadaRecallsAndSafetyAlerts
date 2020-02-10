package com.sonphil.canadarecallsandsafetyalerts.presentation.notification

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.sonphil.canadarecallsandsafetyalerts.R
import kotlinx.android.synthetic.main.activity_main.*

class NotificationsKeywordsFragment : Fragment(R.layout.fragment_notifications_keywords) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().iv_section_icon.setImageResource(R.drawable.ic_notifications_black_24dp)
    }
}
