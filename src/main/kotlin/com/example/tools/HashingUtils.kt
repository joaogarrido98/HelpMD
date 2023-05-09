package com.example.tools

import io.ktor.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object HashingUtils {
    //create a byte array from secret keyword
    private val hashKey = System.getenv("HASH_KEY").toByteArray()

    //create a secret key from a given byteArray and algorithm
    private val hmacKey = SecretKeySpec(hashKey, "HmacSHA256")

    /**
     * hash a given string using SHA256
     * @param word String, string to be hashed
     * @return hashed string
     */
    fun hash(word: String): String {
        val hmac = Mac.getInstance("HmacSHA256")
        hmac.init(hmacKey)
        return hex(hmac.doFinal(word.toByteArray(Charsets.UTF_8)))
    }
}