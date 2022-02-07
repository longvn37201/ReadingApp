package vulong.book_app.util

import android.content.Context
import android.content.SharedPreferences
import vulong.book_app.util.Constant.SHARED_PREFERENCE_NAME


object SharedPrefUtils {

    fun getBooleanData(context: Context, key: String?, defaultBoolean: Boolean = false): Boolean =
        context
            .getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
            .getBoolean(key, defaultBoolean)


    fun getIntData(context: Context, key: String?, defaultInt: Int = 0): Int =
        context
            .getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
            .getInt(key, defaultInt)


    fun getStringData(context: Context, key: String?, defaultString: String=""): String? =
        context
            .getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
            .getString(key, defaultString)


    fun saveString(context: Context, key: String?, data: String?) {
        context
            .getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE).edit()
            .putString(key, data).apply()
    }

    fun saveInt(context: Context, key: String?, data: Int) {
        context
            .getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE).edit()
            .putInt(key, data)
            .apply()
    }

    fun saveBoolean(context: Context, key: String?, data: Boolean) {
        context
            .getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(key, data)
            .apply()
    }

    fun getSharedPrefEditor(context: Context, pref: String?): SharedPreferences.Editor {
        return context
            .getSharedPreferences(pref, Context.MODE_PRIVATE).edit()
    }


}