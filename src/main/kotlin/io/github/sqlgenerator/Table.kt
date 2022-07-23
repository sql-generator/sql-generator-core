package io.github.sqlgenerator

import io.github.sqlgenerator.compiler.Compiler

class Table(name: String) : Selectable {
    private val name = name
        get() = field
    private var alias: String? = null
        get() = field
    private var columns = mutableSetOf<Column>()

    constructor(name: String, alias: String) : this(name) {
        this.alias = alias;
    }

    public fun createColumn(field: String): Column {
        val column = Column(this, field)
        columns.add(column)
        return column
    }

    public fun createColumn(field: String, alias: String): Column {
        val column = Column(this, field, alias)
        columns.add(column)
        return column
    }

    override fun compile(compiler: Compiler): String {
        TODO("Not yet implemented")
    }
}
