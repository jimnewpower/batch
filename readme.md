# Batch
This document provides options for batch processing implementations in Java. I have evaluated 3 options: homegrown, easy batch, and spring batch. Each option has a demo application that reads a CSV file and writes the data to the console. The demo applications are implemented as spring boot console applications.

Key characteristics of a batch processing system include:
* Not required to run in real-time (can be scheduled to run overnight, for instance)
* A volume of data is processed (not a single record)
* Does not require human input or user interaction

## Requirements
The batch processor should be a completely automated, end-of-cycle process that runs once a month.

Must-haves
* Exactly once processing (idempotency)
* Accurate
* Reliable
* Error handling
* Apply business rules
* Reporting

Nice-to-haves
* Scalable
* High performance
* Monitoring
* Start/stop
* Retry

Architectural Characteristics
* Auditability
* Data integrity
* Security
* Performance

Logical Components
* Scheduler (+configuration)
* Job Runner (+configuration)
* Job Launcher
* Job
* Job Repository
* Step
* Data Access
* Processing Units (ItemReader, ItemProcessor, ItemWriter)
* Business Logic

## The Batch Job
A named job that defines the steps to be executed. A job can be executed multiple times, where each execution is a job instance. Jobs may be stoppable and restartable. A job instance must be provided a set of parameters which define its contract. The execution of a job should update the status of a job instance, e.g. STARTED, COMPLETED, FAILED, as well as timestamps for start and end times, and an exit status; all of these execution properties should be persisted to a job repository.

Steps
Steps represent independent, sequential phases of a job (chain of responsibility pattern). Steps allow for data loading, applying business rules, transforming or mapping the data (processing), and writing the data or executing an external API call.

## [Homegrown](https://github.com/jimnewpower/batch/tree/main/homegrown)
Simplest solution is to write your own batch processing code. This is a good option if you have a small number of batch jobs and you don't need to scale. A LinkedBlockingQueue can be used to queue the work, and a thread pool can be used to process the work. 

[Demo application](https://github.com/jimnewpower/batch/tree/main/homegrown) is a spring boot console application.

Pros:
* No dependencies
* Low overhead, nothing hidden
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
* Dependencies
* 3rd Party libraries can be prone to vulnerabilities

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
Demo data is a CSV file with 3600 records. Each row contains two UUIDs: the first id represents a user id, while the second id represents a transaction id. The same user id may appear multiple times in the file, while each transaction id is unique. The idea here is that you can use the user id to lookup the user information (name, etc.) in a database, and then use the transaction id to lookup the transaction information (vendor, amount, timestamp, etc.) in a database.



