package com.np.bug.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@ConfigurationProperties("np.files")
class FileStorageProperties(
    val root: String
)