package dev.justincodinguk.devdeck.core.deck_api.task

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.util.Collections

/**
 * Manages the execution of tasks in a batch. All tasks are executed in parallel, unless inside a [StepTask] sequence
 *
 * @param batchSize The maximum number of tasks that can be executed in parallel
 */
class TaskHandler(
    val id: Long,
    private val batchSize: Int
) : CoroutineScope {

    override val coroutineContext = Dispatchers.IO

    /** The queue of tasks to be executed */
    private val taskQueue = mutableListOf<Task>()

    /** The list of running processes */
    private val runningProcesses = Collections.synchronizedList(mutableListOf<Process>())
    val processes = runningProcesses.toList()
    private var threadId: Long = 1

    private lateinit var onProcessInvoke: suspend (Process) -> Unit

    /**
     * Stops all running processes and clears the task queue.
     */
    fun stopAllProcesses() {
        coroutineContext.cancel()
        taskQueue.clear()
        runningProcesses.clear()
    }

    /**
     * Queues a task for execution. If there are less than [batchSize] running processes, the task is executed
     *
     * @param task The task to be executed
     */
    fun queueTask(task: Task) {
        if (runningProcesses.size < batchSize) {
            val job = launch { task.execute(); }
            val process = Process(threadId++, task, job)
            runningProcesses.add(process)
            launch { onProcessInvoke(process) }
            job.invokeOnCompletion {
                runningProcesses.remove(process)
                job.cancel()
                refreshProcessQueue()
            }
        } else taskQueue.add(task)
    }

    /**
     * Checks if the [TaskHandler] is or will be executing any tasks in the future.
     *
     * @return True if there are any running processes or tasks in the queue, false otherwise
     */

    fun isActive(): Boolean {
        return taskQueue.isNotEmpty() || runningProcesses.isNotEmpty()
    }

    fun setOnInvokeListener(listener: suspend (Process) -> Unit) {
        onProcessInvoke = listener
    }


    /**
     * Refreshes the task queue if there are no running processes. Called when a task completes.
     */
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