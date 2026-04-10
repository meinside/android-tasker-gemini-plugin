package dev.meinside.taskergeminiplugin.text

import com.joaomgcd.taskerpluginlibrary.input.TaskerInputField
import com.joaomgcd.taskerpluginlibrary.input.TaskerInputRoot

@TaskerInputRoot
class Input @JvmOverloads constructor(
    @field:TaskerInputField(VAR_MODEL, labelResIdName = VAR_MODEL) var model: String = "",
    @field:TaskerInputField(VAR_API_KEY, labelResIdName = VAR_API_KEY) var apiKey: String = "",
    @field:TaskerInputField(VAR_PROMPT, labelResIdName = VAR_PROMPT) var prompt: String = "",
    @field:TaskerInputField(VAR_IMAGE_PATH, labelResIdName = VAR_IMAGE_PATH) var imagePath: String = "",
    @field:TaskerInputField(VAR_SYSTEM_INSTRUCTION, labelResIdName = VAR_SYSTEM_INSTRUCTION) var systemInstruction: String = "",
    @field:TaskerInputField(VAR_TEMPERATURE, labelResIdName = VAR_TEMPERATURE) var temperature: String = "",
    @field:TaskerInputField(VAR_MAX_OUTPUT_TOKENS, labelResIdName = VAR_MAX_OUTPUT_TOKENS) var maxOutputTokens: String = "",
){
    companion object {
        const val VAR_MODEL = "gemini_model"
        const val VAR_API_KEY = "gemini_api_key"
        const val VAR_PROMPT = "gemini_prompt"
        const val VAR_IMAGE_PATH = "gemini_image_path"
        const val VAR_SYSTEM_INSTRUCTION = "gemini_system_instruction"
        const val VAR_TEMPERATURE = "gemini_temperature"
        const val VAR_MAX_OUTPUT_TOKENS = "gemini_max_output_tokens"
    }
}
