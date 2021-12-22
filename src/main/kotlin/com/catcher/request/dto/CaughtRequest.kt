package com.catcher.request.dto

import java.util.*

data class CaughtRequest(
    val uuid: UUID = UUID.randomUUID(),
    val timestamp: String,
    val path: String,
    val method: String,
    val source: String,
    val headers: List<CaughtHeader>,
    val body: String,
)