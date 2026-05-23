package com.chase.providers.sources

import com.chase.models.tasks.Task
import com.chase.providers.TaskProvider

class InMemoryCustomTaskProvider(
    items: List<Task>,
) : TaskProvider, BaseInMemoryProvider<Task>(items.toMutableList()) {
}