package io.monstarlab.mosaic.slider

import io.monstarlab.mosaic.slider.distribution.CheckPointsValuesDistribution
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class CheckPointsValueDistributionTest {

    private lateinit var checkPointsValueDistribution: CheckPointsValuesDistribution
    private val accuracy = 0.0001f

    @Before
    fun setUp() {
        checkPointsValueDistribution = CheckPointsValuesDistribution(
            listOf(
                0f to 0f,
                25f to 25f,
                50f to 75f,
                100f to 100f,
            ),
        )
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `test create from pairs with decreasing value`() {
        assertThrows(CheckPointsValuesDistribution.DecreasingValueException::class.java) {
            CheckPointsValuesDistribution(
                listOf(
                    0f to 0f,
                    5f to 10f,
                    8f to 16f,
                    7f to 20f,
                ),
            )
        }
    }

    @Test
    fun `create from pairs and interpolate`() {
        val points = listOf(
            0.1f to 0.1f,
            0.2f to 0.2f,
            0.25f to 0.25f,
            0.4f to 0.55f,
            0.6f to 0.8f,
            0.75f to 0.875f,
        )
        points.forEach {
            assertEquals(it.second, checkPointsValueDistribution.interpolate(it.first), accuracy)
        }
    }

    @Test
    fun `create from pairs and inverse`() {
        val points = listOf(
            0.1f to 0.1f,
            0.2f to 0.2f,
            0.25f to 0.25f,
            0.4f to 0.55f,
            0.6f to 0.8f,
            0.75f to 0.875f,
        )
        points.forEach {
            assertEquals(it.first, checkPointsValueDistribution.inverse(it.second), accuracy)
        }
    }
}
