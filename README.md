[Resilience4j Integration Summary]

To implement Resilience4j in Spring Boot 4 (see source_app):

1. **Add Dependencies**: Include the following in your pom.xml:
	- `spring-cloud-starter-circuitbreaker-resilience4j`
	- `spring-boot-starter-aspectj`
	- (other Spring Boot and test dependencies as needed)

```xml
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-circuitbreaker-resilience4j</artifactId>
</dependency>
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-aspectj</artifactId>
</dependency>
```

2. **Retry Logging Listener**: The `RetryLoggingListener` component is used for logging retry attempts. It registers a listener on the retry registry for the `jokesService` retry, logging each retry attempt and its cause.

```java
@Component
@RequiredArgsConstructor
@Slf4j
public class RetryLoggingListener {
	private final RetryRegistry retryRegistry;

	@PostConstruct
	public void registryRetryLogging() {
		retryRegistry.retry("jokesService").getEventPublisher().onRetry(event ->
			log.warn("Retry attempt {} for {} due to {}",
				event.getNumberOfRetryAttempts(),
				event.getName(),
				event.getLastThrowable() != null ? event.getLastThrowable().getMessage() : null));
	}
}
```

3. **Retry and Fallback**: In `JokesService`, the `getJoke()` method is annotated with `@Retry` and `@CircuitBreaker`. The fallback method must have the signature `public Joke fallback(Throwable throwable)` to be compatible with Resilience4j's retry mechanism. This method is invoked after all retry attempts fail.

```java
@Service
@RequiredArgsConstructor
@Slf4j
public class JokesService {
	private final RestTemplate restTemplate;
	@Value("${jokes.api.url}")
	private String apiUrl;

	@CircuitBreaker(name = "jokesService")
	@Retry(name = "jokesService", fallbackMethod = "fallback")
	public Joke getJoke() {
		log.debug("fetch joke from {}", apiUrl);
		return restTemplate.getForObject(apiUrl, Joke.class);
	}

	// Fallback method signature
	public Joke fallback(Throwable throwable) {
		throw new RuntimeException("3 retries done but got: " + throwable.getMessage());
	}
}
```

4. **Controller Handling**: The controller (`JokesController`) catches runtime exceptions from the service and returns a `SERVICE_UNAVAILABLE` response if the downstream service is unavailable.

```java
@RestController
@RequestMapping("/jokes")
@RequiredArgsConstructor
public class JokesController {
	private final JokesService jokesService;

	@GetMapping
	public ResponseEntity<Object> getJokes() {
		try {
			var response = jokesService.getJoke();
			return ResponseEntity.ok(response);
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("A downstream service is unavailable.");
		}
	}
}
```

**Note:** Spring Boot 4 integrates Resilience4j natively, so annotations like `@Retry` and `@CircuitBreaker` are supported out of the box.

---
### Using Native Spring Boot `@Retryable`

Spring Boot 4 provides a native `@Retryable` annotation for retry logic, which can be used without Resilience4j. No extra dependency is required—just annotate your method:

Example usage:
```java
import org.springframework.retry.annotation.Retryable;

@Retryable(
	value = { ResourceAccessException.class, ConnectException.class },
	maxAttempts = 4,
  delay = 1000,
  multiplier = 2 /*for exponential backoff*/
)
public Joke getJoke() {
	// ...
}
```

This approach is useful for simple retry scenarios and does not require Resilience4j or any additional dependencies.

It's possible to check in which state the Circuit Breaker is by visiting http://localhost:8080/actuator/circuitbreakers