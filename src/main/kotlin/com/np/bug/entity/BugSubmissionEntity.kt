package com.np.bug.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.Instant

@Entity
class BugSubmissionEntity (
    val image: String?,
    val name: String?,
    val latin: String?,
    val confidence: Int?,
    val hint: String?,
    val detail: String?,
    val submittedBy: String?,
    val anonymous: Boolean,

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val submittedAt: Instant? = Instant.now(),
)