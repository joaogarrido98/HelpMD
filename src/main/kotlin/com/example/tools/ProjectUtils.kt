package com.example.tools

object ProjectUtils {

    /**
     * generate a random code with 30 characters with any letter, number or special char
     * @return a random code
     */
    fun generateRandomCode(): String {
        val allNumbersAndLetters = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        var code = ""
        for (i in 0..29) {
            code += allNumbersAndLetters.random()
        }
        return code
    }
}