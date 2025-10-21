package com.np.bug.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("np.clonk")
class ClonkMenuProperties(
    val redeemEndpoint: String
)