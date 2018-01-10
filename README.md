# Android-Intsrumentation-Test-Example

Steps: 
1) add ```classpath 'com.github.grishberg:android-emulator-manager:0.3.8'```
to projects build.gradle

2) add ```apply plugin: 'com.github.grishberg.androidemulatormanager'
          apply from: "ui-tests.gradle"```
to module build.gradle

3) create ui-tests.gradle in module folder and put:

```
buildscript {
       repositories {
           jcenter()
       }
       dependencies {
           classpath 'com.github.grishberg:android-emulator-manager:0.3.8'
       }
   }
   
import com.github.grishberg.androidemulatormanager.CreateAndRunEmulatorsTask
import com.github.grishberg.androidemulatormanager.DisplayMode
import com.github.grishberg.androidemulatormanager.EmulatorConfig
import com.github.grishberg.androidemulatormanager.StopEmulatorsTask
import com.github.grishberg.androidemulatormanager.ext.EmulatorManagerConfig

EmulatorManagerConfig emulatorsConfig = project.extensions.findByType(EmulatorManagerConfig)
Task createAndRunEmulatorsTask = project.tasks.findByName(CreateAndRunEmulatorsTask.NAME)
Task stopEmulatorsTask = project.tasks.findByName(StopEmulatorsTask.NAME)
stopEmulatorsTask.mustRunAfter "connectedAndroidTest"

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

    doLast {
        EmulatorConfig argPhone = new EmulatorConfig("test_phone", DisplayMode.PHONE_HDPI, 26)
        argPhone.setDiskSize(2048)
        EmulatorConfig[] configs = [argPhone]
        emulatorsConfig.setEmulatorArgs(configs)
        emulatorsConfig.setWaitingTimeout(60 * 3 * 1000)
    }
}
```

5) Run with ```./gradlew startConnectedTest```
   
 
   
