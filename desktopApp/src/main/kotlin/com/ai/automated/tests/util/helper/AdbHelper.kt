package com.ai.automated.tests.util.helper

import java.io.File
import java.lang.Thread.sleep

class AdbHelper {

    companion object {

        private const val DUMP_PATH_ON_DEVICE = "/sdcard/ui.xml"
        private const val DUMP_PATH = "data/dump/"
        private const val DUMP_FILE = "ui.xml"

        /** Dumps the current UI */
        fun dump(){
            val dumpFile = File(DUMP_PATH)
            dumpFile.mkdirs()
            ProcessBuilder("adb", "shell", "uiautomator", "dump", DUMP_PATH_ON_DEVICE).start().waitFor()
        }

        fun pullDump():File{
            ProcessBuilder("adb", "pull", DUMP_PATH_ON_DEVICE, DUMP_PATH).start().waitFor()
            return File(DUMP_PATH+DUMP_FILE)
        }

        fun click(x:Int, y:Int){
            ProcessBuilder("adb", "shell", "input", "tap", "$x", "$y").start().waitFor()
        }

        fun goBack(){
            ProcessBuilder("adb", "shell", "input", "keyevent", "KEYCODE_BACK").start().waitFor()
        }

        fun input(text:String){
            ProcessBuilder("adb", "shell", "input", "text", text).start().waitFor()
        }

        fun swipeUp(){
            ProcessBuilder("adb", "shell", "input", "swipe", "500", "1500", "500", "500").start().waitFor()
        }

        fun swipeDown(){
            ProcessBuilder("adb", "shell", "input", "swipe", "500", "500", "500", "1500").start().waitFor()
        }
    }
}