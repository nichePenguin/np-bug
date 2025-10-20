package com.np.bug.repository

import com.np.bug.entity.BugEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BugRepository : JpaRepository<BugEntity, Long> {
}