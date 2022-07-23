package io.github.sqlgenerator

import io.github.sqlgenerator.compiler.Compiler;

interface Selectable {
    fun compile(compiler: Compiler): String
}
