package com.zksolution.cet.sample

import com.zksolution.cet.sample.tests.CurrencySampleActivityTest
import com.zksolution.cet.sample.tests.NumericSampleActivityTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    value = [
        NumericSampleActivityTest::class,
        CurrencySampleActivityTest::class
    ]
)
class TestSuite
