# Android-Intsrumentation-Test-Example

Steps: 
1) add ```classpath 'com.github.grishberg:android-emulator-manager:0.3.9'```
to projects build.gradle

2) add ```apply plugin: 'com.github.grishberg.androidemulatormanager'
          apply from: "ui-tests.gradle"```
to module build.gradle

3) create ui-tests.gradle in module folder and put:

```
// add this to use import *
buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.0.1'
        classpath 'com.github.grishberg:android-emulator-manager:0.3.9'
    }
}

import com.github.grishberg.androidemulatormanager.CreateAndRunEmulatorsTask
import com.github.grishberg.androidemulatormanager.StopEmulatorsTask

Task createAndRunEmulatorsTask = project.tasks.findByName(CreateAndRunEmulatorsTask.NAME)
Task stopEmulatorsTask = project.tasks.findByName(StopEmulatorsTask.NAME)
stopEmulatorsTask.mustRunAfter "connectedAndroidTest"

// configurations for emulators, you can add several emulators : test_phone, test_tablet, e.t.c.
emulatorConfigs {
    test_phone {
        name = "test_phone"
        displayWidth = 768
        displayHeight = 1280
        displayDensity = 320
        apiLevel = 26
        diskSize = 2048
    }
}

emulatorManagerConfig {
    waitingTimeout = 60 * 3 * 1000
}
/**
 * Setup install apk and test apk
 */
def installApkTask = project.tasks.create("installApk") {
    dependsOn('installDebug', 'installDebugAndroidTest')
    finalizedBy(stopEmulatorsTask, "connectedAndroidTest")
    mustRunAfter createAndRunEmulatorsTask
}

/**
 * Starts creating emulators and running instrumental tests.
 */
project.tasks.create("startConnectedTest") {
    finalizedBy createAndRunEmulatorsTask
    finalizedBy installApkTask
    finalizedBy 'assembleDebug'
    finalizedBy 'assembleAndroidTest'
}
```

5) Run with ```./gradlew startConnectedTest```
   
 
   
