/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.os890.async.cdi.impl;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

//needed because the interceptor has to return the future immediately,
// but it needs to be part of the method-signature as well to use it in the application correctly
class FutureUnwrapper<V extends Future<Object>> implements Future<Object>
{
    //future which wraps a future
    private final V wrappingFuture;

    FutureUnwrapper(V future)
    {
        this.wrappingFuture = future;
    }

    public boolean cancel(boolean mayInterruptIfRunning)
    {
        return this.wrappingFuture.cancel(mayInterruptIfRunning);
    }

    public Object get() throws InterruptedException, ExecutionException
    {
        Object result = wrappingFuture.get();

        if (result instanceof Future)
        {
            //needed because we have to return the real result and not the wrapped future
            return ((Future<Object>) result).get();
        }
        return result;
    }

    public Object get(long timeout, TimeUnit unit) throws InterruptedException,
            ExecutionException, TimeoutException
    {
        Object result = wrappingFuture.get(timeout, unit);

        if (result instanceof Future)
        {
            //needed because we have to return the real result and not the wrapped future
            return ((Future<Object>) result).get();
        }
        return result;
    }

    public boolean isCancelled()
    {
        return wrappingFuture.isCancelled();
    }

    public boolean isDone()
    {
        return wrappingFuture.isDone();
    }
}
