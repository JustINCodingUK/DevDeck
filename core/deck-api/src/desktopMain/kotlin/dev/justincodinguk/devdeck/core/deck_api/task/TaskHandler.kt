package dev.justincodinguk.devdeck.core.deck_api.task

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.util.Collections

class TaskHandler(private val batchSize: Int) : CoroutineScope {

    override val coroutineContext = Dispatchers.IO
    private val taskQueue = mutableListOf<Task>()
    private val runningProcesses = Collections.synchronizedList(mutableListOf<Process>())
    private var threadId = 1

    fun stopAllProcesses() {
        coroutineContext.cancel()
        taskQueue.clear()
        runningProcesses.clear()
    }

    fun queueTask(task: Task) {
        if (runningProcesses.size < batchSize) {
            val job = launch { task.execute(); }
            val process = Process(threadId++, task, job)
            runningProcesses.add(process)
            job.invokeOnCompletion {
                runningProcesses.remove(process)
                job.cancel()
                refreshProcessQueue()
            }
        } else taskQueue.add(task)
    }

    fun isActive(): Boolean {
        return taskQueue.isNotEmpty() || runningProcesses.isNotEmpty()
    }

    private fun refreshProcessQueue() {
        var referenceTaskRunning = false
        runningProcesses.forEach {
            if (it.task is ReferenceTask) referenceTaskRunning = true
        }
        if (!referenceTaskRunning) {
            val task = taskQueue.firstOrNull()
            if (task != null) {
                queueTask(task)
                taskQueue.remove(task)
            }
        }
    }
}