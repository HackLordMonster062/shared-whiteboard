package com.hacklord.security

data class SaltedHash(
    val hash: String,
    val salt: String
)
