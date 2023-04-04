package com.mujapps.jetpackcapstone.utils

import com.google.firebase.Timestamp
import java.text.DateFormat

fun formatDate(timeStamp: Timestamp): String {
    return DateFormat.getDateInstance().format(timeStamp.toDate()).toString().split(",")[0]
}