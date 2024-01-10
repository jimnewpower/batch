# Batch
This document provides options for batch processing implementations in Java. I have evaluated 3 options: homegrown, easy batch, and spring batch. Each option has a demo application that reads a CSV file and writes the data to the console. The demo applications are implemented as spring boot console applications.

## [Homegrown](https://github.com/jimnewpower/batch/tree/main/homegrown)
Simplest solution is to write your own batch processing code. This is a good option if you have a small number of batch jobs and you don't need to scale. A LinkedBlockingQueue can be used to queue the work, and a thread pool can be used to process the work. 

[Demo application](https://github.com/jimnewpower/batch/tree/main/homegrown) is a spring boot console application.

Pros:
* Simple
* No dependencies
* Total control of implementation

Cons:
* Must implement and maintain everything yourself (error handling, monitoring, start/stop, etc.)
* Error-prone
* Time-consuming

## [Easy Batch](https://github.com/j-easy/easy-batch)
Easy Batch is a 3rd party framework for batch processing. It provides a simple programming model that includes a JobBuilder, JobExecutor, and JobReport.

[Demo application](https://github.com/jimnewpower/batch/tree/main/easy) is a spring boot console application.

Pros:
* Open source, and available on Maven Central
* Simple implementation
* Multiple contributors
* [Good documentation](https://github.com/j-easy/easy-batch/wiki/getting-started)

Cons:
* Project is in maintenance mode: no new features, only bug fixes
* Limited community thus limited support

## [Spring Batch](https://spring.io/projects/spring-batch)
Spring Batch is a robust framework for development of batch applications. Optimized for speed and scalability. 

Pros:
* Robust
* Full featured (error handling, monitoring, start/stop, etc.)
* Well documented
* Active community
* Spring Boot integration

Cons:
* More complex
* Steeper learning curve

## Demo Data
Demo data is a CSV file with 3600 records. Each row contains two UUIDs: the first id represents a user id, while the second id represents a transaction id. The same user id may appear multiple times in the file, while each transaction id is unique. The idea here is that you can use the user id to lookup the user in a database, and then use the transaction id to lookup the transaction in a database.



