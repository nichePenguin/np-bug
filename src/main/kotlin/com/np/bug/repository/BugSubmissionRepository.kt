package com.np.bug.repository

import com.np.bug.entity.BugSubmissionEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BugSubmissionRepository : JpaRepository<BugSubmissionEntity, Long> {
}