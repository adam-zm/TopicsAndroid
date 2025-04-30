package com.example.topics

import kotlinx.serialization.Serializable

@Serializable
data class Comment(
    var id: Int? = null,
    var content: String,
    var topic_id: Int,
    var created_at: String? = null
)