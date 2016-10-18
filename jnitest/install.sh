adb install -r jnitest/build/outputs/apk/jnitest-debug.apk
adb shell am start -n "com.milton.jnitest/com.milton.jnitest.MainActivity" -a android.intent.action.MAIN -c android.intent.category.LAUNCHER