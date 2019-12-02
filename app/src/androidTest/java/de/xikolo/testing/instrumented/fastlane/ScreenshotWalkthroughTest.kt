package de.xikolo.testing.instrumented.fastlane

import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import de.xikolo.R
import de.xikolo.controllers.main.MainActivity
import de.xikolo.testing.instrumented.mocking.base.BaseMockedTest
import de.xikolo.testing.instrumented.ui.helper.AuthorizationHelper
import de.xikolo.testing.instrumented.ui.helper.NavigationHelper
import org.junit.*
import tools.fastlane.screengrab.Screengrab
import tools.fastlane.screengrab.UiAutomatorScreenshotStrategy
import tools.fastlane.screengrab.locale.LocaleTestRule

/**
 * Serves as the test which walks through the app to capture screenshots for fastlane.
 */
@LargeTest
class ScreenshotWalkthroughTest : BaseMockedTest() {

    @ClassRule
    @JvmField
    val localeTestRule = LocaleTestRule()

    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun login() {
        Screengrab.setDefaultScreenshotStrategy(UiAutomatorScreenshotStrategy())
        AuthorizationHelper.login()
    }

    @After
    fun logout() {
        AuthorizationHelper.logout()
    }

    @Test
    fun myCourses() {
        NavigationHelper.selectNavigationItem(context, R.string.title_section_my_courses)
        Screengrab.screenshot("my_courses")
    }


}
