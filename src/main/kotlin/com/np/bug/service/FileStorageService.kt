package com.np.bug.service

import com.np.bug.config.FileStorageProperties
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.security.MessageDigest
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.io.path.absolutePathString
import kotlin.uuid.Uuid

@Service
class FileStorageService(props: FileStorageProperties) {

    private val logger = LoggerFactory.getLogger(FileStorageService::class.java)
    private val cache = ConcurrentHashMap<String, Boolean>()
    private val root: Path = Paths.get(props.root)

    init {
        logger.info("Initializing file hash cache...")
        val dir = File(root.absolutePathString())
        val list = dir.listFiles()?.filter{it.isFile} ?: emptyList()
        for (file in list) {
            markSubmitted(file.readBytes())
        }
        logger.info("Done for ${list.size} files!")
    }

    fun delete(name: String) {
        val path = root.resolve(name);
        cache.remove(hash(Files.readAllBytes(path)))
        Files.delete(root.resolve(name))
    }

    fun upload(file: MultipartFile): String? {
        if (isDuplicate(file.bytes)) return null;

        val filename = UUID.randomUUID().toString()
        val destination = if (file.originalFilename?.contains(".") ?: false) {
            val extension = file.originalFilename!!.split('.').last()
            root.resolve("$filename.$extension")
        } else {
            root.resolve(filename)
        }
        Files.copy(file.inputStream, destination)
        markSubmitted(file.bytes)
        return destination.fileName.toString()
    }

    fun isDuplicate(file: ByteArray): Boolean = cache.containsKey(hash(file))
    fun markSubmitted(file: ByteArray) { cache[hash(file)] = true }
    private fun hash(bytes: ByteArray): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(bytes)
        return hashBytes.joinToString("") { "%02x".format(it) }
    }

}