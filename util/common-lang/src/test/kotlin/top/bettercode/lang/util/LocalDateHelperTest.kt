package top.bettercode.lang.util

import org.junit.jupiter.api.Test
import java.time.LocalDate

/**
 * @author Peter Wu
 */
class LocalDateHelperTest {

    private val localDate = LocalDateTimeHelper.of(2018, 3, 5)

    @Test
    fun toDate() {
        System.err.println(LocalDateTimeHelper.toDate(LocalDate.now().minusDays(7)))
    }

    @Test
    fun getFirstDayOfMonth() {
        org.junit.jupiter.api.Assertions.assertEquals("2018-03-01T00:00:00+08:00", localDate.firstDayOfMonth.format())
    }

    @Test
    fun getFirstDayOfNextMonth() {
        org.junit.jupiter.api.Assertions.assertEquals("2018-04-01T00:00:00+08:00", localDate.firstDayOfNextMonth.format())
    }

    @Test
    fun getLastDayOfMonth() {
        org.junit.jupiter.api.Assertions.assertEquals("2018-03-31T00:00:00+08:00", localDate.lastDayOfMonth.format())
    }

    @Test
    fun getFirstDayOfQuarter() {
        org.junit.jupiter.api.Assertions.assertEquals("2018-01-01T00:00:00+08:00", localDate.firstDayOfQuarter.format())
    }

    @Test
    fun getFirstDayOfNextQuarter() {
        org.junit.jupiter.api.Assertions.assertEquals("2018-04-01T00:00:00+08:00", localDate.firstDayOfNextQuarter.format())
    }

    @Test
    fun getLastDayOfQuarter() {
        org.junit.jupiter.api.Assertions.assertEquals("2018-03-31T00:00:00+08:00", localDate.lastDayOfQuarter.format())
    }

    @Test
    fun getFirstDayOfYear() {
        org.junit.jupiter.api.Assertions.assertEquals("2018-01-01T00:00:00+08:00", localDate.firstDayOfYear.format())
    }

    @Test
    fun getFirstDayOfNextYear() {
        org.junit.jupiter.api.Assertions.assertEquals("2019-01-01T00:00:00+08:00", localDate.firstDayOfNextYear.format())
    }

    @Test
    fun getLastDayOfYear() {
        org.junit.jupiter.api.Assertions.assertEquals("2018-12-31T00:00:00+08:00", localDate.lastDayOfYear.format())
    }
}