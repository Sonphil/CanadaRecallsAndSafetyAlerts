package com.sonphil.canadarecallsandsafetyalerts.presentation.recall.details

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.sonphil.canadarecallsandsafetyalerts.domain.entity.RecallImage

/**
 * Created by Sonphil on 29-04-20.
 */

fun ImageView.loadWithGlide(recallImage: RecallImage) {
    val thumbnailRequest: RequestBuilder<Drawable> = Glide
        .with(this)
        .load(recallImage.thumbUrl)

    Glide.with(this)
        .load(recallImage.fullUrl)
        .thumbnail(thumbnailRequest)
        .into(this)
}
