package io.github.sqlgenerator

import io.github.sqlgenerator.compiler.Compiler
import java.lang.StringBuilder

class Column @JvmOverloads constructor(val table: Table, val field: String, var alias: String? = null) : Selection {

    override fun collectSelectables(): Collection<Selectable> {
        return setOf(table)
    }

    override fun compile(compiler: Compiler): String {
        val builder = StringBuilder()
        builder.append(table.getOrCreateAlias(compiler))
            .append('.')
            .append(field)
        alias?.let {
            builder.append(' ')
                .append("AS")
                .append(' ')
                .append(alias)
        }
        return builder.toString()
    }

}
