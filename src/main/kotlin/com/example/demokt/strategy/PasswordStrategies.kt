package com.example.demokt.strategy

enum class PasswordStrategies(private val algorithm: (ByteArray, ByteArray) -> String) {
    SHA_256(algorithm@{ bytes, bytes2 ->

        val raw = bytes + bytes2
        val name = "SHA-256"
        try {
            val digest = java.security.MessageDigest.getInstance(name).digest(raw)
            return@algorithm java.util.Base64.getEncoder().encodeToString(digest)
        } catch (e: java.security.NoSuchAlgorithmException) {
            throw Error("Can not get message digest instance with name \"$name\". Cause $e", e)
        }
    });

    fun digest(password: ByteArray, salt: ByteArray):String {
        return algorithm.invoke(password, salt)
    }
}