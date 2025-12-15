package es.lasalle.videoconferencia.s06

import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import es.lasalle.videoconferencia.s02.DemoChip
import es.lasalle.videoconferencia.ui.theme.VideoconferenciaTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DemoChipUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun demoChip_isDisplayed() {
        composeTestRule.setContent {
            VideoconferenciaTheme {
                DemoChip(text = "Test Chip")
            }
        }

        composeTestRule
            .onNodeWithText("Test Chip")
            .assertIsDisplayed()
    }

    @Test
    fun demoChip_canBeClicked() {
        var clicked = false

        composeTestRule.setContent {
            VideoconferenciaTheme {
                DemoChip(
                    text = "Clickable Chip",
                    modifier = androidx.compose.ui.Modifier.testTag("demo_chip")
                )
            }
        }

        composeTestRule
            .onNodeWithTag("demo_chip")
            .performClick()
    }

    @Test
    fun demoChip_showsCorrectText() {
        val testText = "Custom Text"

        composeTestRule.setContent {
            VideoconferenciaTheme {
                DemoChip(text = testText)
            }
        }

        composeTestRule
            .onNodeWithText(testText)
            .assertIsDisplayed()
    }
}