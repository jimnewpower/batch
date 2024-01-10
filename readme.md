# Batch
This document provides options for batch processing implementations in Java. 

## [Homegrown](https://github.com/jimnewpower/batch/tree/main/homegrown)
Simplest solution is to write your own batch processing code. This is a good option if you have a small number of batch jobs and you don't need to scale. A LinkedBlockingQueue can be used to queue the work, and a thread pool can be used to process the work. 

Pros:
* Simple
* No dependencies

Cons:
* Must implement and maintain everything yourself (error handling, monitoring, start/stop, etc.)

## [Easy Batch](https://github.com/j-easy/easy-batch)
Easy Batch is a 3rd party framework for batch processing. It provides a simple programming model that includes a JobBuilder, JobExecutor, and JobReport.

Pros:
* Simple implementation
* Multiple contributors
* [Good documentation](https://github.com/j-easy/easy-batch/wiki/getting-started)

Cons:
* No recent updates (last update was 2020)
* Limited community thus limited support

## [Spring Batch](https://spring.io/projects/spring-batch)
Spring Batch is a robust framework for development of batch applications. Optimized for speed and scalability. 

Pros:
* Robust
* Well documented
* Active community
* Spring Boot integration

Cons:
* More complex

## Apache Flink

## Apache Kafka

