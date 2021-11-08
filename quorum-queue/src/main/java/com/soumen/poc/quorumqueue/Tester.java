package com.soumen.poc.quorumqueue;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * @author Soumen Karmakar
 * @Date 08/11/2021
 */
@Component
@Log4j2
@RequiredArgsConstructor
public class Tester implements CommandLineRunner {

    private final Producer producer;

    @Override
    public void run(String... args) throws Exception {
        log.info("------ Sending Random Messages -------");
        for (int i = 0; i < 1000; i++) {
            String msg = "Hello - @ - " + Instant.now().toString();
            log.info("PUBLISH - {}", msg);
            producer.send(msg);
            Thread.sleep(1000);
        }

    }
}
