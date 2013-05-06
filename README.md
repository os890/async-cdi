@Async is similar (but not the same as javax.ejb.Asynchronous)

It can be used for CDI beans (-> there is no need to use EJBs).

Supported use-cases:

    @Async
    //...
    public class MyAsyncService
    {
        public void processIt1(/*...*/)
        {
            //...
        }

        public java.util.concurrent.Future processIt2(/*...*/)
        {
            //...
        }
    }

    //or

    //...
    public class MyAsyncService
    {
        @Async
        public void processIt1(/*...*/)
        {
            //...
        }

        @Async
        public java.util.concurrent.Future processIt2(/*...*/)
        {
            //...
        }
    }

With OWB 1.2+ @Async is also possible for asynchronous observers:

    //...
    public class MyObserver
    {
        @Async
        public void observeAsync(@Observes MyEvent myEvent)
        {
        }
    }

Every method annotated with @Async will get it's own request-scope ( **if** you add deltaspike-cdictrl-owb).
Without an implementation of the cdictrl-api, you can still use all scopes which get started autom. (like @ApplicationScoped, @TransactionScoped,...).

This add-on was tested with Apache OpenWebBeans as well as with TomEE.
This add-on isn't compatible with Weld, because proxies are handled differently with Weld.