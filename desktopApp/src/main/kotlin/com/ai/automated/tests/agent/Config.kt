package com.ai.automated.tests.agent

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.attribute.PosixFilePermissions
import java.security.SecureRandom
import java.util.Base64

/** Everything the app persists lives under one Application Support directory. */
object AppPaths {
    val dir: Path = Path.of(System.getProperty("user.home"), "Library", "Application Support", "AIHelper")
    val config: Path get() = dir.resolve("config.json")
    val daemon: Path get() = dir.resolve("daemon.json")
    val binDir: Path get() = dir.resolve("bin")
    val shim: Path get() = binDir.resolve("aihelper")
    val services: Path = Path.of(System.getProperty("user.home"), "Library", "Services")
    val models:Path get() = dir.resolve("models.json")

    fun ensure() {
        Files.createDirectories(dir)
        Files.createDirectories(binDir)
    }
}

@Serializable
data class CustomAction(
    val id: String,
    val title: String,
    val instruction: String,
    val composes: Boolean = false,
)

@Serializable
data class Config(
    /** Blank means "fall back to ANTHROPIC_API_KEY or an `ant auth login` profile". */
    val apiKey: String = "",
    var model: String = "openai/gpt-4o-mini",//"claude-opus-5",
    /** low | medium | high | xhigh | max */
    val effort: String = "low",
    val port: Int = 8787,
    val authToken: String = "",
    val customActions: List<CustomAction> = emptyList(),
) {
    /** True when a key is configured here or discoverable from the environment. */
    fun hasCredentials(): Boolean =
        apiKey.isNotBlank() ||
            !System.getenv("ANTHROPIC_API_KEY").isNullOrBlank() ||
            !System.getenv("ANTHROPIC_AUTH_TOKEN").isNullOrBlank() ||
            Files.isDirectory(Path.of(System.getProperty("user.home"), ".config", "anthropic"))
}

@Serializable
data class DaemonInfo(val port: Int, val pid: Long, val startedAt: Long)

@Serializable
data class OpenRouterModel(val id:String, val name:String, val description:String, var price: Double=0.0)

class ConfigStore {
    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    @Volatile
    var current: Config = load()
        private set

    fun load(): Config {
        AppPaths.ensure()
        val cfg = runCatching {
            if (Files.exists(AppPaths.config)) json.decodeFromString<Config>(Files.readString(AppPaths.config))
            else Config()
        }.getOrElse { Config() }

        // Mint the loopback auth token on first run.
        return if (cfg.authToken.isBlank()) cfg.copy(authToken = newToken()).also { save(it) } else cfg
    }

    fun save(cfg: Config) {
        AppPaths.ensure()
        Files.writeString(AppPaths.config, json.encodeToString(cfg))
        // The file holds an API key and the loopback token: owner-only.
        runCatching { Files.setPosixFilePermissions(AppPaths.config, PosixFilePermissions.fromString("rw-------")) }
        current = cfg
    }

    fun writeDaemonInfo(port: Int) {
        AppPaths.ensure()
        val info = DaemonInfo(port, ProcessHandle.current().pid(), System.currentTimeMillis())
        Files.writeString(AppPaths.daemon, json.encodeToString(info))
    }

    fun readDaemonInfo(): DaemonInfo? = runCatching {
        json.decodeFromString<DaemonInfo>(Files.readString(AppPaths.daemon))
    }.getOrNull()

    fun clearDaemonInfo() {
        runCatching { Files.deleteIfExists(AppPaths.daemon) }
    }

    fun readModels(): String = if (Files.exists(AppPaths.models)) Files.readString(AppPaths.models) else ""

    fun writeModels(models: List<OpenRouterModel>) {
        AppPaths.ensure()
        Files.writeString(AppPaths.models, json.encodeToString(models))
    }

    private fun newToken(): String {
        val bytes = ByteArray(32)
        SecureRandom().nextBytes(bytes)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
    }
}
