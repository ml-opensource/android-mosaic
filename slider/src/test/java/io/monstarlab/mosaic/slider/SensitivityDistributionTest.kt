package io.monstarlab.mosaic.slider

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class SensitivityDistributionTest {

    private lateinit var distribution: SensitivityDistribution
    private val accuracy = 0.0001f

    @Before
    fun setUp() {
        distribution = SensitivityDistribution.Builder()
            .add(2f, 0f)
            .add(1f, 0.5f)
            .add(2f, 0.8f)
            .build()
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `range overlap exception`() {
        assertThrows(SensitivityDistribution.OverlappingRangeException::class.java) {
            SensitivityDistribution.Builder()
                .add(0.2f, 0f)
                .add(1f, 0.75f)
                .add(3f, 0.55f)
                .build()
        }
    }

    @Test
    fun `range starts with zero exception`() {
        assertThrows(SensitivityDistribution.FirstValueNotZeroException::class.java) {
            SensitivityDistribution.Builder()
                .add(0.2f, 0.3f)
                .add(1f, 0.75f)
                .build()
        }
    }

    @Test
    fun `out of range value exception`() {
        assertThrows(SensitivityDistribution.OutOfRangeException::class.java) {
            SensitivityDistribution.Builder()
                .add(0.2f, 0f)
                .add(1f, 1.75f)
                .build()
        }
    }

    @Test
    fun `create distribution with no exception`() {
        val distribution = SensitivityDistribution.Builder()
            .add(0.2f, 0f)
            .add(1f, 0.5f)
            .add(3f, 0.8f)
            .build()
        assertNotEquals(distribution.valueFromOffset(0.5f), null)
    }

    @Test
    fun `test value from offset distribution on ranges`() {
        val value0 = distribution.valueFromOffset(0f) ?: 0f // 0
        val value1 = distribution.valueFromOffset(0.3f) ?: 0f // 0.6
        val value2 = distribution.valueFromOffset(0.7f) ?: 0f // 1.2
        val value3 = distribution.valueFromOffset(0.9f) ?: 0f // 1.5
        val value4 = distribution.valueFromOffset(1f) ?: 0f // 1.7

        assertEquals(0f, value0, accuracy)
        assertEquals(0.6f, value1, accuracy)
        assertEquals(1.2f, value2, accuracy)
        assertEquals(1.5f, value3, accuracy)
        assertEquals(1.7f, value4, accuracy)
    }

    @Test
    fun `test offset from value distribution on ranges`() {
        println("---------------------------------------------")
        distribution.equationList.forEach {
            println(it)
        }
        val value0 = distribution.offsetFromValue(0f) ?: 0f // 0
        val value1 = distribution.offsetFromValue(0.6f) ?: 0f // 0.3
        val value2 = distribution.offsetFromValue(1.2f) ?: 0f // 0.7
        val value3 = distribution.offsetFromValue(1.5f) ?: 0f // 0.9
        val value4 = distribution.offsetFromValue(1.69999f) ?: 0f // 1
        assertEquals(0f, value0, accuracy)
        assertEquals(0.3f, value1, accuracy)
        assertEquals(0.7f, value2, accuracy)
        assertEquals(0.9f, value3, accuracy)
        assertEquals(1f, value4, accuracy)
    }
}
