package io.github.sqlgenerator.query

import io.github.sqlgenerator.Selectable
import io.github.sqlgenerator.Selection
import io.github.sqlgenerator.compiler.Compiler
import io.github.sqlgenerator.compiler.StringCompiler

class SelectQuery @JvmOverloads constructor(private val distinct: Boolean = false) {

    private val selection: MutableList<Selection> = mutableListOf()
    private var from: Selectable? = null

    fun addSelection(vararg select: Selection) {
        selection.addAll(select)
    }

    fun addFrom(selectable: Selectable) {
        from = selectable
    }

    private fun compile(compiler: Compiler): String {
        return listOf(
            generateSelectLine(),
            generateSelectionLine(compiler),
            generateFromLine(compiler),
        ).joinToString(separator = System.lineSeparator())
    }

    override fun toString(): String {
        return compile(StringCompiler())
    }

    private fun generateSelectLine(): String {
        return if (distinct) {
            "SELECT DISTINCT"
        } else {
            "SELECT"
        }
    }

    private fun generateSelectionLine(compiler: Compiler): String {
        return selection.joinToString(separator = "," + System.lineSeparator()) { selection ->
            "\t" + selection.compile(compiler)
        }
    }

    private fun generateFromLine(compiler: Compiler): String {
        return if (from != null) {
            "FROM ${from?.compile(compiler)}"
        } else {
            val fromBySelection = selection.flatMap { selection -> selection.collectSelectables() }.toSet()
            if (fromBySelection.size == 1) {
                fromBySelection.joinToString(separator = "") { selectable ->
                    "FROM ${selectable.compile(compiler)}"
                }
            } else {
                throw IllegalArgumentException("one source is needed")
            }
        }
    }

}
