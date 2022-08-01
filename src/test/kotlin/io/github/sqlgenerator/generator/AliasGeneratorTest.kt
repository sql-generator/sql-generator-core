package io.github.sqlgenerator.generator

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class AliasGeneratorTest {

    private val objectUnderTest = AliasGenerator()

    @Test
    fun shouldJustContainAlphabeticalSigns() {
        val alias = objectUnderTest.generateAlias(emptyList())
        assertThat(alias).containsPattern("[a-z]")
    }

    @Test
    fun shouldGenerateCorrectLengthByDefault() {
        val alias = objectUnderTest.generateAlias(emptyList())
        assertThat(alias.length).isBetween(1, 16)
    }

    @Test
    fun shouldRespectDenyList() {
        val forbidden = ('b'..'z').toList().map { it.toString() }.toList()
        val alias = objectUnderTest.generateAlias(forbidden, 1, 2)
        assertThat(alias).isEqualTo("a")
    }

}
