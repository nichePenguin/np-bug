package com.np.bug.dto

import org.springframework.web.multipart.MultipartFile

data class BugSubmitRequestDto(
    val file: MultipartFile,
    val name: String?,
    val latin: String?,
    val hint: String?,
    val confidence: Int?,
    val detail: String?,
    val anonymous: Boolean
)