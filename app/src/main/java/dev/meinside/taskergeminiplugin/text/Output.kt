package dev.meinside.taskergeminiplugin.text

import com.joaomgcd.taskerpluginlibrary.output.TaskerOutputObject
import com.joaomgcd.taskerpluginlibrary.output.TaskerOutputVariable

@TaskerOutputObject
class Output (
    @get:TaskerOutputVariable(VAR_GENERATED_TEXT, labelResIdName = VAR_GENERATED_TEXT) var generated: String = ""
){
    companion object {
        const val VAR_GENERATED_TEXT = "gemini_text"
    }
}