package dev.somnath.onlynews.data.prefrences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class AppPrefrences (
    context: Context
){


    private val applicationContext = context.applicationContext
    private val datastore: DataStore<Preferences>

    init{
        datastore = applicationContext.createDataStore(
            name = "dev.somnath.onlynews"
        )
    }

    val viewtype: Flow<String?>
    get() = datastore.data.map {
        it[KEY_VIEWTYPE]
    }

    val theme: Flow<String?>
    get() = datastore.data.map {
        it[KEY_THEME]
    }

    suspend fun saveViewType(type: String){
        datastore.edit {
            it[KEY_VIEWTYPE] = type
        }
    }

    suspend fun saveUserTheme(theme: String){
        datastore.edit {
            it[KEY_THEME] = theme
        }
    }

    companion object{
        private val KEY_VIEWTYPE = preferencesKey<String>("View_Type")
        private val KEY_THEME = preferencesKey<String>("User_Theme")
    }
}