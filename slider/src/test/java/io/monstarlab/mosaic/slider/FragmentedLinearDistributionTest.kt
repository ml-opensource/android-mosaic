package io.monstarlab.mosaic.slider

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class FragmentedLinearDistributionTest {

    private lateinit var distribution: FragmentedLinearDistribution
    private val accuracy = 0.0001f

    @Before
    fun setUp() {
        distribution = FragmentedLinearDistribution.Builder()
            .sliceAt(2f, 0f)
            .sliceAt(1f, 0.5f)
            .sliceAt(2f, 0.8f)
            .build()
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `range overlap exception`() {
        assertThrows(FragmentedLinearDistribution.OverlappingRangeException::class.java) {
            FragmentedLinearDistribution.Builder()
                .sliceAt(0.2f, 0f)
                .sliceAt(1f, 0.75f)
                .sliceAt(3f, 0.55f)
                .build()
        }
    }

    @Test
    fun `range starts with zero exception`() {
        assertThrows(FragmentedLinearDistribution.FirstValueNotZeroException::class.java) {
            FragmentedLinearDistribution.Builder()
                .sliceAt(0.2f, 0.3f)
                .sliceAt(1f, 0.75f)
                .build()
        }
    }

    @Test
    fun `out of range value exception`() {
        assertThrows(FragmentedLinearDistribution.OutOfRangeException::class.java) {
            FragmentedLinearDistribution.Builder()
                .sliceAt(0.2f, 0f)
                .sliceAt(1f, 1.75f)
                .build()
        }
    }

    @Test
    fun `create distribution with no exception`() {
        val distribution = FragmentedLinearDistribution.Builder()
            .sliceAt(0.2f, 0f)
            .sliceAt(1f, 0.5f)
            .sliceAt(3f, 0.8f)
            .build()
        assertNotEquals(distribution.interpolate(0.5f), null)
    }

    @Test
    fun `test value from offset distribution on ranges`() {
        val value0 = distribution.interpolate(0f) ?: 0f // 0
        val value1 = distribution.interpolate(0.3f) ?: 0f // 0.35294117
        val value2 = distribution.interpolate(0.7f) ?: 0f // 0.7058823
        val value3 = distribution.interpolate(0.9f) ?: 0f // 0.88235294
        val value4 = distribution.interpolate(1f) ?: 0f // 1

        assertEquals(0f, value0, accuracy)
        assertEquals(0.3529411f, value1, accuracy)
        assertEquals(0.7058823f, value2, accuracy)
        assertEquals(0.8823529f, value3, accuracy)
        assertEquals(1f, value4, accuracy)
    }

    @Test
    fun `test offset from value distribution on ranges`() {
        println("---------------------------------------------")
        distribution.equationList.forEach {
            println(it)
        }
        val value0 = distribution.inverse(0f) ?: 0f // 0
        val value1 = distribution.inverse(0.35294f) ?: 0f // 0.3
        val value2 = distribution.inverse(0.70588f) ?: 0f // 0.7
        val value3 = distribution.inverse(0.88235f) ?: 0f // 0.9
        val value4 = distribution.inverse(1f) ?: 0f // 1
        assertEquals(0f, value0, accuracy)
        assertEquals(0.3f, value1, accuracy)
        assertEquals(0.7f, value2, accuracy)
        assertEquals(0.9f, value3, accuracy)
        assertEquals(1f, value4, accuracy)
    }
}
