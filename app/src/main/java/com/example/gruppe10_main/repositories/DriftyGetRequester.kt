package com.example.gruppe10_main.repositories

import android.util.Log
import com.example.gruppe10_main.dataclasses.GetRequest
import com.example.gruppe10_main.misc.*
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitString
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.delay

/**
 * Class for handling GET requests to Drifty.
 */
class DriftyGetRequester {

    /**
     * Creates a GET request to Drifty, returns a [String] of the
     * download URL to .nc file when finished, null if failed request.
     * Polls API every [POLL_INTERVAL_SEC] times [M_MILLIS] ms, returns if waiting time
     * exceeds [MAX_WAITING_TIME_MILLIS].
     */
    suspend fun get(gson: Gson, extension: String?): String? {
        Log.d(TAG,
        "Starting GET request to Drifty, polling approximately every $POLL_INTERVAL_SEC seconds")

        var result: GetRequest?
        Utility.startTimer()

        while (true) {
            try {
                Log.d(TAG, "Polling... (${Utility.endTimer() / M_MILLIS} secs after first poll)")
                result = gson.fromJson(Fuel.get("$DRIFTY_BASE_URL$extension").awaitString(),
                        GetRequest::class.java)
                break

            } catch (e: JsonSyntaxException) {
                Log.w(TAG, "Unsuccessful poll: $e}")
                if (Utility.endTimer() >= MAX_WAITING_TIME_MILLIS) {
                    Log.d(TAG, "Maximum set waiting time exceeded, returning...")
                    return null
                }
                delay(POLL_INTERVAL_SEC * M_MILLIS)
            }
        }

        Log.d(TAG, "Data successfully retrieved after approximately " +
                "${Utility.endTimer() / M_MILLIS} secs")

        Log.d(TAG, result.toString())
        if (result?.result?.status?.success == true) return result.result?.files?.main.toString()
        return null

    }
}