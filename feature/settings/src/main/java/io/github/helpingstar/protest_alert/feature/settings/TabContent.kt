package io.github.helpingstar.protest_alert.feature.settings

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.helpingstar.protest_alert.core.model.data.FollowableRegion
import io.github.helpingstar.protest_alert.core.model.data.Region
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

private val CheckedColor = Color(0xFF3899FA)
private val UncheckedBorderColor = Color(0xFF565D6D)
private val TextColor = Color(0xFF171A1F)

private const val TAG = "TabContent"

@Composable
fun RegionsTabContent(
    regions: List<FollowableRegion>,
    onFollowButtonClick: (Long, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Log.d(TAG, "${regions}")
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
    ) {
        Text(
            text = "관심 지역 선택",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = TextColor,
            lineHeight = 24.sp,
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            items(
                items = regions,
                key = { it.region.id }
            ) { followableRegion ->
                RegionCheckboxItem(
                    regionName = followableRegion.region.name,
                    isFollowed = followableRegion.isFollowed,
                    onCheckedChange = { checked ->
                        onFollowButtonClick(followableRegion.region.id, checked)
                    },
                )
            }
        }
    }
}

@Composable
private fun RegionCheckboxItem(
    regionName: String,
    isFollowed: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = isFollowed,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = CheckedColor,
                uncheckedColor = UncheckedBorderColor,
                checkmarkColor = Color.White,
            ),
        )
        Text(
            text = regionName,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = TextColor,
            lineHeight = 22.sp,
        )
    }
}

@OptIn(ExperimentalTime::class)
@Preview(showBackground = true)
@Composable
private fun RegionsTabContentPreview() {
    val sampleRegions = listOf(
        FollowableRegion(Region(1, "서울", Instant.DISTANT_PAST), isFollowed = true),
        FollowableRegion(Region(2, "경기", Instant.DISTANT_PAST), isFollowed = false),
        FollowableRegion(Region(3, "인천", Instant.DISTANT_PAST), isFollowed = false),
        FollowableRegion(Region(4, "부산", Instant.DISTANT_PAST), isFollowed = false),
        FollowableRegion(Region(5, "대구", Instant.DISTANT_PAST), isFollowed = false),
        FollowableRegion(Region(6, "대전", Instant.DISTANT_PAST), isFollowed = false),
        FollowableRegion(Region(7, "광주", Instant.DISTANT_PAST), isFollowed = false),
        FollowableRegion(Region(8, "울산", Instant.DISTANT_PAST), isFollowed = false),
        FollowableRegion(Region(9, "세종", Instant.DISTANT_PAST), isFollowed = false),
        FollowableRegion(Region(10, "강원", Instant.DISTANT_PAST), isFollowed = false),
        FollowableRegion(Region(11, "충북", Instant.DISTANT_PAST), isFollowed = false),
        FollowableRegion(Region(12, "충남", Instant.DISTANT_PAST), isFollowed = false),
        FollowableRegion(Region(13, "전북", Instant.DISTANT_PAST), isFollowed = false),
        FollowableRegion(Region(14, "전남", Instant.DISTANT_PAST), isFollowed = false),
        FollowableRegion(Region(15, "경북", Instant.DISTANT_PAST), isFollowed = false),
        FollowableRegion(Region(16, "경남", Instant.DISTANT_PAST), isFollowed = false),
        FollowableRegion(Region(17, "제주", Instant.DISTANT_PAST), isFollowed = false),
    )

    RegionsTabContent(
        regions = sampleRegions,
        onFollowButtonClick = { _, _ -> },
    )
}
