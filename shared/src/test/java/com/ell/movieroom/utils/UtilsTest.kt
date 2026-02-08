package com.ell.movieroom.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class UtilsTest {

    @Test
    fun `Valid  mm ss  format with standard values`() {
        assertEquals(630, "10:30".toSeconds())
    }

    @Test
    fun `Valid  hh mm ss  format with standard values`() {
        assertEquals(4230, "01:10:30".toSeconds())
    }

    @Test
    fun `Zero values in  mm ss  format`() {
        assertEquals(0, "00:00".toSeconds())
    }

    @Test
    fun `Zero values in  hh mm ss  format`() {
        assertEquals(0, "00:00:00".toSeconds())
    }

    @Test
    fun `Seconds boundary value  59 `() {
        assertEquals(59, "00:59".toSeconds())
    }

    @Test
    fun `Minutes boundary value  59 `() {
        assertEquals(3540, "59:00".toSeconds())
    }

    @Test
    fun `Hours boundary value`() {
        assertEquals(356400, "99:00:00".toSeconds())
    }

    @Test
    fun `Leading trailing spaces around time string`() {
        assertEquals(62, " 01:02 ".toSeconds())
    }

    @Test
    fun `Internal spaces around delimiters`() {
        assertEquals(3723, "01 : 02 : 03".toSeconds())
    }

    @Test
    fun `Single digit time components`() {
        assertEquals(3723, "1:2:3".toSeconds())
    }

    @Test
    fun `Invalid format with insufficient parts`() {
        assertEquals(0, "60".toSeconds())
    }

    @Test
    fun `Invalid format with excessive parts`() {
        assertEquals(0, "1:2:3:4".toSeconds())
    }

    @Test
    fun `Empty input string`() {
        assertEquals(0, "".toSeconds())
    }

    @Test
    fun `Non numeric input string`() {
        assertEquals(0, "abc:def".toSeconds())
    }

    @Test
    fun `Partial non numeric input`() {
        assertEquals(0, "01:abc:03".toSeconds())
    }

    @Test
    fun `Negative number components`() {
        assertEquals(0, "-01:00:00".toSeconds())
    }

    @Test
    fun `Large number components causing Long overflow`() {
        assertEquals(0, "${Long.MAX_VALUE}:00".toSeconds())
    }

    @Test
    fun `Input with only a colon`() {
        assertEquals(0, ":".toSeconds())
    }

    @Test
    fun `Input with multiple colons together`() {
        assertEquals(0, "1::2".toSeconds())
    }

    @Test
    fun `String with only whitespace`() {
        assertEquals(0, "   ".toSeconds())
    }

    @Test
    fun `toVideoTime with zero value`() {
        assertEquals("00:00", 0L.toVideoTime())
    }

    @Test
    fun `toVideoTime with value under a minute`() {
        assertEquals("00:30", (30 * 1000L).toVideoTime())
    }

    @Test
    fun `toVideoTime with standard mm ss format`() {
        assertEquals("10:30", (630 * 1000L).toVideoTime())
    }

    @Test
    fun `toVideoTime with exactly one hour`() {
        assertEquals("01:00:00", (3600 * 1000L).toVideoTime())
    }

    @Test
    fun `toVideoTime with standard hh mm ss format`() {
        assertEquals("01:10:30", (4230 * 1000L).toVideoTime())
    }

    @Test
    fun `toVideoTime with value just under an hour`() {
        assertEquals("59:59", (3599 * 1000L).toVideoTime())
    }

    @Test
    fun `toVideoTime with single digit components`() {
        assertEquals("01:01:01", (3661 * 1000L).toVideoTime())
    }

    @Test
    fun `toVideoTimeRounded with zero value`() {
        assertEquals("00:00", 0L.toVideoTimeRounded())
    }

    @Test
    fun `toVideoTimeRounded with value rounding down`() {
        assertEquals("00:00", 499L.toVideoTimeRounded())
    }

    @Test
    fun `toVideoTimeRounded with value at threshold`() {
        assertEquals("00:01", 500L.toVideoTimeRounded())
    }

    @Test
    fun `toVideoTimeRounded with value rounding up`() {
        assertEquals("00:01", 999L.toVideoTimeRounded())
    }

    @Test
    fun `toVideoTimeRounded with carry-over to minute`() {
        assertEquals("01:00", 59_999L.toVideoTimeRounded())
    }

    @Test
    fun `toVideoTimeRounded with carry-over to hour`() {
        assertEquals("01:00:00", 3599_500L.toVideoTimeRounded())
    }

    @Test
    fun `toVideoTimeRounded with no rounding needed`() {
        assertEquals("01:10:30", 4230200L.toVideoTimeRounded())
    }

    @Test
    fun `toVideoTimeRounded with rounding on standard time`() {
        assertEquals("01:10:31", 4230800L.toVideoTimeRounded())
    }
}
