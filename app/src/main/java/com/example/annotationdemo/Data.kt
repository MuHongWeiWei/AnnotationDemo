package com.example.annotationdemo

import com.example.annotationdemo.annotation.SerializedEncryption
import com.example.annotationdemo.annotation.SerializedTrim

/**
 * Author: FlyWei
 * E-mail: tony91097@gmail.com
 * Date: 2021/12/21
 */
data class Data(
    @SerializedTrim @SerializedEncryption(type = "SHA256")
    val sha256: String,
    @SerializedTrim @SerializedEncryption(type = "MD5")
    val md5: String,
    @SerializedTrim @SerializedEncryption
    val base64: String
)