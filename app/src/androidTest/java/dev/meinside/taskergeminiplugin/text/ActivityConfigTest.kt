package dev.meinside.taskergeminiplugin.text

import android.app.Activity
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBackUnconditionally
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.joaomgcd.taskerpluginlibrary.TaskerPluginConstants
import dev.meinside.taskergeminiplugin.R
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Leaving the configuration screen must hand the values back to Tasker.
 *
 * [back_returnsConfigurationToTasker] reproduces the reported "changes are lost on back"
 * bug only on Android 16+ (API 36+): with targetSdk 36+ the old onKeyDown()-based back
 * handling was never called there, and the activity finished with RESULT_CANCELED.
 */
@RunWith(AndroidJUnit4::class)
class ActivityConfigTest {

    private fun launch(): ActivityScenario<ActivityConfig> = ActivityScenario.launchActivityForResult(
        Intent(ApplicationProvider.getApplicationContext(), ActivityConfig::class.java)
    )

    private fun fillIn() {
        onView(withId(R.id.editTextModel)).perform(replaceText("gemini-2.5-flash"))
        onView(withId(R.id.editTextApiKey)).perform(replaceText("test-api-key"))
        onView(withId(R.id.editTextPrompt)).perform(replaceText("Say hello"))
        closeSoftKeyboard()
    }

    private fun assertSaved(scenario: ActivityScenario<ActivityConfig>) {
        val result = scenario.result
        assertEquals(Activity.RESULT_OK, result.resultCode)
        val bundle = result.resultData.getBundleExtra(TaskerPluginConstants.EXTRA_BUNDLE)
        assertNotNull(bundle)
        assertEquals("gemini-2.5-flash", bundle!!.getString(Input.VAR_MODEL))
        assertEquals("test-api-key", bundle.getString(Input.VAR_API_KEY))
        assertEquals("Say hello", bundle.getString(Input.VAR_PROMPT))
    }

    @Test
    fun back_returnsConfigurationToTasker() {
        val scenario = launch()
        fillIn()
        pressBackUnconditionally()
        assertSaved(scenario)
    }

    @Test
    fun saveButton_returnsConfigurationToTasker() {
        val scenario = launch()
        fillIn()
        onView(withId(R.id.buttonSave)).perform(click())
        assertSaved(scenario)
    }

    @Test
    fun cancelButton_returnsNothing() {
        val scenario = launch()
        fillIn()
        onView(withId(R.id.buttonCancel)).perform(click())
        assertEquals(Activity.RESULT_CANCELED, scenario.result.resultCode)
    }
}
