package dev.meinside.taskergeminiplugin.text

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.core.widget.doAfterTextChanged
import com.joaomgcd.taskerpluginlibrary.config.TaskerPluginConfig
import com.joaomgcd.taskerpluginlibrary.input.TaskerInput
import com.joaomgcd.taskerpluginsample.tasker.ActivityConfigTasker
import dev.meinside.taskergeminiplugin.R
import dev.meinside.taskergeminiplugin.databinding.ActivityConfigBinding

class ActivityConfig : ActivityConfigTasker<Input, Output, Runner, ActionHelper, ActivityConfigBinding>() {
    override fun getNewHelper(config: TaskerPluginConfig<Input>) = ActionHelper(this)
    override fun inflateBinding(layoutInflater: LayoutInflater): ActivityConfigBinding = ActivityConfigBinding.inflate(layoutInflater)

    private val requestImageAccess = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        updateImageAccess()
        // denied for good: the system no longer shows the dialog, only the app settings can grant it
        val permission = ImageAccess.permissionsFor(Build.VERSION.SDK_INT).first()
        if (!ImageAccess.hasFullAccess(this) && !ImageAccess.hasLimitedAccess(this) &&
            !shouldShowRequestPermissionRationale(permission)
        ) {
            startActivity(
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", packageName, null))
            )
        }
    }

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

            b.editTextImagePath.doAfterTextChanged { updateImageAccess() }
            b.buttonImageAccess.setOnClickListener {
                requestImageAccess.launch(ImageAccess.permissionsFor(Build.VERSION.SDK_INT).toTypedArray())
            }
        }
    }

    override fun onResume() {
        super.onResume()

        // the permission may have changed in the app settings meanwhile
        updateImageAccess()
    }

    /**
     * Offers to grant image access while an image path is set, as the runner cannot ask for it.
     */
    private fun updateImageAccess() {
        val b = binding ?: return
        val needed = ImageAccess.mayNeedPermission(b.editTextImagePath.text?.toString() ?: "") &&
                !ImageAccess.hasFullAccess(this)
        b.layoutImageAccess.visibility = if (needed) View.VISIBLE else View.GONE
        b.textImageAccess.setText(
            if (ImageAccess.hasLimitedAccess(this)) R.string.image_access_limited else R.string.image_access_none
        )
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
