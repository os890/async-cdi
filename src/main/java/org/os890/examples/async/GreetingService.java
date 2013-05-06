/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
*/
package org.os890.examples.async;

import org.os890.async.cdi.api.Async;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;

@ApplicationScoped
public class GreetingService
{
    //just used for the demo
    private String calculatedResult;

    //since MyRequestScopedBean is used in an async call here, you will see an instance created for this async call
    //@Async starts (and afterwards stops) an own request-scope for every method annotated with it
    @Inject
    private MyRequestScopedBean requestScopedBean;

    @Async
    //it's also possible to return java.util.concurrent.Future
    public void createGreeting(String name)
    {
        requestScopedBean.increase();

        try
        {
            Thread.sleep(5000);
        }
        catch (InterruptedException e)
        {
            throw new IllegalStateException(e);
        }

        this.calculatedResult = "Hello " + name + ". Enjoy @Async CDI beans!";
    }

    public String getCalculatedResult()
    {
        return calculatedResult;
    }

    @Async //only works with owb 1.2.x+ due to a bug which is in owb 1.1.8 (maybe even in 1.1.x)
    public void observeAsync(@Observes CustomEvent customEvent) {
        requestScopedBean.increase();

        try
        {
            Thread.sleep(10000);
        }
        catch (InterruptedException e)
        {
            throw new IllegalStateException(e);
        }
        this.calculatedResult = "Enjoy @Async CDI event-observers!";
    }
}
