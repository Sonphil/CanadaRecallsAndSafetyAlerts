package com.sonphil.canadarecallsandsafetyalerts.domain.model

/**
 * Created by Sonphil on 01-02-20.
 */

data class RecallAndBasicInformationAndDetailsSectionsAndImages(
    val recall: Recall,
    val basicInformation: RecallDetailsBasicInformation?,
    val detailsSections: List<RecallDetailsSection>?,
    val images: List<RecallImage>?
)
