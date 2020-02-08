package com.sonphil.canadarecallsandsafetyalerts.presentation.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sonphil.canadarecallsandsafetyalerts.R
import com.sonphil.canadarecallsandsafetyalerts.ext.openUrlExternal
import dagger.android.support.DaggerFragment
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_more.*

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

        rv_more.setupRecyclerView()
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
        val labels = resources.getStringArray(R.array.array_more_items_labels)
        val moreItems = labels.mapIndexed { index, label ->
            val icon = icons.getResourceId(index, -1)

            MoreItem(icon, label) {
                when (icon) {
                    R.drawable.ic_settings_white_24dp -> {
                        // TODO
                    }
                    R.drawable.ic_report_black_24dp -> openUrlExternal(R.string.url_report)
                    R.drawable.ic_maple_leaf_red_650dp -> openUrlExternal(R.string.url_contact_health_canada)
                    R.drawable.ic_website_white_24dp -> openUrlExternal(R.string.url_website)
                    R.drawable.ic_licence_black_24dp -> openUrlExternal(R.string.url_data_licence)
                    R.drawable.ic_github_white_24dp -> openUrlExternal(R.string.url_source_code)
                }
            }
        }

        icons.recycle()

        return moreItems
    }
}
