package com.np.bug.service

import com.np.auth.common.NpAuthentication
import com.np.bug.config.ClonkMenuProperties
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate
import kotlin.math.log


@Service
class ClonkMenuService(val props: ClonkMenuProperties) {

    private val logger: Logger = LoggerFactory.getLogger(ClonkMenuService::class.java)

    private fun postRedeem(auth: NpAuthentication, name: String, input: String? = null) {
        if (auth.principal?.clonkToken == null) {
            logger.info("Not sending $name redeem because token is null!")
            return
        }
        logger.info("Posting: $name with ${input ?: "none"}")
        val headers = HttpHeaders().apply {
            set("Proxy-Authorization", "Bearer ${auth.principal!!.clonkToken}")
            contentType = MediaType.MULTIPART_FORM_DATA
        }
        val body = LinkedMultiValueMap<String, String>()
            .apply {
                set("name", name)
                input?.let {set("input", input)}
            }
        val request = HttpEntity<LinkedMultiValueMap<String, String>>(body, headers)
        val response = RestTemplate().postForObject(props.redeemEndpoint, request, String::class.java)
        logger.info("Response: $response")
    }
    fun userEvent(auth: NpAuthentication, message: String) = postRedeem(auth, "say", message)
    fun submitHeadline(auth: NpAuthentication, headline: String) = postRedeem(auth, "submit headline", headline)
    fun pursueIdolDream(auth: NpAuthentication) = postRedeem(auth, "pursue idol dream")
    fun paletteSwapHair(auth: NpAuthentication, pattern: String) = postRedeem(auth, "palette swap (hair)", pattern)
    fun paletteSwapEyes(auth: NpAuthentication, pattern: String) = postRedeem(auth, "palette swap (eyes)", pattern)
    fun paletteSwapHighlight(auth: NpAuthentication, pattern: String) = postRedeem(auth, "palette swap (highlight)", pattern)
    fun arrow(auth: NpAuthentication, text: String) = postRedeem(auth, "arrow", text)
    fun feedFriend(auth: NpAuthentication, food: String) = postRedeem(auth, "feed friend", food)
    fun talkFriend(auth: NpAuthentication, text: String) = postRedeem(auth, "talk to friend", text)
}