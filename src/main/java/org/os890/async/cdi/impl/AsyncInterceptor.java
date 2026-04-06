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

package org.os890.async.cdi.impl;

import org.apache.deltaspike.cdise.api.ContextControl;
import org.os890.async.cdi.api.Async;

import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * CDI interceptor that executes methods annotated with {@link Async}
 * on a separate thread using an {@link ExecutorService}.
 *
 * <p>For void methods, the interceptor returns {@code null} immediately.
 * For methods returning {@link Future}, the interceptor returns a
 * {@link FutureUnwrapper} that delegates to the actual result.</p>
 */
@Interceptor
@Async
public class AsyncInterceptor
{
    @Inject
    private Instance<ContextControl> contextControlInstance;

    /**
     * Intercepts the method invocation and dispatches it asynchronously.
     *
     * @param invocationContext the intercepted invocation context
     * @return {@code null} for void methods, or a {@link FutureUnwrapper} for Future-returning methods
     * @throws Exception if the executor submission fails
     */
    @AroundInvoke
    public Object executeAsynchronous(InvocationContext invocationContext) throws Exception
    {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        try
        {
            Future<Object> result = executor.submit(new AsyncExecutor(invocationContext, contextControlInstance));

            Class<?> returnType = invocationContext.getMethod().getReturnType();
            if ("void".equalsIgnoreCase(returnType.getName()) || Void.class.isAssignableFrom(returnType))
            {
                return null;
            }
            return new FutureUnwrapper<>(result);
        }
        finally
        {
            executor.shutdown(); //won't stop immediately
        }
    }
}
