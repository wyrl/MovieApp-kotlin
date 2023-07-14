package com.example.movieapp.data.utils

import android.content.Context
import android.content.SharedPreferences.Editor
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class EncryptedDataStore private constructor(fileName: String, context: Context) {

    private var sharedPref: EncryptedSharedPreferences
    private var editor: Editor

    companion object{
        @Volatile
        private var instance: EncryptedDataStore? = null

        fun getInstance(context: Context): EncryptedDataStore{
            if(instance == null){
                synchronized(this){
                    if(instance == null){
                        instance = EncryptedDataStore("preferences", context)
                    }
                }
            }
            return instance as EncryptedDataStore
        }
    }

    init {
        sharedPref = EncryptedSharedPreferences.create(
            fileName,
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        ) as EncryptedSharedPreferences
        editor = sharedPref.edit()
    }

    fun put(key: String, value: String){
        editor.putString(key, value).apply()
    }

    fun get(key: String, defValue: String): String? {
        return sharedPref.getString(key, defValue)
    }

    fun put(key: String, value: Boolean){
        editor.putBoolean(key, value).apply()
    }

    fun get(key: String, defValue: Boolean): Boolean {
        return sharedPref.getBoolean(key, defValue)
    }


}