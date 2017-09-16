package com.mobile.utils

import android.animation.Animator
import android.graphics.Paint
import android.provider.Contacts
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewPropertyAnimator
import android.widget.EditText
import android.widget.TextView
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.channels.actor
import kotlinx.coroutines.experimental.delay

/**
 * Created by jimji on 2017/9/16.
 */
fun TextView.underLine() {
    paint.flags = paint.flags or Paint.UNDERLINE_TEXT_FLAG
    paint.isAntiAlias = true
}

fun TextView.deleteLine() {
    paint.flags = paint.flags or Paint.STRIKE_THRU_TEXT_FLAG
    paint.isAntiAlias = true
}

fun TextView.bold() {
    paint.isFakeBoldText = true
    paint.isAntiAlias = true
}

fun View.visiable() {
    visibility = View.VISIBLE
}

inline fun View.visiableIf(block: () -> Boolean) {
    if (visibility != View.VISIBLE && block()) {
        visibility = View.VISIBLE
    }
}

fun View.invisiable() {
    visibility = View.INVISIBLE
}

inline fun View.invisiableIf(block: () -> Boolean) {
    if (visibility != View.INVISIBLE && block()) {
        visibility = View.INVISIBLE
    }
}


fun View.gone() {
    visibility = View.GONE
}

inline fun View.goneIf(block: () -> Boolean) {
    if (visibility != View.GONE && block()) {
        visibility = View.GONE
    }
}

fun View.toggle() {
    visibility = if (visibility == View.GONE) View.VISIBLE
    else View.GONE
}

//timeout时间内只有一次有效，解决重复点击问题
fun View.onClick(timeout: Long, action: () -> Unit) {
    val realAction: suspend () -> Unit = {
        action()
        delay(timeout)
    }
    val eventActor = actor<Unit>(CommonPool) {
        for (event in channel) realAction()
    }
    setOnClickListener {
        eventActor.offer(Unit)
    }
}

fun ViewPropertyAnimator.afterDone(todo: () -> Unit): ViewPropertyAnimator {
    setListener(object : Animator.AnimatorListener {
        override fun onAnimationRepeat(p0: Animator?) {
        }

        override fun onAnimationEnd(p0: Animator) {
            todo()
        }

        override fun onAnimationCancel(p0: Animator?) {
        }

        override fun onAnimationStart(p0: Animator?) {
        }
    })
    return this
}

val EditText.value: String
    get() = text.toString()

fun EditText.onTextChange(todo: (String) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
            todo(p0.toString())
        }
    })
}