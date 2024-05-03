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
                0.25f to 25f,
                0.50f to 75f,
                1f to 100f,
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
            10f to 10f,
            20f to 20f,
            25f to 25f,
            40f to 55f,
            60f to 80f,
            70.5f to 85.25f,
        )
        points.forEach {
            assertEquals(it.second, checkPointsValueDistribution.interpolate(it.first), accuracy)
        }
    }

    @Test
    fun `create from pairs and inverse`() {
        val points = listOf(
            10f to 10f,
            20f to 20f,
            25f to 25f,
            40f to 55f,
            60f to 80f,
            70.5f to 85.25f,
        )
        points.forEach {
            assertEquals(it.first, checkPointsValueDistribution.inverse(it.second), accuracy)
        }
    }
}
