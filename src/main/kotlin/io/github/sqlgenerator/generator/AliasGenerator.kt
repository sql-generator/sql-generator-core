package io.github.sqlgenerator.generator

import kotlin.random.Random

class AliasGenerator {

    private val reservedWords =
        ReservedWords.values().map { reservedWords -> reservedWords.toString() }.toSet()
    private val charPool: List<Char> = ('a'..'z').toList()

    @JvmOverloads
    internal fun generateAlias(
        forbidden: Collection<String> = emptyList(),
        minLength: Int = 1,
        maxLength: Int = 16
    ): String {
        var alias: String?
        do {
            alias = (minLength..Random.nextInt(minLength, maxLength))
                .map { Random.nextInt(0, charPool.size) }
                .map(charPool::get)
                .joinToString("")
        } while (alias?.let {
                containsIgnoreCase(it, forbidden)
            } == true
            ||
            alias?.let {
                containsIgnoreCase(it, reservedWords)
            } == true)
        return alias ?: ""
    }

    private fun containsIgnoreCase(toCheck: String, against: Collection<String>): Boolean {
        return against.stream().anyMatch { a -> toCheck.equals(a, true) }
    }

}
