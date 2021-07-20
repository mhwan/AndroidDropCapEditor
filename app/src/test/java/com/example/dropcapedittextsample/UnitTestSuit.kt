package com.example.dropcapedittextsample

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(ArrayHashMapUnitTest::class,
    EmojiUtilUnitTest::class,
    DropCapDataControllerUnitTest::class,
    DropCapIndexHandlerTest::class,
    DropCapStyleHandlerUnitTest::class)
class UnitTestSuit {
}