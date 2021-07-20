package top.bettercode.generator.puml

import top.bettercode.generator.GeneratorExtension
import top.bettercode.generator.ddl.MysqlToDDL
import top.bettercode.generator.ddl.OracleToDDL
import org.junit.jupiter.api.Test
import java.io.File

/**
 * @author Peter Wu
 * @since 0.0.45
 */
class PumlConverterTest {

    @Test
    fun convert() {
        val tables = PumlConverter.toTables(File("build/gen/puml/db.puml"))
        println(tables)
    }

    @Test
    fun compile() {
        PumlConverter.compile(GeneratorExtension(), File(PumlConverterTest::class.java.getResource("/oracle.puml").file), File("build/gen/puml/database.puml"))
    }

    @Test
    fun toMysql() {
        PumlConverter.toMysql(GeneratorExtension(), File(PumlConverterTest::class.java.getResource("/oracle.puml").file), File("build/gen/puml/database.puml"))
    }

    @Test
    fun toOracle() {
        PumlConverter.toOracle(GeneratorExtension(), File(PumlConverterTest::class.java.getResource("/mysql.puml").file), File("build/gen/puml/database.puml"))
    }

    @Test
    fun toOracleDLL() {
        OracleToDDL.toDDL(PumlConverter.toTables(File(PumlConverterTest::class.java.getResource("/oracle.puml").file)), File("build/gen/puml/oracle.sql"))
    }

    @Test
    fun toMySqlDLL() {
        MysqlToDDL.toDDL(PumlConverter.toTables(File(PumlConverterTest::class.java.getResource("/mysql.puml").file)), File("build/gen/puml/mysql.sql"))
    }

    @Test
    fun toOracleUpdate() {
        File("build/gen/puml/oracleUpdate.sql").printWriter().use {
            OracleToDDL.toDDLUpdate(PumlConverter.toTables(File(PumlConverterTest::class.java.getResource("/oracle.puml").file)), PumlConverter.toTables(File(PumlConverterTest::class.java.getResource("/newOracle.puml").file)), it)
        }
    }

    @Test
    fun toMysqlUpdate() {
        File("build/gen/puml/mysqlUpdate.sql").printWriter().use {
            MysqlToDDL.toDDLUpdate(PumlConverter.toTables(File(PumlConverterTest::class.java.getResource("/mysql.puml").file)), PumlConverter.toTables(File(PumlConverterTest::class.java.getResource("/newMysql.puml").file)), it)
        }
    }
}