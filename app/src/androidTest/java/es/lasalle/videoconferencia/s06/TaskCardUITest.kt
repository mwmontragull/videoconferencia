package es.lasalle.videoconferencia.s06

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import es.lasalle.videoconferencia.s02.TaskCard
import es.lasalle.videoconferencia.ui.theme.VideoconferenciaTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TaskCardUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun taskCard_displaysTitle() {
        val taskTitle = "Test Task Title"

        composeTestRule.setContent {
            VideoconferenciaTheme {
                TaskCard(
                    title = taskTitle,
                    onRemove = { },
                    onShare = { }
                )
            }
        }

        composeTestRule
            .onNodeWithText(taskTitle)
            .assertIsDisplayed()
    }

    @Test
    fun taskCard_shareButton_isDisplayed() {
        composeTestRule.setContent {
            VideoconferenciaTheme {
                TaskCard(
                    title = "Test Task",
                    onRemove = { },
                    onShare = { }
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Share task")
            .assertIsDisplayed()
    }

    @Test
    fun taskCard_deleteButton_isDisplayed() {
        composeTestRule.setContent {
            VideoconferenciaTheme {
                TaskCard(
                    title = "Test Task",
                    onRemove = { },
                    onShare = { }
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Delete task")
            .assertIsDisplayed()
    }

    @Test
    fun taskCard_shareButton_callsCallback() {
        var shareClicked = false

        composeTestRule.setContent {
            VideoconferenciaTheme {
                TaskCard(
                    title = "Test Task",
                    onRemove = { },
                    onShare = { shareClicked = true }
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Share task")
            .performClick()

        assertTrue("Share callback was not called", shareClicked)
    }

    @Test
    fun taskCard_deleteButton_callsCallback() {
        var deleteClicked = false

        composeTestRule.setContent {
            VideoconferenciaTheme {
                TaskCard(
                    title = "Test Task",
                    onRemove = { deleteClicked = true },
                    onShare = { }
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Delete task")
            .performClick()

        assertTrue("Delete callback was not called", deleteClicked)
    }
}