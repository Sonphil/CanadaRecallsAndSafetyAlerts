package com.sonphil.canadarecallsandsafetyalerts.presentation.more

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.getResourceIdOrThrow
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sonphil.canadarecallsandsafetyalerts.BuildConfig
import com.sonphil.canadarecallsandsafetyalerts.R
import com.sonphil.canadarecallsandsafetyalerts.databinding.FragmentMoreBinding
import com.sonphil.canadarecallsandsafetyalerts.ext.openUrl
import com.sonphil.canadarecallsandsafetyalerts.ext.openUrlExternal
import com.sonphil.canadarecallsandsafetyalerts.ext.viewLifecycle
import dagger.android.support.DaggerFragment
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator

class MoreFragment : DaggerFragment() {
    private var binding: FragmentMoreBinding by viewLifecycle()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMoreBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvMore.setupRecyclerView()
    }

    private fun RecyclerView.setupRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        setLayoutManager(layoutManager)

        itemAnimator = SlideInLeftAnimator()

        setHasFixedSize(true)

        val moreAdapter = MoreAdapter(createMoreItems())
        moreAdapter.setHasStableIds(true)

        adapter = moreAdapter
    }

    private fun createMoreItems(): List<MoreItem> {
        val icons = resources.obtainTypedArray(R.array.array_more_items_icons)
        val labels = resources.obtainTypedArray(R.array.array_more_items_labels)
        val descriptions = resources.obtainTypedArray(R.array.array_more_items_descriptions)
        val count = labels.length()
        val moreItems = mutableListOf<MoreItem>()

        for (i in 0 until count) {
            val labelResId = labels.getResourceIdOrThrow(i)

            val moreItem = MoreItem(
                icons.getResourceIdOrThrow(i),
                labelResId,
                descriptions.getResourceIdOrThrow(i)
            ) {
                when (labelResId) {
                    R.string.title_settings -> findNavController().navigate(R.id.fragment_preference)
                    R.string.title_report -> openUrlExternal(R.string.url_report)
                    R.string.title_contact_health_canada -> openUrlExternal(R.string.url_contact_health_canada)
                    R.string.title_website -> openUrlExternal(R.string.url_website)
                    R.string.title_motor_vehicle_safety_recalls_database -> openUrlExternal(R.string.url_motor_vehicle_safety_recalls_database)
                    R.string.title_data_licence -> openUrl(R.string.url_data_licence)
                    R.string.title_source_code -> openUrl(R.string.url_source_code)
                    R.string.title_contact_dev -> composeEmailDev()
                }
            }

            moreItems.add(moreItem)
        }

        icons.recycle()
        labels.recycle()
        descriptions.recycle()

        return moreItems
    }

    private fun composeEmailDev() {
        val email = getString(R.string.email_dev)
        val text = String.format(
            getString(R.string.text_body_contact_dev),
            BuildConfig.VERSION_NAME,
            "${Build.MANUFACTURER} ${Build.MODEL}",
            Build.VERSION.RELEASE
        )
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            putExtra(Intent.EXTRA_TEXT, text)
        }

        if (intent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(intent)
        }
    }
}
