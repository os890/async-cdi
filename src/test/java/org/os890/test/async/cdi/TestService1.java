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
package org.os890.test.async.cdi;

import org.os890.async.cdi.api.Async;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.CountDownLatch;

@ApplicationScoped
public class TestService1
{
    static final CountDownLatch TEST_LATCH = new CountDownLatch(1);

    private boolean called = false;

    @Async
    public void call()
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

    public boolean isCalled()
    {
        return called;
    }
}
