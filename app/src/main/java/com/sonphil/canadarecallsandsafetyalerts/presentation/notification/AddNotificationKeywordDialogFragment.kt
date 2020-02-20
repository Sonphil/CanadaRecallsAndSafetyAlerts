package com.sonphil.canadarecallsandsafetyalerts.presentation.notification

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sonphil.canadarecallsandsafetyalerts.R
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_dialog_add_notification_keyword.*
import javax.inject.Inject

class AddNotificationKeywordDialogFragment : BottomSheetDialogFragment() {

    private val viewModel: AddNotificationKeywordViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(AddNotificationKeywordViewModel::class.java)
    }
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)

        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_BottomSheetDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(
        R.layout.fragment_dialog_add_notification_keyword,
        container,
        false
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        edit_text_notification_keyword.requestFocus()

        btn_add_notification_keyword.setOnClickListener {
            viewModel.insertNewKeyword(edit_text_notification_keyword.text.toString())
        }

        viewModel.dismissDialog.observe(viewLifecycleOwner, Observer {
            dismiss()
        })
    }

    companion object {
        const val TAG = "AddNotificationKeywordDialogFragment"

        fun newInstance() = AddNotificationKeywordDialogFragment()
    }
}
