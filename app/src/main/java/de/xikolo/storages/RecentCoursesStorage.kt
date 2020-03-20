package de.xikolo.storages

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import de.xikolo.storages.base.BaseStorage
import java.util.ArrayList

class RecentCoursesStorage : BaseStorage(PREF_RECENT_COURSES,Context.MODE_PRIVATE) {

    val recentCourses: LinkedHashSet<Pair<String,String>>?
        get() {
            val json = getString(PREF_RECENT_COURSES)
            val type = object : TypeToken<LinkedHashSet<Pair<String,String>>>() {}.type
            return Gson().fromJson(json, type)
        }

    fun addCourse(courseId : String, title : String) {
        var courses: LinkedHashSet<Pair<String,String>>? = recentCourses

        if (recentCourses == null) {
            courses = LinkedHashSet<Pair<String,String>>()
        }

        when {
            courses!!.contains(Pair(courseId,title)) -> {
                courses.remove(Pair(courseId,title))
                courses.add(Pair(courseId,title))
            }
            courses.size < 3                         -> {
                courses.add(Pair(courseId,title))
            }
            courses.size == 3                        -> {
                val leastRecentCourse = courses.last()
                courses.remove(leastRecentCourse)
                courses.add(Pair(courseId,title))
            }
        }

        putString(PREF_RECENT_COURSES, Gson().toJson(courses))
    }


    //if course finished remove from list
//    fun deleteDownloadNotification(notification: String) {
//        val notifications = downloadNotifications
//        notifications?.remove(notification)
//        putString(DOWNLOAD_NOTIFICATIONS, Gson().toJson(notifications))
//    }

    companion object {
        private const val PREF_RECENT_COURSES = "pref_recent_courses"
    }
}