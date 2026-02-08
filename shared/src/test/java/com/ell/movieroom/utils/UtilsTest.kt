package com.ell.movieroom.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class UtilsTest {

    @Test
    fun `Valid  mm ss  format with standard values`() {
        assertEquals(630, timeStringToSeconds("10:30"))
    }

    @Test
    fun `Valid  hh mm ss  format with standard values`() {
        assertEquals(4230, timeStringToSeconds("01:10:30"))
    }

    @Test
    fun `Zero values in  mm ss  format`() {
        assertEquals(0, timeStringToSeconds("00:00"))
    }

    @Test
    fun `Zero values in  hh mm ss  format`() {
        assertEquals(0, timeStringToSeconds("00:00:00"))
    }

    @Test
    fun `Seconds boundary value  59 `() {
        assertEquals(59, timeStringToSeconds("00:59"))
    }

    @Test
    fun `Minutes boundary value  59 `() {
        assertEquals(3540, timeStringToSeconds("59:00"))
    }

    @Test
    fun `Hours boundary value`() {
        assertEquals(356400, timeStringToSeconds("99:00:00"))
    }

    @Test
    fun `Leading trailing spaces around time string`() {
        assertEquals(62, timeStringToSeconds(" 01:02 "))
    }

    @Test
    fun `Internal spaces around delimiters`() {
        assertEquals(3723, timeStringToSeconds("01 : 02 : 03"))
    }

    @Test
    fun `Single digit time components`() {
        assertEquals(3723, timeStringToSeconds("1:2:3"))
    }

    @Test
    fun `Invalid format with insufficient parts`() {
        assertEquals(0, timeStringToSeconds("60"))
    }

    @Test
    fun `Invalid format with excessive parts`() {
        assertEquals(0, timeStringToSeconds("1:2:3:4"))
    }

    @Test
    fun `Empty input string`() {
        assertEquals(0, timeStringToSeconds(""))
    }

    @Test
    fun `Non numeric input string`() {
        assertEquals(0, timeStringToSeconds("abc:def"))
    }

    @Test
    fun `Partial non numeric input`() {
        assertEquals(0, timeStringToSeconds("01:abc:03"))
    }

    @Test
    fun `Negative number components`() {
        assertEquals(0, timeStringToSeconds("-01:00:00"))
    }

    @Test
    fun `Large number components causing Long overflow`() {
        assertEquals(0, timeStringToSeconds("${Long.MAX_VALUE}:00"))
    }

    @Test
    fun `Input with only a colon`() {
        assertEquals(0, timeStringToSeconds(":"))
    }

    @Test
    fun `Input with multiple colons together`() {
        assertEquals(0, timeStringToSeconds("1::2"))
    }

    @Test
    fun `String with only whitespace`() {
        assertEquals(0, timeStringToSeconds("   "))
    }

}
