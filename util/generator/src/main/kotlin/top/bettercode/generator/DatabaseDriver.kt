package top.bettercode.generator

import java.util.*

/**
 * Enumeration of common database drivers.
 */
enum class DatabaseDriver(private val productName: String?,
                          /**
                           * Return the driver class name.
                           * @return the class name or `null`
                           */
                          val driverClassName: String?,
                          /**
                           * Return the XA driver source class name.
                           * @return the class name or `null`
                           */
                          val xaDataSourceClassName: String? = null,
                          /**
                           * Return the validation query.
                           * @return the validation query or `null`
                           */
                          val validationQuery: String? = null) {

    /**
     * Unknown type.
     */
    UNKNOWN(null, null),

    /**
     * Apache Derby.
     */
    DERBY("Apache Derby", "org.apache.derby.jdbc.EmbeddedDriver",
            "org.apache.derby.jdbc.EmbeddedXADataSource",
            "SELECT 1 FROM SYSIBM.SYSDUMMY1"),

    /**
     * H2.
     */
    H2("H2", "org.h2.Driver", "org.h2.jdbcx.JdbcDataSource", "SELECT 1"),

    /**
     * HyperSQL DataBase.
     */
    HSQLDB("HSQL Database Engine", "org.hsqldb.jdbc.JDBCDriver",
            "org.hsqldb.jdbc.pool.JDBCXADataSource",
            "SELECT COUNT(*) FROM INFORMATION_SCHEMA.SYSTEM_USERS"),

    /**
     * SQL Lite.
     */
    SQLITE("SQLite", "org.sqlite.JDBC"),

    /**
     * MySQL.
     */
    MYSQL("MySQL", "com.mysql.cj.jdbc.Driver", "com.mysql.cj.jdbc.MysqlXADataSource",
            "/* ping */ SELECT 1"),

    /**
     * Maria DB.
     */
    MARIADB("MySQL", "org.mariadb.jdbc.Driver", "org.mariadb.jdbc.MariaDbDataSource",
            "SELECT 1") {

        override val id: String
            get() = "mysql"
    },

    /**
     * Google App Engine.
     */
    GAE(null, "com.google.appengine.api.rdbms.AppEngineDriver"),

    /**
     * Oracle.
     */
    ORACLE("Oracle", "oracle.jdbc.OracleDriver",
            "oracle.jdbc.xa.client.OracleXADataSource", "SELECT 'Hello' from DUAL"),

    /**
     * Postgres.
     */
    POSTGRESQL("PostgreSQL", "org.postgresql.Driver", "org.postgresql.xa.PGXADataSource",
            "SELECT 1"),

    /**
     * HANA - SAP HANA Database - HDB.
     * @since 2.1.0
     */
    HANA("HDB", "com.sap.db.jdbc.Driver", "com.sap.db.jdbcext.XADataSourceSAP",
            "SELECT 1 FROM SYS.DUMMY") {
        override val urlPrefixes: Collection<String>
            get() = setOf("sap")
    },

    /**
     * jTDS. As it can be used for several databases, there isn't a single product name we
     * could rely on.
     */
    JTDS(null, "net.sourceforge.jtds.jdbc.Driver"),

    /**
     * SQL Server.
     */
    SQLSERVER("Microsoft SQL Server", "com.microsoft.sqlserver.jdbc.SQLServerDriver",
            "com.microsoft.sqlserver.jdbc.SQLServerXADataSource", "SELECT 1") {

        override fun matchProductName(productName: String): Boolean {
            return super.matchProductName(productName) || "SQL SERVER".equals(productName, ignoreCase = true)

        }

    },

    /**
     * Firebird.
     */
    FIREBIRD("Firebird", "org.firebirdsql.jdbc.FBDriver",
            "org.firebirdsql.ds.FBXADataSource", "SELECT 1 FROM RDB\$DATABASE") {

        override val urlPrefixes: Collection<String>
            get() = setOf("firebirdsql")

        override fun matchProductName(productName: String): Boolean {
            return super.matchProductName(productName) || productName.toLowerCase(Locale.ENGLISH).startsWith("firebird")
        }
    },

    /**
     * DB2 Server.
     */
    DB2("DB2", "com.ibm.db2.jcc.DB2Driver", "com.ibm.db2.jcc.DB2XADataSource",
            "SELECT 1 FROM SYSIBM.SYSDUMMY1") {

        override fun matchProductName(productName: String): Boolean {
            return super.matchProductName(productName) || productName.toLowerCase(Locale.ENGLISH).startsWith("db2/")
        }
    },

    /**
     * DB2 AS400 Server.
     */
    DB2_AS400("DB2 UDB for AS/400", "com.ibm.as400.access.AS400JDBCDriver",
            "com.ibm.as400.access.AS400JDBCXADataSource",
            "SELECT 1 FROM SYSIBM.SYSDUMMY1") {

        override val id: String
            get() = "db2"

        override val urlPrefixes: Collection<String>
            get() = setOf("as400")

        override fun matchProductName(productName: String): Boolean {
            return super.matchProductName(productName) || productName.toLowerCase(Locale.ENGLISH).contains("as/400")
        }
    },

    /**
     * Teradata.
     */
    TERADATA("Teradata", "com.teradata.jdbc.TeraDriver"),

    /**
     * Informix.
     */
    INFORMIX("Informix Dynamic Server", "com.informix.jdbc.IfxDriver", null,
            "select count(*) from systables") {

        override val urlPrefixes: Collection<String>
            get() = listOf("informix-sqli", "informix-direct")

    };

    /**
     * Return the identifier of this driver.
     * @return the identifier
     */
    open val id: String
        get() = name.toLowerCase(Locale.ENGLISH)

    protected open val urlPrefixes: Collection<String>
        get() = setOf(this.name.toLowerCase(Locale.ENGLISH))

    protected open fun matchProductName(productName: String): Boolean {
        return this.productName != null && this.productName.equals(productName, ignoreCase = true)
    }

    companion object {

        /**
         * Find a [DatabaseDriver] for the given URL.
         * @param url the JDBC URL
         * @return the database driver or [UNKNOWN] if not found
         */
        fun fromJdbcUrl(url: String): top.bettercode.generator.DatabaseDriver {
            if (url.isNotBlank()) {
                if (!url.startsWith("jdbc")) {
                    throw IllegalArgumentException("URL must start with 'jdbc'")
                }
                val urlWithoutPrefix = url.substring("jdbc".length)
                        .toLowerCase(Locale.ENGLISH)
                for (driver in values()) {
                    for (urlPrefix in driver.urlPrefixes) {
                        val prefix = ":$urlPrefix:"
                        if (driver !== top.bettercode.generator.DatabaseDriver.UNKNOWN && urlWithoutPrefix.startsWith(prefix)) {
                            return driver
                        }
                    }
                }
            }
            return top.bettercode.generator.DatabaseDriver.UNKNOWN
        }

        /**
         * Find a [DatabaseDriver] for the given product name.
         * @param productName product name
         * @return the database driver or [UNKNOWN] if not found
         */
        fun fromProductName(productName: String): top.bettercode.generator.DatabaseDriver {
            if (productName.isNotBlank()) {
                for (candidate in values()) {
                    if (candidate.matchProductName(productName)) {
                        return candidate
                    }
                }
            }
            return top.bettercode.generator.DatabaseDriver.UNKNOWN
        }
    }

}