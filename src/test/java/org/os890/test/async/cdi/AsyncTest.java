/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.os890.test.async.cdi;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.os890.cdi.addon.dynamictestbean.EnableTestBeans;

import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests for the {@code @Async} CDI interceptor verifying asynchronous
 * execution of void methods, Future-returning methods, and CDI events.
 */
@EnableTestBeans
class AsyncTest
{
    @Inject
    private TestService1 testService1;

    @Inject
    private TestService2 testService2;

    @Inject
    private TestObserver testObserver;

    @Inject
    private Event<AsyncTestEvent> asyncEvent;

    @Test
    void testAsyncService1()
    {
        assertNotNull(testService1);

        testService1.call();

        assertFalse(testService1.isCalled());

        try
        {
            TestService1.TEST_LATCH.await(10, TimeUnit.SECONDS);
        }
        catch (InterruptedException e)
        {
            fail();
        }

        assertTrue(testService1.isCalled());
    }

    @Test
    void testAsyncService2()
    {
        assertNotNull(testService2);

        Future<Boolean> result = testService2.call();

        assertFalse(testService2.isCalled());

        try
        {
            assertTrue(result.get(10, TimeUnit.SECONDS));
        }
        catch (Exception e)
        {
            fail();
        }
        assertTrue(result.isDone());
        assertTrue(testService2.isCalled());
    }

    @Test
    @Disabled("works with owb 1.2+ and versions of tomee which are based on it"
            + " - owb 1.1.x uses Method#isAccessible instead of"
            + " Modifier.isPublic(method.getModifiers()) to check if the"
            + " (interceptor-)proxy should be used")
    void testAsyncEvent()
    {
        assertNotNull(testObserver);
        assertNotNull(asyncEvent);

        asyncEvent.fire(new AsyncTestEvent());

        assertFalse(testObserver.isCalled());

        try
        {
            TestObserver.TEST_LATCH.await(10, TimeUnit.SECONDS);
        }
        catch (InterruptedException e)
        {
            fail();
        }

        assertTrue(testObserver.isCalled());
    }
}
