package com.doubleu.kotlintrader.util

import java.security.MessageDigest

object MD5 {

    private val encrypter = MessageDigest.getInstance("MD5")!!
    fun encrypt(s: String) = toHexString(encrypter.digest(s.toByteArray())!!)
    fun toHexString(bytes: ByteArray): String {
        val hexString = StringBuilder()
        for (byte in bytes) {
            val hex = Integer.toHexString(0xFF and byte.toInt())
            if (hex.length == 1) {
                hexString.append('0')
            }
            hexString.append(hex)
        }
        return hexString.toString()
    }

}