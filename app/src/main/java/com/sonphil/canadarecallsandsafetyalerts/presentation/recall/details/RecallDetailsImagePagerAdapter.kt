package com.sonphil.canadarecallsandsafetyalerts.presentation.recall.details

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.sonphil.canadarecallsandsafetyalerts.databinding.ItemRecallDetailsImageBinding
import com.sonphil.canadarecallsandsafetyalerts.domain.model.RecallImage

/**
 * Created by Sonphil on 20-06-20.
 */

class RecallDetailsImagePagerAdapter() : PagerAdapter() {
    var recallImages: List<RecallImage> = emptyList()

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount() = recallImages.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater = container
            .context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemBinding = ItemRecallDetailsImageBinding.inflate(layoutInflater)
        val itemView = itemBinding.root
        val imageView = itemBinding.ivRecallDetailsImage

        imageView.loadWithGlide(recallImages[position])
        container.addView(itemView)

        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}
