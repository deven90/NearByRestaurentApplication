package com.example.myrestaurentapplication.utility

import android.content.Context
import android.widget.Toast

fun Context.shortToast(message: CharSequence) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()