package com.example.annotationdemo.util

import java.math.BigInteger
import java.security.MessageDigest

/**
 * Author: FlyWei
 * E-mail: tony91097@gmail.com
 * Date: 2021/12/21
 */

object MDUtil {
    fun encode(type: Type, data: String): String {
        return try {
            val digest = MessageDigest.getInstance(type.value)
            digest.update(data.toByteArray())
            //32位補0
            String.format("%032X", BigInteger(1, digest.digest()))
        } catch (e: Exception) {
            data
        }
    }

    enum class Type(val value: String) {
        MD5("MD5"), SHA256("SHA-256"), SHA512("SHA-512");
    }
}