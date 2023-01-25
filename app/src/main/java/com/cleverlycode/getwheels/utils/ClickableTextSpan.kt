package com.cleverlycode.getwheels.utils

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.NavDirections
import com.cleverlycode.getwheels.R

fun clickableTextSpan(
    text: String,
    start: Int,
    end: Int,
    context: Context,
    action: NavDirections,
    onClick: (NavDirections) -> Unit
): SpannableString {
    val spannableText = SpannableString(text)
    val clickableSpan = object : ClickableSpan() {
        override fun onClick(widget: View) {
            onClick(action)
        }

        override fun updateDrawState(ds: TextPaint) {
            ds.color = ContextCompat.getColor(context, R.color.blue)
            ds.isUnderlineText = false
        }
    }

    spannableText.setSpan(clickableSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

    return spannableText
}