package es.lasalle.videoconferencia.s06

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import es.lasalle.videoconferencia.s06.S06Demo
import es.lasalle.videoconferencia.ui.theme.VideoconferenciaTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun s06Navigation_showsMenuScreen() {
        composeTestRule.setContent {
            VideoconferenciaTheme {
                S06Demo(onNavigateBack = { })
            }
        }

        composeTestRule
            .onNodeWithText("S06 - UI Testing y Compose")
            .assertIsDisplayed()
    }

    @Test
    fun s06Navigation_navigateToTesting() {
        composeTestRule.setContent {
            VideoconferenciaTheme {
                S06Demo(onNavigateBack = { })
            }
        }

        // Click on UI Testing card
        composeTestRule
            .onNodeWithText("Compose UI Testing")
            .performClick()

        // Should navigate to testing screen (TestingPlayground)
        composeTestRule
            .onNodeWithText("¿Qué es UI Testing?")
            .assertIsDisplayed()
    }



    @Test
    fun s06Navigation_backButton_works() {
        composeTestRule.setContent {
            VideoconferenciaTheme {
                S06Demo(onNavigateBack = { })
            }
        }

        // Navigate to testing screen
        composeTestRule
            .onNodeWithText("Compose UI Testing")
            .performClick()

        // Click back button
        composeTestRule
            .onNodeWithContentDescription("Volver")
            .performClick()

        // Should be back at menu
        composeTestRule
            .onNodeWithText("S06 - UI Testing y Compose")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("UI Testing con Jetpack Compose")
            .assertIsDisplayed()
    }
}