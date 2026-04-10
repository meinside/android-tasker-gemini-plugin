package dev.meinside.taskergeminiplugin.text

import android.os.Bundle
import android.view.LayoutInflater
import com.joaomgcd.taskerpluginlibrary.config.TaskerPluginConfig
import com.joaomgcd.taskerpluginlibrary.input.TaskerInput
import com.joaomgcd.taskerpluginsample.tasker.ActivityConfigTasker
import dev.meinside.taskergeminiplugin.databinding.ActivityConfigBinding

class ActivityConfig : ActivityConfigTasker<Input, Output, Runner, ActionHelper, ActivityConfigBinding>() {
    override fun getNewHelper(config: TaskerPluginConfig<Input>) = ActionHelper(this)
    override fun inflateBinding(layoutInflater: LayoutInflater): ActivityConfigBinding = ActivityConfigBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = inflateBinding(layoutInflater)
        if (!isConfigurable) {
            taskerHelper.finishForTasker()
            return
        }
        binding?.root?.let { setContentView(it) }
        taskerHelper.onCreate()
    }

    override val inputForTasker: TaskerInput<Input>
        get() = TaskerInput(Input(
            binding?.editTextModel?.text?.toString() ?: "",
            binding?.editTextApiKey?.text?.toString() ?: "",
            binding?.editTextPrompt?.text?.toString() ?: "",
            binding?.editTextImagePath?.text?.toString() ?: "",
            binding?.editTextSystemInstruction?.text?.toString() ?: "",
            binding?.editTextTemperature?.text?.toString() ?: "",
            binding?.editTextMaxOutputTokens?.text?.toString() ?: ""))

    override fun assignFromInput(input: TaskerInput<Input>): Unit = input.regular.run {
        binding?.editTextModel?.setText(model)
        binding?.editTextApiKey?.setText(apiKey)
        binding?.editTextPrompt?.setText(prompt)
        binding?.editTextImagePath?.setText(imagePath)
        binding?.editTextSystemInstruction?.setText(systemInstruction)
        binding?.editTextTemperature?.setText(temperature)
        binding?.editTextMaxOutputTokens?.setText(maxOutputTokens)
    }
}
