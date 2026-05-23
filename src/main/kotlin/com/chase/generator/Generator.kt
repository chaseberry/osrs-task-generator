package com.chase.generator

import com.chase.generator.parameters.GeneratorParameters
import com.chase.providers.ItemProvider
import com.chase.providers.ItemSourceProvider
import com.chase.providers.TaskProvider

class Generator(
    val parameters: GeneratorParameters,
    val itemProvider: ItemProvider,
    val itemSourceProvider: ItemSourceProvider,
    val taskProvider: TaskProvider,
) {
}