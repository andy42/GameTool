package com.jaehl.gametools.util

object Log {
    fun i(tag : String, value : String) {
        System.out.println("$tag : $value")
    }
    fun e(tag : String, value : String?) {
        System.err.println("$tag : $value")
    }
}