package dev.meinside.taskergeminiplugin.text

import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.joaomgcd.taskerpluginlibrary.config.TaskerPluginConfig
import com.joaomgcd.taskerpluginlibrary.input.TaskerInput
import com.joaomgcd.taskerpluginsample.tasker.ActivityConfigTasker
import dev.meinside.taskergeminiplugin.databinding.ActivityConfigBinding

class ActivityConfig : ActivityConfigTasker<Input, Output, Runner, ActionHelper, ActivityConfigBinding>() {
    override fun getNewHelper(config: TaskerPluginConfig<Input>) = ActionHelper(this)
    override fun inflateBinding(layoutInflater: LayoutInflater): ActivityConfigBinding = ActivityConfigBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding?.let { b ->
            // targetSdk 35+ draws edge-to-edge: keep the fields and the buttons
            // clear of the status bar, the navigation bar and the keyboard.
            ViewCompat.setOnApplyWindowInsetsListener(b.root) { view, insets ->
                val bars = insets.getInsets(
                    WindowInsetsCompat.Type.systemBars()
                            or WindowInsetsCompat.Type.displayCutout()
                            or WindowInsetsCompat.Type.ime()
                )
                view.updatePadding(left = bars.left, top = bars.top, right = bars.right, bottom = bars.bottom)
                WindowInsetsCompat.CONSUMED
            }

            b.buttonSave.setOnClickListener { saveAndFinish() }
            b.buttonCancel.setOnClickListener { cancelAndFinish() }
        }
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
