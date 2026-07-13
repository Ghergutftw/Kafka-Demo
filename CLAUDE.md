# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Overview

Spring Boot 3.5 / Java 21 demo project exploring Kafka producers and consumers, including a
reactive WebFlux client that streams Wikipedia's recent-changes feed into a Kafka topic. The
actual Maven project lives in the `kafka-demo/` subdirectory (the repo root only holds the
README and IDE metadata) — run all commands from `kafka-demo/`.

## Commands

Run from the `kafka-demo/` directory:

- Start Kafka (required before running the app): `docker compose up -d`
- Build: `./mvnw clean install` (`mvnw.cmd` on native Windows shells)
- Run the app: `./mvnw spring-boot:run`
- Run all tests: `./mvnw test`
- Run a single test class: `./mvnw test -Dtest=KafkaDemoApplicationTests`

Kafka runs as a single KRaft (no ZooKeeper) broker on `localhost:9092`. The `PLAINTEXT` listener
advertises `localhost` rather than the in-network `kafka` hostname specifically so the app (which
connects from the host, not from inside the docker network) can resolve it — see the comments in
`docker-compose.yml` before changing listener config.

## Architecture

Base package is `app` (note: the test class lives under `com.kafka_demo`, a leftover mismatch from
scaffolding — not indicative of the real package layout).

- `app/config` — Kafka topic declarations (`KafkaTopicConfig` → `my-topic`, `WifimediaTopicConfig`
  → `wikimedia-updates`) and `WebClientConfig`, a WebClient pointed at the public Wikimedia
  EventStreams API (`https://stream.wikimedia.org/v2`).
- `app/producer` — `KafkaProducer` sends raw strings to `my-topic`; `KafkaJsonProducer` sends a
  `Package` DTO as JSON to the same topic; `WikimediaProducer` subscribes to the Wikimedia SSE
  recent-changes stream via WebClient and republishes each event onto `wikimedia-updates`.
  `WikimediaProducer` previously self-injected itself as a workaround for the reactive callback;
  that caused a circular-dependency startup failure (self-injection cycle) and was removed — the
  subscription now just calls `sendMessage` via `this`, which is fine since the method carries no
  proxy-requiring annotations (`@Transactional`, `@Async`, etc.).
  `KafkaJsonProducer`'s `KafkaTemplate<String, Package>` requires the producer serializer in
  `application.yml` to be swapped to `JsonSerializer` (currently commented out in favor of
  `StringSerializer`) or sends will fail type checks.
- `app/consumer` — `KafkaConsumer` and `WikiMediaConsumer` are `@KafkaListener`s that simply log
  incoming messages for `my-topic` and `wikimedia-updates` respectively, both under consumer group
  `my-kafka-group`.
- `app/controller/MessageController` — REST surface under `/api/v1/messages`:
  - `POST /send` — raw string through `KafkaProducer`
  - `POST /send-json` — `Package` JSON through `KafkaJsonProducer`
  - `POST /see-update` — kicks off the Wikimedia stream subscription (fire-and-forget; the
    response returns immediately while messages continue arriving asynchronously via the consumer
    log)
- `app/dto/Package` — the one JSON payload shape used across the JSON producer/consumer path.

`application.yml` has consumer/producer JSON (de)serializer config commented out alongside the
active `String(De)Serializer` config — when switching a producer/consumer pair to JSON, both
sides' properties need uncommenting together, not just one.
