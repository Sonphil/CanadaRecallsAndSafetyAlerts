package com.sonphil.canadarecallsandsafetyalerts.presentation.notification

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sonphil.canadarecallsandsafetyalerts.R
import com.sonphil.canadarecallsandsafetyalerts.databinding.FragmentDialogAddNotificationKeywordBinding
import com.sonphil.canadarecallsandsafetyalerts.ext.viewLifecycle
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class AddNotificationKeywordDialogFragment : BottomSheetDialogFragment() {
    private var binding: FragmentDialogAddNotificationKeywordBinding by viewLifecycle()
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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDialogAddNotificationKeywordBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupEditText()

        binding.btnAddNotificationKeyword.setOnClickListener {
            viewModel.insertNewKeyword(binding.editTextNotificationKeyword.text.toString())
        }

        viewModel.dismissDialog.observe(
            viewLifecycleOwner,
            Observer {
                dismiss()
            }
        )
    }

    private fun setupEditText() {
        binding.editTextNotificationKeyword.setOnEditorActionListener { textView, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    viewModel.insertNewKeyword(textView.text.toString())

                    true
                }
                else -> false
            }
        }

        binding.editTextNotificationKeyword.requestFocus()
    }

    companion object {
        const val TAG = "AddNotificationKeywordDialogFragment"

        fun newInstance() = AddNotificationKeywordDialogFragment()
    }
}
