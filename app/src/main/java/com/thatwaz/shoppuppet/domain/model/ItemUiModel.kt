package com.thatwaz.shoppuppet.domain.model


// todo need to explain this
data class ItemUiModel(
    val itemId: Long,
    val itemName: String,
    val shopNames: List<Shop>,
    val isPriorityItem: Boolean,
    var isPurchased: Boolean = false,


    )


