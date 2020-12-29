package com.sonphil.canadarecallsandsafetyalerts.domain.model

/**
 * Created by Sonphil on 21-02-20.
 */

data class RecallDetailsSection(
    val recallId: String,
    val panelName: String,
    val type: RecallDetailsSectionType,
    val title: String,
    val text: String
)
