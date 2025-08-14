package dev.justincodinguk.devdeck.core.probe.background

import dev.justincodinguk.devdeck.core.deck_api.task.Process
import dev.justincodinguk.devdeck.core.deck_api.task.Task
import dev.justincodinguk.devdeck.core.deck_api.task.TaskHandler
import kotlinx.coroutines.flow.SharedFlow

interface BackgroundTaskManager {

    fun getEventStream() : SharedFlow<BackgroundTaskEvent>

    fun attachTaskHandler(taskHandler: TaskHandler)

    fun stopTask(taskId: Long, handlerId: Long)

    fun terminateHandler(taskHandlerId: Long)

}