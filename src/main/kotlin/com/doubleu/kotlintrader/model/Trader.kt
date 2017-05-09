package com.doubleu.trader

import tornadofx.*
import java.security.MessageDigest


private val encrypter = MessageDigest.getInstance("MD5")!!
fun encrypt(s: String)= toHexString(encrypter.digest(s.toByteArray())!!)
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

class Trader(override val id: Int) : Entity() {

    var name by DatabaseDelegate<String>()
    val nameProperty = property(name)
    var pass by DatabaseDelegate<String>()
    var geld by DatabaseDelegate<Double>()
    var master by DatabaseDelegate<Boolean>()

    fun checkPw(s: String) = encrypt(s) == pass

}