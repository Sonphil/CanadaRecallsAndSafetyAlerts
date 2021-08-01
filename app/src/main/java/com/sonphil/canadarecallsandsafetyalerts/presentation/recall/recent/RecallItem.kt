package com.sonphil.canadarecallsandsafetyalerts.presentation.recall.recent

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import com.sonphil.canadarecallsandsafetyalerts.R
import com.sonphil.canadarecallsandsafetyalerts.domain.model.Bookmark
import com.sonphil.canadarecallsandsafetyalerts.domain.model.Category
import com.sonphil.canadarecallsandsafetyalerts.domain.model.ReadStatus
import com.sonphil.canadarecallsandsafetyalerts.domain.model.Recall
import com.sonphil.canadarecallsandsafetyalerts.domain.model.RecallAndBookmarkAndReadStatus
import com.sonphil.canadarecallsandsafetyalerts.ext.formatUTC
import com.sonphil.canadarecallsandsafetyalerts.presentation.AppTheme
import com.sonphil.canadarecallsandsafetyalerts.presentation.onBackgroundSecondary
import com.sonphil.canadarecallsandsafetyalerts.presentation.recall.CategoryResources
import java.text.DateFormat
import java.text.SimpleDateFormat

/**
 * Created by Sonphil on 30-07-21.
 */

@Composable
fun RecallItem(
    itemLiveData: LiveData<RecallAndBookmarkAndReadStatus>,
    dateFormat: DateFormat,
    onItemClicked: (item: RecallAndBookmarkAndReadStatus) -> Unit,
    onBookmarkClicked: (recall: Recall, isCurrentlyBookmarked: Boolean) -> Unit
) {
    val item by itemLiveData.observeAsState()

    item?.let {
        RecallItemCard(it, dateFormat, onItemClicked, onBookmarkClicked)
    }
}

@Composable
private fun RecallItemCard(
    item: RecallAndBookmarkAndReadStatus,
    dateFormat: DateFormat,
    onItemClicked: (item: RecallAndBookmarkAndReadStatus) -> Unit,
    onBookmarkClicked: (recall: Recall, isCurrentlyBookmarked: Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 2.dp)
            .clickable {
                onItemClicked(item)
            },
        elevation = 2.dp
    ) {
        RecallItemCardContent(
            item = item,
            dateFormat = dateFormat,
            onBookmarkClicked = onBookmarkClicked
        )
    }
}

@Composable
private fun RecallItemCardContent(
    item: RecallAndBookmarkAndReadStatus,
    dateFormat: DateFormat,
    onBookmarkClicked: (recall: Recall, isCurrentlyBookmarked: Boolean) -> Unit
) {
    Row {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val categoryResources = CategoryResources(item.recall.category)

                Image(
                    painter = painterResource(id = categoryResources.iconId),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
                )

                Text(
                    text = stringResource(id = categoryResources.labelId),
                    color = MaterialTheme.colors.primary,
                    letterSpacing = 0.sp,
                    style = MaterialTheme.typography.caption
                )

                item.recall.datePublished?.let { date ->
                    Divider(
                        color = MaterialTheme.colors.onBackgroundSecondary.copy(alpha = 0.25f),
                        modifier = Modifier
                            .height(20.dp)
                            .padding(top = 1.dp, bottom = 1.dp)
                            .width(1.dp)
                    )

                    Text(
                        text = dateFormat.formatUTC(date),
                        color = MaterialTheme.colors.onBackgroundSecondary,
                        letterSpacing = 0.sp,
                        style = MaterialTheme.typography.caption
                    )
                }
            }

            Text(
                text = item.recall.title.orEmpty(),
                modifier = Modifier.padding(start = 16.dp, bottom = 16.dp),
                fontWeight = if (item.readStatus == null) {
                    FontWeight.Bold
                } else {
                    null
                },
                style = MaterialTheme.typography.body2
            )
        }

        RecallItemBookmark(item, onBookmarkClicked)
    }
}

@Composable
private fun RecallItemBookmark(
    item: RecallAndBookmarkAndReadStatus,
    onBookmarkClicked: (recall: Recall, isCurrentlyBookmarked: Boolean) -> Unit
) {
    val isItemBookmarked = item.bookmark != null

    IconButton(onClick = { onBookmarkClicked(item.recall, isItemBookmarked) }) {
        Icon(
            imageVector = if (isItemBookmarked) {
                Icons.Filled.Bookmark
            } else {
                Icons.Filled.BookmarkBorder
            },
            contentDescription = if (isItemBookmarked) {
                stringResource(id = R.string.label_action_unbookmark_recall_remove_from_my_recalls)
            } else {
                stringResource(id = R.string.label_action_bookmark_recall_add_to_my_recalls)
            },
            modifier = Modifier.size(24.dp),
            tint = if (isItemBookmarked) {
                MaterialTheme.colors.primary
            } else {
                MaterialTheme.colors.onBackgroundSecondary
            }
        )
    }
}

@Preview(showBackground = false)
@Composable
fun RecallItemPreviewLightTheme() {
    AppTheme(darkTheme = false) {
        RecallItemCard(
            createPreviewItem(isBookmarked = true, isRead = false),
            SimpleDateFormat.getDateInstance(DateFormat.MEDIUM),
            { _ -> },
            { _, _ -> }
        )
    }
}

@Preview(showBackground = false)
@Composable
fun RecallItemPreviewDarkTheme() {
    AppTheme(darkTheme = true) {
        RecallItemCard(
            createPreviewItem(isBookmarked = false, isRead = true),
            SimpleDateFormat.getDateInstance(DateFormat.MEDIUM),
            { _ -> },
            { _, _ -> }
        )
    }
}

private fun createPreviewItem(
    isBookmarked: Boolean,
    isRead: Boolean
) = RecallAndBookmarkAndReadStatus(
    recall = Recall(
        category = Category.CONSUMER_PRODUCT,
        datePublished = System.currentTimeMillis(),
        id = "123",
        title = "Health Canada updates Pfizer-BioNTech and Moderna COVID-19 vaccine labels to include information on myocarditis and pericarditis",
        apiUrl = "foo"
    ),
    bookmark = if (isBookmarked) {
        Bookmark(recallId = "123", date = System.currentTimeMillis() - 1000)
    } else {
        null
    },
    readStatus = if (isRead) {
        ReadStatus("132")
    } else {
        null
    }
)
