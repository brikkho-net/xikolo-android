package de.xikolo.storages

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import de.xikolo.App
import de.xikolo.storages.base.BaseStorage
import de.xikolo.storages.base.SharedPreferenceLiveData

class RecentCoursesStorage : BaseStorage(PREF_RECENT_COURSES,Context.MODE_PRIVATE) {

    val recentCourses: LinkedHashSet<Pair<String,String>>?
        get() {
            val json = getString(PREF_RECENT_COURSES)
            val type = object : TypeToken<LinkedHashSet<Pair<String,String>>>() {}.type
            return Gson().fromJson(json, type)
        }

    private val shortcutMax = 3
    public val coursesLive: SharedPreferenceLiveData<String> by lazy {
        // what name does the preference have? What is the sharedPref Parameter supposed to do?
        SharedPreferenceLiveData.SharedPreferenceStringLiveData(PreferenceManager.getDefaultSharedPreferences(App.instance) ,"recentCourses", "")
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
            courses.size < shortcutMax                         -> {
                courses.add(Pair(courseId,title))
            }
            courses.size == shortcutMax                        -> {
                courses.remove(courses.last())
                courses.add(Pair(courseId,title))
            }
        }

        putString(PREF_RECENT_COURSES, Gson().toJson(courses))
        //is this update procedure correct?
        coursesLive.update(Gson().toJson(courses))
    }

    companion object {
        private const val PREF_RECENT_COURSES = "pref_recent_courses"
    }
}