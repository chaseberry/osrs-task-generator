package com.chase.generator

import com.chase.generator.parameters.GeneratorParameters

class GenerationException(
    val parameters: GeneratorParameters,
    message: String
) : RuntimeException(message) {
}