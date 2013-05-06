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

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;

@Named("helloWorld")
@RequestScoped
public class HelloWorldController
{
    private String name;

    @Inject
    private GreetingService greetingService;

    @Inject
    private Event<CustomEvent> asyncEvent;

    @Inject
    private MyRequestScopedBean requestScopedBean;

    public String send()
    {
        requestScopedBean.increase();
        greetingService.createGreeting(name);
        asyncEvent.fire(new CustomEvent());

        return "page2.xhtml";
    }

    public String getGreeting()
    {
        return greetingService.getCalculatedResult(); //to illustrate that the result isn't available immediately
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
