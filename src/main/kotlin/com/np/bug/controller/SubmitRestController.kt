package com.np.bug.controller

import com.np.bug.dto.BugSubmitRequestDto
import com.np.bug.dto.BugSubmitResponseDto
import com.np.bug.entity.BugSubmissionEntity
import com.np.bug.repository.BugSubmissionRepository
import com.np.bug.service.FileStorageService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/submit")
class SubmitRestController(
    val bugRepository: BugSubmissionRepository,
    val fileService: FileStorageService
) {
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun submit(
        request: HttpServletRequest,
        auth: Authentication,
        @ModelAttribute
        req: BugSubmitRequestDto?): ResponseEntity<BugSubmitResponseDto>
    {
        req ?: return badRequest("Body cannot be empty")
        request.getSession(true)
        if (req.file.originalFilename?.split('.')?.last() == "sh") {
            return if (auth.name == "asquared31415") {
                badRequest("Sorry, USER ASQUARED THREE ONE FOUR ONE FIVE, a shell script is not a bug!")
            } else {
                badRequest("Sorry, a shell script is not a bug!")
            }
        }
        validate(req)?.let { return badRequest(it) }
        if (req.file.isEmpty || fileService.isDuplicate(req.file.bytes)) return badRequest("Image empty or already uploaded")
        val filePath = fileService.upload(req.file) ?: return badRequest("Failed to save image")

        try {
            val bug = BugSubmissionEntity(
                filePath,
                req.name,
                req.latin,
                req.confidence,
                req.hint,
                req.detail,
                auth.name,
                req.anonymous
            )
            bugRepository.save(bug)
        } catch (e: Exception) {
            fileService.delete(filePath)
            if (request.session.getAttribute("tamper") == null) {
                request.session.setAttribute("tamper", true)
                return if (auth.name == "asquared31415") {
                    badRequest("What are you up to, user asquared three one four one five? >:3")
                } else {
                    badRequest("What are you up to, user ${auth.name}?")
                }
            }
            throw e
        }
        return ResponseEntity.status(HttpStatus.OK)
            .body(BugSubmitResponseDto(true))
    }

    fun badRequest(reason: String): ResponseEntity<BugSubmitResponseDto> =
        ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(BugSubmitResponseDto(false, reason))

    fun validate(req: BugSubmitRequestDto): String? = when {
        req.name.isNullOrBlank() -> "'name' is required"
        req.latin!= null -> when (req.confidence) {
            null -> "'taxonomyConfidence' is required because 'latinName' is not null"
            !in 0..5 -> "'taxonomyConfidence' needs to be in range [0; 5]"
            else -> null
        }
        else -> null
    }
}