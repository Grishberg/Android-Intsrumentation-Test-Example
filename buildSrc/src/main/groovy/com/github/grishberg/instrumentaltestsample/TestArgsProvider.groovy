package com.github.grishberg.instrumentaltestsample;

/**
 * Created by grishberg on 12.12.17.
 */

import com.github.grishberg.tests.DeviceWrapper;
import com.github.grishberg.tests.InstrumentationArgsProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * Custom args provider for instrumentation tests.
 */
class TestArgsProvider implements InstrumentationArgsProvider {

    private static final String TABLET_ANNOTATION =
            "com.github.grishberg.instrumentaltestsample.TabletTest"

    @Override
    Map<String, String> provideInstrumentationArgs(DeviceWrapper targetDevice) {
        HashMap<String, String> args = new HashMap<>()
        if (targetDevice.name == "test_tablet") {
            args.put("annotation", TABLET_ANNOTATION)
        } else {
            args.put("notAnnotation", TABLET_ANNOTATION)
        }
        return args
    }
}
