package dev.meinside.taskergeminiplugin.text

import com.joaomgcd.taskerpluginlibrary.input.TaskerInputField
import com.joaomgcd.taskerpluginlibrary.input.TaskerInputRoot

@TaskerInputRoot
class Input @JvmOverloads constructor(
    @field:TaskerInputField(VAR_API_KEY, labelResIdName = VAR_API_KEY) var apiKey: String = "",
    @field:TaskerInputField(VAR_PROMPT, labelResIdName = VAR_PROMPT) var prompt: String = "",
){
    companion object {
        const val VAR_API_KEY = "gemini_api_key"
        const val VAR_PROMPT = "gemini_prompt"
    }
}