# CDI Async (@Async)

`@Async` is similar (but not the same as `javax.ejb.Asynchronous`).
It can be used for CDI beans — there is no need to use EJBs.

## Supported Use-Cases

### Class-level annotation (all methods async)

```java
@Async
@ApplicationScoped
public class MyAsyncService {

    public void processIt1(/* ... */) {
        // runs asynchronously
    }

    public Future<Result> processIt2(/* ... */) {
        // runs asynchronously, result available via Future
    }
}
```

### Method-level annotation

```java
@ApplicationScoped
public class MyAsyncService {

    @Async
    public void processIt1(/* ... */) {
        // ...
    }

    @Async
    public Future<Result> processIt2(/* ... */) {
        // ...
    }
}
```

### Asynchronous observers (OWB 1.2+)

```java
@ApplicationScoped
public class MyObserver {

    @Async
    public void observeAsync(@Observes MyEvent myEvent) {
        // handled asynchronously
    }
}
```

## Request Scope Support

Every method annotated with `@Async` will get its own request scope **if** you add
`deltaspike-cdictrl-owb` to the classpath. Without an implementation of the
cdictrl-api, you can still use all scopes which get started automatically
(like `@ApplicationScoped`, `@TransactionScoped`, etc.).

## Requirements

- Java 25+
- Maven 3.6.3+
- CDI 4.1 (Jakarta EE 11)
- Apache DeltaSpike 2.x

## Building

```bash
mvn clean verify
```

## Quality Plugins

The build enforces the following quality gates:

- **Compiler**: `-Xlint:all`, fail on warnings
- **Enforcer**: Java 25+, Maven 3.6.3+, dependency convergence, no javax.* dependencies
- **Checkstyle**: no star imports, brace enforcement, whitespace rules
- **Apache RAT**: Apache 2.0 license header verification
- **JaCoCo**: code coverage reporting
- **Surefire**: JUnit Jupiter test execution

## Testing

Tests use the [Dynamic CDI Test Bean Addon](https://github.com/os890/dynamic-cdi-test-bean-addon)
with `@EnableTestBeans` for CDI SE container management and Apache OpenWebBeans as the CDI implementation.

## Compatibility

This add-on was tested with Apache OpenWebBeans as well as with TomEE.
This add-on isn't compatible with Weld, because proxies are handled differently with Weld.

## License

[Apache License, Version 2.0](LICENSE)
