package top.bettercode.generator.database

import top.bettercode.generator.DataType
import top.bettercode.generator.GeneratorExtension
import top.bettercode.generator.dsl.Generators
import top.bettercode.generator.puml.PumlConverterTest
import org.junit.jupiter.api.Test
import java.io.File

/**
 *
 * @author Peter Wu
 */
class PumlGeneratorsTest {
    private val extension = GeneratorExtension(
        basePath = File("build"),
        dir = "gen/java",
        packageName = "com.bettercode.test",
        replaceAll = true,
        tablePrefixes = arrayOf("OAUTH_"),
        dataType = DataType.PUML,
        tableNames = arrayOf("OAUTH_CLIENT_DETAILS", "OAUTH_CLIENT_TOKEN")
    )

    @Test
    fun gen() {
        extension.generators = arrayOf(
        )
        extension.pumlSrc = PumlConverterTest::class.java.getResource("/database.puml").file
        Generators.call(extension)
    }
}