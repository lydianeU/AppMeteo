package fr.afpa.appmeteo

import fr.afpa.appmeteo.utils.Formatter
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class FormatterTest {

    @Test
    fun whenTimeIsGiven_ShouldDisplayHoursMinutesSeconds() {
        val currentTimeST = 1623226505
        val formatter = Formatter()
        assertEquals("10:15:05", formatter.formatTimeDisplay(1623226505))
    }
}