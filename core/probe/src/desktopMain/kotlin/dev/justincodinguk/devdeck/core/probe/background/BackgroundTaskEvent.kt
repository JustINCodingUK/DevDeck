package dev.justincodinguk.devdeck.core.probe.background

import dev.justincodinguk.devdeck.core.deck_api.task.Process

sealed interface BackgroundTaskEvent {
    class TaskStartedEvent(val process: Process) : BackgroundTaskEvent
    class TaskCompletedEvent(val process: Process) : BackgroundTaskEvent
    class TaskTerminatedEvent(val process: Process) : BackgroundTaskEvent
}