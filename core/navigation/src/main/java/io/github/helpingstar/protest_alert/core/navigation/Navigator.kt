package io.github.helpingstar.protest_alert.core.navigation

import androidx.navigation3.runtime.NavKey

class Navigator(val state: NavigationState) {
    fun navigate(key: NavKey) {
        goToLevel(key)
    }

    fun goBack() {
        state.topLevelStack.removeLastOrNull()
    }

    private fun goToLevel(key: NavKey) {
        state.topLevelStack.apply {
            if (key == state.startKey) {
                clear()
            } else {
                remove(key)
            }
            add(key)
        }
    }
}
