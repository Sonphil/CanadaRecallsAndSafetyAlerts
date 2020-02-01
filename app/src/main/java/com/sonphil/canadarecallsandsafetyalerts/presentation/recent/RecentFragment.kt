package com.sonphil.canadarecallsandsafetyalerts.presentation.recent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sonphil.canadarecallsandsafetyalerts.R
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class RecentFragment : DaggerFragment() {

    private val viewModel: RecentViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(RecentViewModel::class.java)
    }
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recent, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeUI()
    }

    private fun subscribeUI() {
        viewModel.recentRecalls.observe(viewLifecycleOwner, Observer {

        })

        viewModel.loading.observe(viewLifecycleOwner, Observer {
        })

        viewModel.error.observe(viewLifecycleOwner, Observer {
            
        })
    }
}
