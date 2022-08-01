package io.github.sqlgenerator

import io.github.sqlgenerator.compiler.Compiler

interface Selection {
    fun collectSelectables(): Collection<Selectable>
    fun compile(compiler: Compiler): String
}
