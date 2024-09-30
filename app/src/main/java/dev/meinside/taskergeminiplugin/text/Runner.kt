package dev.meinside.taskergeminiplugin.text

import android.content.Context
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.asTextOrNull
import com.joaomgcd.taskerpluginlibrary.action.TaskerPluginRunnerAction
import com.joaomgcd.taskerpluginlibrary.input.TaskerInput
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResult
import com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginResultSucess
import dev.meinside.taskergeminiplugin.R
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope

class Runner : TaskerPluginRunnerAction<Input, Output>() {
    override fun run(context: Context, input: TaskerInput<Input>): TaskerPluginResult<Output> {
        var generated: String? = null

        if (input.regular.model.trim() == "") {
            generated = context.getString(R.string.error_message_no_model)
        } else if (input.regular.apiKey.trim() == "") {
            generated = context.getString(R.string.error_message_no_api_key)
        } else if (input.regular.prompt.trim() == "") {
            generated = context.getString(R.string.error_message_no_prompt)
        } else {
            runBlocking {
                supervisorScope {
                    val model = GenerativeModel(modelName = input.regular.model, apiKey = input.regular.apiKey)
                    val res = model.generateContent(input.regular.prompt)

                    res.promptFeedback?.blockReason?.let {
                        generated = context.getString(R.string.error_message_blocked).format(it.name)
                    }

                    if (res.candidates.isNotEmpty() &&
                        res.candidates.first().content.parts.isNotEmpty() &&
                        generated.isNullOrBlank()
                    ) {
                        generated = res.candidates.first().content.parts.first().asTextOrNull()
                    }
                }
            }
        }

        generated = generated ?: context.getString(R.string.error_message_unknown)

        return TaskerPluginResultSucess(Output(generated!!))
    }
}