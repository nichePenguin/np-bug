package com.np.bug.entity

import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import java.time.Instant

@Entity
class BugEntity(
    val image: String,
    val name: String,
    val latin: String?,
    val taxonomyConfidence: Int,
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "bug_entity_tags",
        joinColumns = [JoinColumn(name = "bug_id")]
    )
    @Column(name = "tag", length = 100)
    val tags: List<String> = emptyList(),
    val submittedBy: String,

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val public: Boolean = false,
    val submittedAt: Instant = Instant.now(),
)