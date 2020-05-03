package com.sonphil.canadarecallsandsafetyalerts.presentation.recall.details

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.sonphil.canadarecallsandsafetyalerts.data.entity.RecallImage
import technolifestyle.com.imageslider.FlipperView

/**
 * Created by Sonphil on 29-04-20.
 */

private fun ImageView.loadWithGlide(recallImage: RecallImage) {
    val thumbnailRequest: RequestBuilder<Drawable> = Glide
        .with(this)
        .load(recallImage.thumbUrl)

    Glide.with(this)
        .load(recallImage.fullUrl)
        .thumbnail(thumbnailRequest)
        .into(this)
}

fun List<RecallImage>?.toFlipperViews(context: Context): List<FlipperView> {
    return this.orEmpty()
        .map { recallImage ->
            FlipperView(context).apply {
                setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                setDescriptionBackgroundAlpha(0f)
                fun setFlipperImage(flipperImageView: ImageView, imageUrl: String) {
                    flipperImageView.loadWithGlide(recallImage)
                }
                setImageUrl(recallImage.fullUrl, ::setFlipperImage)
            }
        }
}