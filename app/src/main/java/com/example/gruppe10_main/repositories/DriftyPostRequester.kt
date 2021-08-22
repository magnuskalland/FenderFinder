package com.example.gruppe10_main.repositories

import android.util.Log
import com.example.gruppe10_main.R
import com.example.gruppe10_main.misc.*
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.core.extensions.jsonBody

/**
 * Class for handling POST requests to Drifty.
 */
class DriftyPostRequester {

    private var numRequests = 0
    private val token = "Location"

    /**
     * Creates a POST request to Drifty from the given [bodyAsJson].
     * Return a [String] extension URL returned by Drifty, null if failed request.
     */
    fun post(bodyAsJson: String): String? {
        Log.d(TAG, "Making a Drifty POST request with: $bodyAsJson")
        Log.d(TAG, "Starting Drifty POST request ${++numRequests}")
        Utility.startTimer()

        /**
         * Request: content that has been uploaded
         * Response: body, header -> including location/id <-
         * Result: of class Result<ByteArray, FuelError>
         */
        val response = Fuel.post("$DRIFTY_BASE_URL$DRIFTY_POST_REQUEST_EXTENSION")
                .jsonBody(bodyAsJson)
                .authentication().basic(App.getContext().getString(R.string.drifty_username),
                        App.getContext().getString(R.string.drifty_password))
                .header("Connection", "close")
                .header("Content-Length", "100000")
                .response()
        Log.d(TAG, "Finished POST request $numRequests after ${Utility.endTimer()} ms")

        val result = Utility.searchForKeyInHeader(response.second.toString(), token)

        if (result == null) Log.e(TAG, "Token '$token' not in header")
        else Log.d(TAG, "Got extension for GET request: $result")
        return result
    }
}