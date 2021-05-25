package com.awkwardlydevelopedapps.unicharsheet.service

import android.app.Activity
import android.content.Context
import android.util.Log
import com.awkwardlydevelopedapps.unicharsheet.common.utils.LogWrapper
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManagerFactory
import java.util.concurrent.TimeUnit

class InAppReview(
    context: Context,
    private val activity: Activity
) {


    private val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)

    private var reviewInfo: ReviewInfo? = null
    private val manager = ReviewManagerFactory.create(context)


    fun requestReview() {
        // Requesting Review info
        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { request ->
            if (request.isSuccessful) {
                // We got the ReviewInfo object
                reviewInfo = request.result

                //Start review flow
                startReviewFlow()
            } else {
                // There was some problem, continue regardless of the result.
                reviewInfo = null
            }
        }
    }

    fun startReviewFlow() {
        reviewInfo?.let {
            LogWrapper.v("REVIEW", "TRYING to launch Review Flow")

            val flow = manager.launchReviewFlow(activity, it)
            flow.addOnCompleteListener { _ ->
                LogWrapper.v("REVIEW", "Launched Review Flow")
            }
            flow.addOnFailureListener {
                LogWrapper.v("REVIEW", "FAILED to launch Review Flow")
            }
        }


    }

    fun checkIfWeeksPassed(weeks: Long): Boolean {
        val lastTimeStamp = sharedPref.getLong(PREF_KEY_DAYS, 0)
        val currentTimeStamp = System.currentTimeMillis()

        LogWrapper.v(
            "DAYS",
            "Days passed " + TimeUnit.MILLISECONDS.toDays((currentTimeStamp - lastTimeStamp))
        )

        return (currentTimeStamp - lastTimeStamp) >= TimeUnit.DAYS.toMillis(weeks)
    }

    fun updateWeekToCheck() {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putLong(PREF_KEY_DAYS, System.currentTimeMillis())
            commit()
        }
    }

    fun getCurrentWeek(): Long {
        return sharedPref.getLong(PREF_KEY_DAYS, 0)
    }

    companion object {
        const val ZERO_WEEKS: Long = 0
        const val ONE_WEEK: Long = 7
        const val TWO_WEEKS: Long = 14
        private const val PREF_KEY_DAYS = "DAYS"
    }
}


