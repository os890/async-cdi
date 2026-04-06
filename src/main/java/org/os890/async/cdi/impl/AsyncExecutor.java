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
import org.apache.deltaspike.core.util.ExceptionUtils;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.interceptor.InvocationContext;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * Callable that executes an intercepted method invocation on a separate thread.
 *
 * <p>If a DeltaSpike {@link ContextControl} implementation is available,
 * a request scope is started before and stopped after invocation.</p>
 */
class AsyncExecutor implements Callable<Object>
{
    private final InvocationContext invocationContext;
    private final ContextControl contextControl;

    /**
     * Creates a new executor for the given invocation context.
     *
     * @param invocationContext       the intercepted method invocation context
     * @param contextControlInstance  CDI instance for optional context control
     */
    AsyncExecutor(InvocationContext invocationContext, Instance<ContextControl> contextControlInstance)
    {
        this.invocationContext = invocationContext;

        if (!contextControlInstance.isUnsatisfied())
        {
            contextControl = contextControlInstance.get();
        }
        else
        {
            contextControl = null;
        }
    }

    @Override
    public Object call() throws Exception
    {
        try
        {
            if (contextControl != null)
            {
                contextControl.startContext(RequestScoped.class);
            }
            Object result = invocationContext.proceed();

            if (result instanceof Future<?>)
            {
                return result;
            }
            return null;
        }
        catch (Exception e)
        {
            throw ExceptionUtils.throwAsRuntimeException(e);
        }
        finally
        {
            if (contextControl != null)
            {
                contextControl.stopContext(RequestScoped.class);
            }
        }
    }
}
