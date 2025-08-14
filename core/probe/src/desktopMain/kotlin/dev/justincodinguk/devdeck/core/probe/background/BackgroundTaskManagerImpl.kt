package dev.justincodinguk.devdeck.core.probe.background

import dev.justincodinguk.devdeck.core.deck_api.task.TaskHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

internal class BackgroundTaskManagerImpl : BackgroundTaskManager {

    private val handlers = mutableMapOf<Long, TaskHandler>()
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val eventStream = MutableSharedFlow<BackgroundTaskEvent>()

    override fun getEventStream() = eventStream.asSharedFlow()

    override fun attachTaskHandler(taskHandler: TaskHandler) {
        handlers[taskHandler.id] = taskHandler
        taskHandler.setOnInvokeListener { process ->
            eventStream.emit(BackgroundTaskEvent.TaskStartedEvent(process))
            process.job.invokeOnCompletion {
                eventStream.tryEmit(BackgroundTaskEvent.TaskCompletedEvent(process))
            }
        }
    }


    override fun stopTask(taskId: Long, handlerId: Long) {
        val handler = handlers[handlerId]
        val process = handler?.processes?.find { it.id == taskId }
        process?.job?.let {
            it.cancel()
            coroutineScope.launch {
                eventStream.emit(BackgroundTaskEvent.TaskTerminatedEvent(process))
            }
        }
    }

    override fun terminateHandler(taskHandlerId: Long) {
        val handler = handlers[taskHandlerId]
        handler?.let {
            it.stopAllProcesses()
            it.processes.forEach { process ->
                coroutineScope.launch {
                    eventStream.emit(BackgroundTaskEvent.TaskTerminatedEvent(process))
                }
            }
        }
        handlers.remove(taskHandlerId)
    }
}