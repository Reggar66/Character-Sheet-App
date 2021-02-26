package com.awkwardlydevelopedapps.unicharsheet

import android.app.Activity
import android.content.Context

object AppReviewSingleton {

    // Right now shown inside of statistic fragment. Maybe there is a better place?
    fun inAppReview(context: Context, activity: Activity) {
        val inAppReview = InAppReview(context, activity)

        if (inAppReview.checkIfWeeksPassed(InAppReview.ONE_WEEK)) {
            if (inAppReview.getCurrentWeek() != 0L) {
                inAppReview.requestReview()
            }
            inAppReview.updateWeekToCheck()
        }
    }
}