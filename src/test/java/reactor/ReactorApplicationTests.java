package reactor;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

@SpringBootTest
class ReactorApplicationTests {

	@Test
	void contextLoads() {
	}
	
	@Test
	void flux_just() {
		Flux<String> fruitFlux = Flux
				.just("Apple", "Orange", "Grape", "Banana", "Strawberry");
		fruitFlux.subscribe(System.out::println);
		
		StepVerifier.create(fruitFlux)
				.expectNext("Apple")
				.expectNext("Orange")
				.expectNext("Grape")
				.expectNext("Banana")
				.expectNext("Strawberry")
				.expectComplete()
				.verify();
		
	}
	
	@Test
	void flux_interval() {
		Flux<Long> flux = Flux.interval(Duration.ofSeconds(1)).take(3);
		StepVerifier.create(flux)
				.expectNext(0L)
				.expectNext(1L)
				.expectNext(2L)
				.expectComplete()
				.verify();
	}
	
	@Test
	void flux_merge() {
		Flux<String> flux1 = Flux.just("t", "t1")
				.delayElements(Duration.ofMillis(500));
		Flux<Integer> flux2 = Flux.just(1, 2)
				.delaySubscription(Duration.ofMillis(250))
				.delayElements(Duration.ofMillis(500));
		Flux<Comparable> merged = Flux.merge(flux1, flux2);
		StepVerifier.create(merged)
				.expectNext("t")
				.expectNext(1)
				.expectNext("t1")
				.expectNext(2)
				.expectComplete()
				.verify();
	}
	
	@Test
	void flux_zip() {
		Flux<String> flux1 = Flux.just("t", "t1");
		Flux<Integer> flux2 = Flux.just(1, 2);
		Flux<Tuple2<String, Integer>> zipped = Flux.zip(flux1, flux2);
		StepVerifier.create(zipped)
			.expectNextMatches(p -> p.getT1().equals("t") && p.getT2() == 1)
			.expectNextMatches(p -> p.getT1().equals("t1") && p.getT2() == 2)
			.verifyComplete();
	}
}
