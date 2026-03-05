package com.example.colorphone.domain.repository

import androidx.datastore.core.DataStore
import java.util.prefs.Preferences
import javax.inject.Inject

class SettingRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {

    }
}