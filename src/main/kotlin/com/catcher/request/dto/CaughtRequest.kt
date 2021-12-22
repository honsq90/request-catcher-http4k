package com.catcher.request.dto

data class CaughtRequest(
    val timestamp: String,
    val path: String,
    val method: String,
    val source: String,
    val headers: List<CaughtHeader>,
    val body: String,
)