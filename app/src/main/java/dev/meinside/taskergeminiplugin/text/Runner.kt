package dev.meinside.taskergeminiplugin.text

import android.content.Context
import com.google.genai.Client
import com.google.genai.types.Content
import com.google.genai.types.GenerateContentConfig
import com.google.genai.types.GenerateContentResponse
import com.google.genai.types.Part
import com.joaomgcd.taskerpluginlibrary.action.TaskerPluginRunnerAction
import com.joaomgcd.taskerpluginlibrary.input.TaskerInput
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResult
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResultSucess
import dev.meinside.taskergeminiplugin.R

class Runner : TaskerPluginRunnerAction<Input, Output>() {
    override fun run(context: Context, input: TaskerInput<Input>): TaskerPluginResult<Output> {
        var generated: String? = null

        val validationError = validateInput(input.regular.model, input.regular.apiKey, input.regular.prompt)
        if (validationError != null) {
            generated = context.getString(validationError)
        } else {
            try {
                val client = Client.builder().apiKey(input.regular.apiKey).build()
                val imagePath = input.regular.imagePath.trim()
                val config = buildConfig(
                    input.regular.systemInstruction,
                    input.regular.temperature,
                    input.regular.maxOutputTokens
                )

                val res: GenerateContentResponse = if (imagePath.isEmpty()) {
                    client.models.generateContent(input.regular.model, input.regular.prompt, config)
                } else {
                    val image = when (val read = ImageAccess.read(context, imagePath)) {
                        is ImageAccess.Read.Failed -> {
                            generated = context.getString(read.messageRes).format(imagePath)
                            return TaskerPluginResultSucess(Output(generated))
                        }
                        is ImageAccess.Read.Image -> read
                    }
                    val content = Content.fromParts(
                        Part.fromText(input.regular.prompt),
                        Part.fromBytes(image.bytes, image.mimeType)
                    )
                    client.models.generateContent(input.regular.model, content, config)
                }

                res.promptFeedback().orElse(null)?.blockReason()?.orElse(null)?.let {
                    generated = context.getString(R.string.error_message_blocked).format(it.toString())
                }

                if (generated.isNullOrBlank()) {
                    generated = res.text()
                }
            } catch (e: Exception) {
                generated = context.getString(R.string.error_message_unknown) + ": " + e.message
            }
        }

        generated = generated ?: context.getString(R.string.error_message_unknown)

        return TaskerPluginResultSucess(Output(generated))
    }

    companion object {
        const val DEFAULT_TEMPERATURE = 1.0f
        const val DEFAULT_MAX_OUTPUT_TOKENS = 2048

        fun validateInput(model: String, apiKey: String, prompt: String): Int? {
            return when {
                model.trim().isEmpty() -> R.string.error_message_no_model
                apiKey.trim().isEmpty() -> R.string.error_message_no_api_key
                prompt.trim().isEmpty() -> R.string.error_message_no_prompt
                else -> null
            }
        }

        fun inferMimeType(path: String): String {
            return when {
                path.endsWith(".png", ignoreCase = true) -> "image/png"
                path.endsWith(".webp", ignoreCase = true) -> "image/webp"
                path.endsWith(".gif", ignoreCase = true) -> "image/gif"
                path.endsWith(".bmp", ignoreCase = true) -> "image/bmp"
                else -> "image/jpeg"
            }
        }

        fun buildConfig(
            systemInstruction: String,
            temperature: String,
            maxOutputTokens: String
        ): GenerateContentConfig {
            val builder = GenerateContentConfig.builder()

            if (systemInstruction.trim().isNotEmpty()) {
                builder.systemInstruction(Content.fromParts(Part.fromText(systemInstruction.trim())))
            }
            builder.temperature(temperature.trim().toFloatOrNull() ?: DEFAULT_TEMPERATURE)
            builder.maxOutputTokens(maxOutputTokens.trim().toIntOrNull() ?: DEFAULT_MAX_OUTPUT_TOKENS)

            return builder.build()
        }
    }
}
