package com.thatwaz.shoppuppet.domain.model



data class ItemUiModel(
    val itemId: Long,
    val itemName: String,
    val shopNames: List<Shop>,
    var isPriorityItem: Boolean,
    var isPurchased: Boolean = false,
    )


