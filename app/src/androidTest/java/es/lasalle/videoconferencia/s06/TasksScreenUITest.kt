package es.lasalle.videoconferencia.s06

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import es.lasalle.videoconferencia.s02.TasksScreen
import es.lasalle.videoconferencia.ui.theme.VideoconferenciaTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TasksScreenUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun tasksScreen_showsCorrectTitle() {
        composeTestRule.setContent {
            VideoconferenciaTheme {
                TasksScreen()
            }
        }

        composeTestRule
            .onNodeWithText("Tasks")
            .assertIsDisplayed()
    }

    @Test
    fun tasksScreen_showsAddButton() {
        composeTestRule.setContent {
            VideoconferenciaTheme {
                TasksScreen()
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Add task")
            .assertIsDisplayed()
    }

    @Test
    fun tasksScreen_addButton_createsNewTask() {
        composeTestRule.setContent {
            VideoconferenciaTheme {
                TasksScreen()
            }
        }

        // Initially no tasks
        composeTestRule
            .onAllNodesWithContentDescription("Delete task")
            .assertCountEquals(0)

        // Click add button
        composeTestRule
            .onNodeWithContentDescription("Add task")
            .performClick()

        // Should have one task now
        composeTestRule
            .onNodeWithText("Task 1")
            .assertIsDisplayed()

        composeTestRule
            .onAllNodesWithContentDescription("Delete task")
            .assertCountEquals(1)
    }

    @Test
    fun tasksScreen_addMultipleTasks() {
        composeTestRule.setContent {
            VideoconferenciaTheme {
                TasksScreen()
            }
        }

        // Add 3 tasks
        repeat(3) {
            composeTestRule
                .onNodeWithContentDescription("Add task")
                .performClick()
        }

        // Should have 3 tasks
        composeTestRule
            .onNodeWithText("Task 1")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Task 2")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Task 3")
            .assertIsDisplayed()

        composeTestRule
            .onAllNodesWithContentDescription("Delete task")
            .assertCountEquals(3)
    }

    @Test
    fun tasksScreen_deleteTask_removesFromList() {
        composeTestRule.setContent {
            VideoconferenciaTheme {
                TasksScreen()
            }
        }

        // Add a task
        composeTestRule
            .onNodeWithContentDescription("Add task")
            .performClick()

        // Verify task exists
        composeTestRule
            .onNodeWithText("Task 1")
            .assertIsDisplayed()

        // Delete the task
        composeTestRule
            .onAllNodesWithContentDescription("Delete task")[0]
            .performClick()

        // Task should be gone
        composeTestRule
            .onAllNodesWithContentDescription("Delete task")
            .assertCountEquals(0)
    }
}