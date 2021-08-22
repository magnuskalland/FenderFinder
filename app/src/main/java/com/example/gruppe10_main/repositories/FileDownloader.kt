package com.example.gruppe10_main.repositories

import android.app.Application
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.util.Log
import com.example.gruppe10_main.R
import com.example.gruppe10_main.misc.App
import com.example.gruppe10_main.misc.TAG
import java.io.File

/**
 * Class for handling file downloads. Creates a [BroadcastReceiver] to alert device when download
 * is finished.
 */
class FileDownloader(
    private val repository: DriftyRepository,
) : Application() {

    private val context = App.getContext()
    private var downloadId: Long = 0
    private lateinit var downloadManager: DownloadManager
    private lateinit var request: DownloadManager.Request

    private val filename = "results.nc"
    private lateinit var pathname: String

    /**
     * Downloads file from [uri] to internal storage under
     * directory path '/storage/emulated/0/Android/data/com.example.Gruppe10_main/files/results.nc'.
     */
    fun download(uri: String) {
        downloadManager = context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        Log.d(TAG, "Starting download from: $uri")

        request = DownloadManager.Request(Uri.parse(uri))
            .setTitle(context.getString(R.string.download_title))
            .setDescription(context.getString(R.string.download_description))
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalFilesDir(context, "", filename)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)

        downloadId = downloadManager.enqueue(request)
        context.registerReceiver(broadcastReceiver,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    private var broadcastReceiver = object:BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id: Long? = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (id == downloadId) notifyDownloadFinished()
        }
    }

    /**
     * Callback invoked when download is finished. Deletes downloaded file after execution.
     */
    private fun notifyDownloadFinished() {
        pathname = "${context.getExternalFilesDir(filename)}"
        Log.d(TAG, "Finished downloading to $pathname")
        repository.notifyDownloadFinished(pathname)
        File(pathname).delete()
        Log.d(TAG, "Deleted file '$filename'")
    }
}