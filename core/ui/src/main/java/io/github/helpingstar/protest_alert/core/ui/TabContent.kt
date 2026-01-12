package io.github.helpingstar.protest_alert.core.ui

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.helpingstar.protest_alert.core.designsystem.theme.fontFamily
import io.github.helpingstar.protest_alert.core.model.data.FollowableRegion
import io.github.helpingstar.protest_alert.core.model.data.Region
import kotlin.time.Instant

private val AccentColor = Color(0xFF3899FA)
private val SelectedBackground = Color(0xFFEBF5FF)
private val DefaultBackground = Color(0xFFF3F4F6)
private val DefaultTextColor = Color(0xFF374151)
private val TitleTextColor = Color(0xFF171A1F)

private const val TAG = "TabContent"

@Composable
fun RegionsTabContent(
    title: String,
    regions: List<FollowableRegion>,
    onFollowButtonClick: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Log.d(TAG, "$regions")
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontFamily = fontFamily,
            fontWeight = FontWeight.SemiBold,
            color = TitleTextColor,
            lineHeight = 24.sp,
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(
                items = regions,
                key = { it.region.id }
            ) { followableRegion ->
                SelectableRegionChip(
                    regionName = followableRegion.region.name,
                    isSelected = followableRegion.isFollowed,
                    onClick = {
                        onFollowButtonClick(
                            followableRegion.region.id,
                            !followableRegion.isFollowed
                        )
                    },
                )
            }
        }
    }
}

@Composable
private fun SelectableRegionChip(
    regionName: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = if (isSelected) SelectedBackground else DefaultBackground
    val contentColor = if (isSelected) AccentColor else DefaultTextColor
    val borderStroke = if (isSelected) {
        BorderStroke(width = 2.dp, color = AccentColor)
    } else {
        null
    }

    Surface(
        onClick = onClick,
        modifier = modifier.height(44.dp),
        shape = RoundedCornerShape(22.dp),
        color = backgroundColor,
        border = borderStroke,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = contentColor,
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(
                text = regionName,
                fontSize = 14.sp,
                fontFamily = fontFamily,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                color = contentColor,
                lineHeight = 20.sp,
            )
        }
    }
}


@Preview(
    name = "Small width (320dp)",
    widthDp = 390,
    heightDp = 640,
    showBackground = true
)
@Composable
private fun RegionsTabContentPreview() {
    val sampleRegions = listOf(
        FollowableRegion(Region("서울", "서울", Instant.DISTANT_PAST), isFollowed = true),
        FollowableRegion(Region("경기", "경기북부", Instant.DISTANT_PAST), isFollowed = false),
        FollowableRegion(Region("인천", "인천", Instant.DISTANT_PAST), isFollowed = false),
        FollowableRegion(Region("부산", "부산", Instant.DISTANT_PAST), isFollowed = false),
        FollowableRegion(Region("대구", "대구", Instant.DISTANT_PAST), isFollowed = false),
        FollowableRegion(Region("대전", "대전", Instant.DISTANT_PAST), isFollowed = false),
        FollowableRegion(Region("광주", "광주", Instant.DISTANT_PAST), isFollowed = false),
        FollowableRegion(Region("울산", "울산", Instant.DISTANT_PAST), isFollowed = false),
        FollowableRegion(Region("세종", "세종", Instant.DISTANT_PAST), isFollowed = false),
        FollowableRegion(Region("강원", "강원", Instant.DISTANT_PAST), isFollowed = false),
        FollowableRegion(Region("충북", "충북", Instant.DISTANT_PAST), isFollowed = false),
        FollowableRegion(Region("충남", "충남", Instant.DISTANT_PAST), isFollowed = false),
        FollowableRegion(Region("전북", "전북", Instant.DISTANT_PAST), isFollowed = false),
        FollowableRegion(Region("전남", "전남", Instant.DISTANT_PAST), isFollowed = false),
        FollowableRegion(Region("경북", "경북", Instant.DISTANT_PAST), isFollowed = false),
        FollowableRegion(Region("경남", "경남", Instant.DISTANT_PAST), isFollowed = false),
        FollowableRegion(Region("제주", "제주", Instant.DISTANT_PAST), isFollowed = false),
    )

    RegionsTabContent(
        title = "관심 지역 선택",
        regions = sampleRegions,
        onFollowButtonClick = { _, _ -> },
    )
}
