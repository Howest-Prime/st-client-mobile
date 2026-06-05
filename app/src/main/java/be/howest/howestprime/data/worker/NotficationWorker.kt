package be.howest.howestprime.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class NotficationWorker(
    context: Context,
    params: WorkerParameters
): CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val ticketID = inputData.getString("TICKET_ID") ?: return Result.failure()
        val movieName = inputData.getString("MOVIE_NAME") ?: return Result.failure()
        val date = inputData.getString("DATE") ?: return Result.failure()
        val time = inputData.getString("TIME") ?: return Result.failure()

        NotificationHelper.showMovieStartReminderNotification(applicationContext, ticketID, movieName, date, time )
        return Result.success()
    }


}