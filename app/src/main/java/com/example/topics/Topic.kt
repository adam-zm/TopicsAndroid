package com.example.topics

import kotlinx.serialization.Serializable

@Serializable
data class Topic(
    var id: Int,
    var created_at: String? = null,
    var title: String,
    var text: String
)
