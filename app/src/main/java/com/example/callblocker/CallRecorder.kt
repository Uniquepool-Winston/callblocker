package com.example.callblocker

import android.media.MediaRecorder
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.IOException

// Experimental call recorder. May not work on many devices and may require additional permissions.
class CallRecorder {
    private var recorder: MediaRecorder? = null
    private var outFile: String? = null

    fun startRecording() : String? {
        try {
            val dir = File(Environment.getExternalStorageDirectory(), "CallBlockerRecords")
            if (!dir.exists()) dir.mkdirs()
            outFile = File(dir, "call_${System.currentTimeMillis()}.mp3").absolutePath

            recorder = MediaRecorder().apply {
                // This configuration is experimental and might not capture both sides on many phones.
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(outFile)
                prepare()
                start()
            }
            Log.d("CallRecorder", "Recording started: $outFile")
        } catch (e: IOException) {
            Log.e("CallRecorder", "startRecording error", e)
            outFile = null
        }
        return outFile
    }

    fun stopRecording() {
        try {
            recorder?.apply {
                stop()
                reset()
                release()
            }
        } catch (e: Exception) {
            Log.e("CallRecorder", "stopRecording error", e)
        } finally {
            recorder = null
        }
    }
}
