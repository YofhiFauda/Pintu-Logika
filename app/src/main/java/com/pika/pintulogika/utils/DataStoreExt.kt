package com.pika.pintulogika.utils

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.userDataStore by preferencesDataStore(name = "user_session")