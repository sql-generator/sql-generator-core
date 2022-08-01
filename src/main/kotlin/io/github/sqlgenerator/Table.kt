package io.github.sqlgenerator

import io.github.sqlgenerator.compiler.Compiler

class Table @JvmOverloads constructor(val name: String, private var alias: String? = null) : Selectable {
    private var columns = mutableSetOf<Column>()

    fun createColumn(field: String): Column {
        val column = Column(this, field)
        columns.add(column)
        return column
    }

    fun createColumn(field: String, alias: String): Column {
        val column = Column(this, field, alias)
        columns.add(column)
        return column
    }

    internal fun getOrCreateAlias(compiler: Compiler): String {
        if (alias == null) {
            alias = compiler.aliasGenerator.generateAlias(compiler.usedSelectableAlias)
            compiler.usedSelectableAlias.add(alias!!)
        }
        return alias!!
    }

    override fun compile(compiler: Compiler): String {
        return "$name ${getOrCreateAlias(compiler)}"
    }
}
