package dev.meinside.taskergeminiplugin.text

import org.junit.Assert.assertEquals
import org.junit.Test

class InputTest {

    @Test
    fun defaultValues_areEmptyStrings() {
        val input = Input()
        assertEquals("", input.model)
        assertEquals("", input.apiKey)
        assertEquals("", input.prompt)
        assertEquals("", input.imagePath)
        assertEquals("", input.systemInstruction)
        assertEquals("", input.temperature)
        assertEquals("", input.maxOutputTokens)
    }

    @Test
    fun constructorSetsValues() {
        val input = Input(
            model = "gemini-2.5-flash",
            apiKey = "key123",
            prompt = "Say hello",
            imagePath = "/sdcard/photo.jpg",
            systemInstruction = "You are a translator",
            temperature = "0.5",
            maxOutputTokens = "1024"
        )
        assertEquals("gemini-2.5-flash", input.model)
        assertEquals("key123", input.apiKey)
        assertEquals("Say hello", input.prompt)
        assertEquals("/sdcard/photo.jpg", input.imagePath)
        assertEquals("You are a translator", input.systemInstruction)
        assertEquals("0.5", input.temperature)
        assertEquals("1024", input.maxOutputTokens)
    }

    @Test
    fun companionConstants() {
        assertEquals("gemini_model", Input.VAR_MODEL)
        assertEquals("gemini_api_key", Input.VAR_API_KEY)
        assertEquals("gemini_prompt", Input.VAR_PROMPT)
        assertEquals("gemini_image_path", Input.VAR_IMAGE_PATH)
        assertEquals("gemini_system_instruction", Input.VAR_SYSTEM_INSTRUCTION)
        assertEquals("gemini_temperature", Input.VAR_TEMPERATURE)
        assertEquals("gemini_max_output_tokens", Input.VAR_MAX_OUTPUT_TOKENS)
    }
}
