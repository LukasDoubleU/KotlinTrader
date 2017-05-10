package com.doubleu.kotlintrader.model

import com.doubleu.kotlintrader.database.Entity
import java.security.MessageDigest


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

class Trader(override val id: Int) : Entity() {

    var name: String? by delegate(this::name)
    val nameProperty = property(this::name)

    var pass: String? by delegate(this::pass)
    val passProperty = property(this::pass)

    var geld: Double? by delegate(this::geld)
    val geldProperty = property(this::geld)

    var master: Boolean? by delegate(this::master)
    val masterProperty = property(this::master)

    fun checkPw(s: String) = encrypt(s) == pass

}