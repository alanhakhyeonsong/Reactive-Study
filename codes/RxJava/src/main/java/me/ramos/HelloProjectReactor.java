package me.ramos;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class HelloProjectReactor {

    public static void main(String[] args) throws InterruptedException {
        Flux<String> flux = Flux.just("Hello", "Project", "Reactor", "by", "Ramos")
                .map(data -> {
                    System.out.println("Mapping on thread: " + Thread.currentThread().getName());
                    return data.toUpperCase();
                })
                .filter(data -> {
                    System.out.println("Filtering on thread: " + Thread.currentThread().getName());
                    return !data.contains("P");
                })
                .subscribeOn(Schedulers.boundedElastic())  // 구독 및 데이터 생산은 별도의 스레드에서 실행
                .publishOn(Schedulers.parallel());         // 이후 연산은 병렬 스케줄러에서 실행

        flux.subscribe(data ->
                System.out.println("Received on thread: " + Thread.currentThread().getName() + " - " + data)
        );

        // 비동기 처리가 완료될 때까지 메인 스레드를 잠시 대기
        Thread.sleep(1000);
    }
}
