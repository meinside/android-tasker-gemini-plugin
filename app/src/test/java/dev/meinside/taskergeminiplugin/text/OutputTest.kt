package dev.meinside.taskergeminiplugin.text

import org.junit.Assert.assertEquals
import org.junit.Test

class OutputTest {

    @Test
    fun defaultValue_isEmptyString() {
        val output = Output()
        assertEquals("", output.generated)
    }

    @Test
    fun constructorSetsValue() {
        val output = Output("Hello from Gemini")
        assertEquals("Hello from Gemini", output.generated)
    }

    @Test
    fun companionConstant() {
        assertEquals("gemini_text", Output.VAR_GENERATED_TEXT)
    }
}
