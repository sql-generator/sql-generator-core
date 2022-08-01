package io.github.sqlgenerator.compiler

import io.github.sqlgenerator.generator.AliasGenerator

interface Compiler {

    val aliasGenerator: AliasGenerator
        get() = AliasGenerator()

    val usedSelectableAlias: MutableCollection<String>
        get() = mutableListOf()

}
