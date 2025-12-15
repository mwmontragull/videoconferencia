package es.lasalle.videoconferencia.s06

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import es.lasalle.videoconferencia.s02.CounterDemo
import es.lasalle.videoconferencia.ui.theme.VideoconferenciaTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CounterDemoUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun counterDemo_displaysInitialValue() {
        composeTestRule.setContent {
            VideoconferenciaTheme {
                CounterDemo(
                    quantity1 = 5,
                    onValueChange = { }
                )
            }
        }

        composeTestRule
            .onNodeWithText("Count: 5")
            .assertIsDisplayed()
    }

    @Test
    fun counterDemo_incrementButton_works() {
        composeTestRule.setContent {
            var currentValue by mutableIntStateOf(0)
            
            VideoconferenciaTheme {
                CounterDemo(
                    quantity1 = currentValue,
                    onValueChange = { currentValue = it }
                )
            }
        }

        composeTestRule
            .onNodeWithText("+")
            .performClick()

        composeTestRule
            .onNodeWithText("Count: 1")
            .assertIsDisplayed()
    }

    @Test
    fun counterDemo_decrementButton_works() {
        composeTestRule.setContent {
            var currentValue by mutableIntStateOf(5)
            
            VideoconferenciaTheme {
                CounterDemo(
                    quantity1 = currentValue,
                    onValueChange = { currentValue = it }
                )
            }
        }

        composeTestRule
            .onNodeWithTag("decrease_button")
            .performClick()

        composeTestRule
            .onNodeWithText("Count: 4")
            .assertIsDisplayed()
    }

    @Test
    fun counterDemo_multipleClicks_updateCorrectly() {
        composeTestRule.setContent {
            var currentValue by mutableIntStateOf(0)
            
            VideoconferenciaTheme {
                CounterDemo(
                    quantity1 = currentValue,
                    onValueChange = { currentValue = it }
                )
            }
        }

        // Click increment 3 times
        repeat(3) {
            composeTestRule
                .onNodeWithText("+")
                .performClick()
        }

        composeTestRule
            .onNodeWithText("Count: 3")
            .assertIsDisplayed()
    }
}