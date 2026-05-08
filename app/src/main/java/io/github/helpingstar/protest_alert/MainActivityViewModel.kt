package io.github.helpingstar.protest_alert

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.helpingstar.protest_alert.core.data.repository.AnnouncementRepository
import io.github.helpingstar.protest_alert.core.model.data.Announcement
import io.github.helpingstar.protest_alert.core.model.data.AnnouncementType
import io.github.helpingstar.protest_alert.ui.component.AnnouncementUiModel
import io.github.helpingstar.protest_alert.ui.component.NotificationStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val announcementRepository: AnnouncementRepository,
) : ViewModel() {

    private val _showAnnouncementSheet = MutableStateFlow(false)
    val showAnnouncementSheet: StateFlow<Boolean> = _showAnnouncementSheet.asStateFlow()

    val announcements: StateFlow<List<AnnouncementUiModel>> =
        announcementRepository.getAnnouncements()
            .map { announcements -> announcements.map(Announcement::toUiModel) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    val hasUnreadAnnouncements: StateFlow<Boolean> =
        announcementRepository.hasUnreadAnnouncements()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = false
            )

    fun openAnnouncementSheet() {
        _showAnnouncementSheet.value = true
    }

    fun closeAnnouncementSheet() {
        _showAnnouncementSheet.value = false
    }

    fun markAsRead(id: String) {
        viewModelScope.launch {
            announcementRepository.markAsRead(id)
        }
    }

    fun markAllAsRead() {
        viewModelScope.launch {
            announcementRepository.markAllAsRead()
        }
    }
}

private fun Announcement.toUiModel(): AnnouncementUiModel {
    val date = startAt.toLocalDateTime(TimeZone.currentSystemDefault()).date
    val dateString = "${date.year}.${date.month.number.toString().padStart(2, '0')}.${
        date.day.toString().padStart(2, '0')
    }"

    return AnnouncementUiModel(
        id = id,
        status = when (type) {
            AnnouncementType.EMERGENCY -> NotificationStatus.EMERGENCY
            AnnouncementType.PINNED -> NotificationStatus.PINNED
            AnnouncementType.NORMAL -> NotificationStatus.NORMAL
        },
        title = title,
        body = body,
        date = dateString,
        isRead = isRead
    )
}
