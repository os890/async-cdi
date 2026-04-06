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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * A simple {@link Future} implementation that wraps an already-available result.
 *
 * <p>Used in tests to simulate a completed asynchronous computation.</p>
 *
 * @param <V> the type of the result
 */
public final class AsyncTestResult<V> implements Future<V>
{
    private final V result;

    /**
     * Creates a new completed future holding the given result.
     *
     * @param result the result value
     */
    public AsyncTestResult(V result)
    {
        this.result = result;
    }

    public boolean cancel(boolean mayInterruptIfRunning)
    {
        throw new IllegalStateException();
    }

    public V get() throws InterruptedException, ExecutionException
    {
        return result;
    }

    public V get(long timeout, TimeUnit unit) throws InterruptedException,
            ExecutionException, TimeoutException
    {
        return result;
    }

    public boolean isCancelled()
    {
        return false;
    }

    public boolean isDone()
    {
        return true;
    }
}
