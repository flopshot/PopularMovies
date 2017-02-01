package com.example.sean.popularmovies;

import android.test.suitebuilder.TestSuiteBuilder;

import junit.framework.Test;
import junit.framework.TestSuite;

public class FullInstrumentedTestSuite extends TestSuite {
    public static Test suite() {
        return new TestSuiteBuilder(FullInstrumentedTestSuite.class)
              .includeAllPackagesUnderHere().build();
    }

    public FullInstrumentedTestSuite() {
        super();
    }
}