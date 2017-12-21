package com.github.grishberg.instrumentaltestsample

import com.github.grishberg.androidemulatormanager.CreateAndRunEmulatorsTask
import com.github.grishberg.androidemulatormanager.DisplayMode
import com.github.grishberg.androidemulatormanager.EmulatorConfig
import com.github.grishberg.androidemulatormanager.EmulatorManagerConfig
import com.github.grishberg.androidemulatormanager.StopAndDeleteEmulatorsTask
import com.github.grishberg.tests.InstrumentalTestTask
import com.github.grishberg.tests.InstrumentationInfo
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task;

/**
 * Created by grishberg on 12.12.17.
 */
class RunTestPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        EmulatorManagerConfig emulatorsConfig = project.extensions.findByType(EmulatorManagerConfig)
        Task instrumentalTestTask = project.tasks.findByName(InstrumentalTestTask.TASK_NAME)
        Task createAndRunEmulatorsTask = project.tasks.findByName(CreateAndRunEmulatorsTask.NAME)
        Task stopEmulatorsTask = project.tasks.findByName(StopAndDeleteEmulatorsTask.NAME)
        instrumentalTestTask.finalizedBy stopEmulatorsTask

        /**
         * Setup custom instrumentation test runner.
         */
        def runTestTask = project.tasks.create("runTestTask") {
            dependsOn('installDebug', 'installDebugAndroidTest')
            finalizedBy instrumentalTestTask
            mustRunAfter createAndRunEmulatorsTask
            group 'android'
            doLast {
                println "-------------------- setup instrumentation tests ------------------"

                // Custom Args provider for instrumentation test
                instrumentalTestTask.instrumentationArgsProvider = new TestArgsProvider()

                def instrumentationInfo = new InstrumentationInfo.Builder(
                        "com.github.grishberg.instrumentaltestsample",
                        "com.github.grishberg.instrumentaltestsample.test",
                        "android.support.test.runner.AndroidJUnitRunner")
                        .setFlavorName("TEST_FLAVOR")
                        .build() as InstrumentationInfo

                instrumentalTestTask.setInstrumentationInfo(instrumentationInfo)
            }
        }

/**
 * Starts creating emulators and running instrumental tests.
 */
        project.tasks.create("startConnectedTest") {
            finalizedBy createAndRunEmulatorsTask
            finalizedBy runTestTask
            finalizedBy 'assembleDebug'
            finalizedBy 'assembleAndroidTest'

            group 'android'
            doLast {
                println "--------------- setup emulators ----------------"
                EmulatorConfig argPhone = new EmulatorConfig("test_phone",
                        DisplayMode.PHONE_HDPI, 26)
                argPhone.setDiskSize(2048)
                EmulatorConfig argTablet = new EmulatorConfig("test_tablet",
                        DisplayMode.TABLET_XHDPI, 26)
                argTablet.setDiskSize(2048)
                EmulatorConfig[] args = [argPhone, argTablet]

                emulatorsConfig.setEmulatorArgs(args)
                emulatorsConfig.setWaitingTimeout(60 * 3 * 1000)
            }
        }
    }
}
