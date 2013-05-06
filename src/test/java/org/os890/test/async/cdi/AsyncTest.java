package org.os890.test.async.cdi;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.os890.async.cdi.api.Async;
import org.os890.async.cdi.impl.AsyncInterceptor;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class AsyncTest
{
    @Deployment
    public static WebArchive createTestArchive()
    {
        Asset beansXml = new StringAsset("<beans><interceptors><class>org.os890.async.cdi.impl.AsyncInterceptor</class></interceptors></beans>");
        return ShrinkWrap.create(WebArchive.class, "async-cdi-test.war")
                .addAsWebInfResource(beansXml, "beans.xml")
                .addPackage(AsyncTestEvent.class.getPackage())

                .addAsLibraries(ShrinkWrap.create(JavaArchive.class, "async-cdi-lib.jar")
                        .addPackages(true, Async.class.getPackage())
                        .addPackages(true, AsyncInterceptor.class.getPackage()));
    }

    @Inject
    private TestService1 testService1;

    @Inject
    private TestService2 testService2;

    @Inject
    private TestObserver testObserver;

    @Inject
    private Event<AsyncTestEvent> asyncEvent;

    @Test
    public void testAsyncService1()
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
    public void testAsyncService2()
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
    @Ignore //works with owb 1.2+ and versions of tomee which are based on it
    //owb 1.1.x uses Method#isAccessible instead of Modifier.isPublic(method.getModifiers()) to check if the (interceptor-)proxy should be used
    public void testAsyncEvent()
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
