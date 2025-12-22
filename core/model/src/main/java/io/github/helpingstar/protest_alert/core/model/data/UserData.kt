package io.github.helpingstar.protest_alert.core.model.data

data class UserData(
    val followedRegions: Set<String>,
    val shouldHideOnboarding: Boolean,
)