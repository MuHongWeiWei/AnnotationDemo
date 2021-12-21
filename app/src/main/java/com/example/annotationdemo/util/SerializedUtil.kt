package com.example.annotationdemo.util

import android.text.TextUtils
import android.util.Base64
import com.example.annotationdemo.annotation.SerializedEncryption
import com.example.annotationdemo.annotation.SerializedTrim
import java.io.File
import java.lang.reflect.Field
import java.util.*

/**
 * Author: FlyWei
 * E-mail: tony91097@gmail.com
 * Date: 2021/12/21
 */
object SerializedUtil {

    fun convertToRequestContent(model: Any?, field: Field): Array<Any>? {
        val key = field.name
        val originalValue = field[model]

        if (TextUtils.isEmpty(key) || originalValue == null) {
            return null
        }

        return if (originalValue is File) {
            arrayOf(key, originalValue)
        } else {
            var value = originalValue.toString()

            val serializedTrim = field.getAnnotation(SerializedTrim::class.java)
            if (null != serializedTrim) {
                value = value.replace("\\s*".toRegex(), "")
            }

            val serializedEncryption = field.getAnnotation(SerializedEncryption::class.java)
            if (null != serializedEncryption) {
                value = when (serializedEncryption.type) {
                    "MD5" -> {
                        MDUtil.encode(MDUtil.Type.MD5, value).uppercase(Locale.getDefault())
                    }
                    "SHA256" -> {
                        MDUtil.encode(MDUtil.Type.SHA256, value).uppercase(Locale.getDefault())
                    }
                    "SHA512" -> {
                        MDUtil.encode(MDUtil.Type.SHA512, value).uppercase(Locale.getDefault())
                    }
                    else -> {
                        Base64.encodeToString(value.toByteArray(), Base64.DEFAULT)
                    }
                }
            }

            arrayOf(key, value)
        }
    }
}