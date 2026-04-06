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

import org.os890.async.cdi.api.Async;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

import java.util.concurrent.CountDownLatch;

/**
 * CDI observer that handles {@link AsyncTestEvent} asynchronously.
 *
 * <p>The observer method is annotated with {@link Async} and includes
 * a deliberate delay to verify asynchronous execution.</p>
 */
@ApplicationScoped
public class TestObserver
{
    static final CountDownLatch TEST_LATCH = new CountDownLatch(1);

    private boolean called = false;

    /**
     * Observes the test event asynchronously with a simulated delay.
     *
     * @param testEvent the fired event
     */
    @Async
    public void observeAsync(@Observes AsyncTestEvent testEvent)
    {
        try
        {
            Thread.sleep(5000);
        }
        catch (InterruptedException e)
        {
            throw new IllegalStateException(e);
        }
        this.called = true;
        TEST_LATCH.countDown();
    }

    /**
     * Returns whether the observer method has been invoked.
     *
     * @return {@code true} if the observer has been called
     */
    public boolean isCalled()
    {
        return called;
    }
}
