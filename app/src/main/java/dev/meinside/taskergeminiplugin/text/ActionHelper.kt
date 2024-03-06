package dev.meinside.taskergeminiplugin.text

import com.joaomgcd.taskerpluginlibrary.config.TaskerPluginConfig
import com.joaomgcd.taskerpluginlibrary.config.TaskerPluginConfigHelper
import com.joaomgcd.taskerpluginlibrary.input.TaskerInput
import com.joaomgcd.taskerpluginlibrary.output.TaskerOutputsForConfig

class ActionHelper(config: TaskerPluginConfig<Input>) : TaskerPluginConfigHelper<Input, Output, Runner>(config) {
    override val runnerClass = Runner::class.java
    override val inputClass = Input::class.java
    override val outputClass = Output::class.java

    override fun addOutputs(input: TaskerInput<Input>, output: TaskerOutputsForConfig) {
        super.addOutputs(input, output)
    }
}