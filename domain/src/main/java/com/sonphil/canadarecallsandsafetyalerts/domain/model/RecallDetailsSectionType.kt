package com.sonphil.canadarecallsandsafetyalerts.domain.model

/**
 * Created by Sonphil on 07-03-20.
 */

enum class RecallDetailsSectionType {
    /** A short summary of the recall **/
    BASIC_DETAILS,
    /** Details about the issue. May not be present in every recall. **/
    CMS_ISSUE_PROBLEM,
    /** Instructions to consumers (ie, to return a product to the retailer.) Usually present in consumer product recalls. **/
    CMS_WHO_WHAT_CONSUMER,
    /** Details about the issue. May not be present in every recall. **/
    DETAILS,
    /** Details about the affected product **/
    PRODUCT,
    /** References to other information about the topic **/
    MORE_INFORMATION,
    /** Contact information for media enquiries **/
    MEDIA_ENQUIRIES,
    /** Contact information for public enquiries **/
    PUBLIC_ENQUIRIES,
    /** ID number for the recall **/
    ID_NUMBERS,
    IMAGES,
    OTHER
}
