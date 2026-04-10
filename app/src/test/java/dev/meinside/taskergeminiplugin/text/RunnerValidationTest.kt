package dev.meinside.taskergeminiplugin.text

import dev.meinside.taskergeminiplugin.R
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class RunnerValidationTest {

    @Test
    fun validInput_returnsNull() {
        assertNull(Runner.validateInput("gemini-2.5-flash", "api-key-123", "Hello world"))
    }

    @Test
    fun emptyModel_returnsModelError() {
        assertEquals(R.string.error_message_no_model, Runner.validateInput("", "api-key-123", "Hello world"))
    }

    @Test
    fun blankModel_returnsModelError() {
        assertEquals(R.string.error_message_no_model, Runner.validateInput("   ", "api-key-123", "Hello world"))
    }

    @Test
    fun emptyApiKey_returnsApiKeyError() {
        assertEquals(R.string.error_message_no_api_key, Runner.validateInput("gemini-2.5-flash", "", "Hello world"))
    }

    @Test
    fun blankApiKey_returnsApiKeyError() {
        assertEquals(R.string.error_message_no_api_key, Runner.validateInput("gemini-2.5-flash", "  ", "Hello world"))
    }

    @Test
    fun emptyPrompt_returnsPromptError() {
        assertEquals(R.string.error_message_no_prompt, Runner.validateInput("gemini-2.5-flash", "api-key-123", ""))
    }

    @Test
    fun blankPrompt_returnsPromptError() {
        assertEquals(R.string.error_message_no_prompt, Runner.validateInput("gemini-2.5-flash", "api-key-123", "   "))
    }

    @Test
    fun allEmpty_returnsModelErrorFirst() {
        assertEquals(R.string.error_message_no_model, Runner.validateInput("", "", ""))
    }

    @Test
    fun modelValid_apiKeyEmpty_promptEmpty_returnsApiKeyError() {
        assertEquals(R.string.error_message_no_api_key, Runner.validateInput("gemini-2.5-flash", "", ""))
    }
}

class RunnerMimeTypeTest {

    @Test
    fun jpgExtension_returnsJpeg() {
        assertEquals("image/jpeg", Runner.inferMimeType("/path/photo.jpg"))
    }

    @Test
    fun jpegExtension_returnsJpeg() {
        assertEquals("image/jpeg", Runner.inferMimeType("/path/photo.jpeg"))
    }

    @Test
    fun pngExtension_returnsPng() {
        assertEquals("image/png", Runner.inferMimeType("/path/photo.png"))
    }

    @Test
    fun webpExtension_returnsWebp() {
        assertEquals("image/webp", Runner.inferMimeType("/path/photo.webp"))
    }

    @Test
    fun gifExtension_returnsGif() {
        assertEquals("image/gif", Runner.inferMimeType("/path/photo.gif"))
    }

    @Test
    fun bmpExtension_returnsBmp() {
        assertEquals("image/bmp", Runner.inferMimeType("/path/photo.bmp"))
    }

    @Test
    fun unknownExtension_defaultsToJpeg() {
        assertEquals("image/jpeg", Runner.inferMimeType("/path/photo.tiff"))
    }

    @Test
    fun upperCaseExtension_isCaseInsensitive() {
        assertEquals("image/png", Runner.inferMimeType("/path/photo.PNG"))
    }
}

class RunnerBuildConfigTest {

    @Test
    fun allEmpty_returnsConfigWithDefaults() {
        val config = Runner.buildConfig("", "", "")
        assertNotNull(config)
    }

    @Test
    fun invalidTemperature_usesDefault() {
        val config = Runner.buildConfig("", "abc", "")
        assertNotNull(config)
    }

    @Test
    fun invalidMaxTokens_usesDefault() {
        val config = Runner.buildConfig("", "", "xyz")
        assertNotNull(config)
    }

    @Test
    fun systemInstructionOnly_returnsConfig() {
        val config = Runner.buildConfig("You are a translator", "", "")
        assertNotNull(config)
    }

    @Test
    fun temperatureOnly_returnsConfig() {
        val config = Runner.buildConfig("", "0.7", "")
        assertNotNull(config)
    }

    @Test
    fun maxTokensOnly_returnsConfig() {
        val config = Runner.buildConfig("", "", "1024")
        assertNotNull(config)
    }

    @Test
    fun allFieldsSet_returnsConfig() {
        val config = Runner.buildConfig("You are a translator", "0.5", "2048")
        assertNotNull(config)
    }

    @Test
    fun temperatureWithSpaces_parsed() {
        val config = Runner.buildConfig("", " 0.5 ", "")
        assertNotNull(config)
    }

    @Test
    fun maxTokensWithSpaces_parsed() {
        val config = Runner.buildConfig("", "", " 512 ")
        assertNotNull(config)
    }
}

class RunnerDefaultConstantsTest {

    @Test
    fun defaultTemperature_is1() {
        assertEquals(1.0f, Runner.DEFAULT_TEMPERATURE)
    }

    @Test
    fun defaultMaxOutputTokens_is8192() {
        assertEquals(2048, Runner.DEFAULT_MAX_OUTPUT_TOKENS)
    }
}
