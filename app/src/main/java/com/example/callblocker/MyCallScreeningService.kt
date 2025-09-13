package com.example.callblocker

import android.telecom.CallScreeningService
import android.telecom.Call.Details
import android.util.Log

class MyCallScreeningService : CallScreeningService() {

    override fun onScreenCall(callDetails: Details) {
        val incomingNumber = callDetails.handle?.schemeSpecificPart ?: ""
        Log.d("CallScreen", "Incoming: $incomingNumber")

        val shouldBlock = isNumberBlocked(incomingNumber)

        if (shouldBlock) {
            val response = CallResponse.Builder()
                .setDisallowCall(true)
                .setRejectCall(true)
                .setSkipCallLog(true)
                .setSkipNotification(true)
                .build()
            respondToCall(callDetails, response)
            Log.d("CallScreen", "Blocked: $incomingNumber")
        } else {
            val response = CallResponse.Builder()
                .setDisallowCall(false)
                .build()
            respondToCall(callDetails, response)
        }
    }

    private fun isNumberBlocked(incoming: String): Boolean {
        var num = incoming
        if (num.startsWith("tel:")) num = num.removePrefix("tel:")

        val prefs = getSharedPreferences("block_prefs", MODE_PRIVATE)
        val set = prefs.getStringSet("blocks", setOf("+99878")) ?: setOf("+99878")

        for (p in set) {
            val normalizedP = p.trim()
            if (normalizedP.isEmpty()) continue
            val pNorm = normalizedP.replace("\\s".toRegex(), "")
            if (num.startsWith(pNorm) || num.startsWith(pNorm.removePrefix("+"))) {
                return true
            }
        }
        return false
    }
}
