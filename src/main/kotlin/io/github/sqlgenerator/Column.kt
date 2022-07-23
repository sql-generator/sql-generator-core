package io.github.sqlgenerator

class Column(table: Table, field: String) {
    private val table: Table = table
        get() = field
    private val field: String = field
        get() = field
    private var alias: String? = null
        get() = field

    constructor(table: Table, field: String, alias: String) : this(table, field) {
        this.alias = alias
    }
}
