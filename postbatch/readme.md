# Bootstrap
Spring boot project with batch processing and Postgres dependencies.

# Postgres Docker Image
Pull Postgres Docker image. Note the username and password here (these must also be specified in `application.properties`)
```shell
docker run --name my-postgres -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postbatch -d -p 5432:5432 postgres:alpine
docker ps
docker inspect my-postgres
```

OR, use docker compose with local `docker-compose.yml` file:
```shell
docker-compose -f docker-compose.yml up -d
```

Exec into Postgres container:
```shell
docker exec -it my-postgres psql -U postgres
```

From postgres container, add batch execution metadata tables to Postgres:
```shell
CREATE TABLE BATCH_JOB_INSTANCE  (
    JOB_INSTANCE_ID BIGINT  NOT NULL PRIMARY KEY ,
    VERSION BIGINT ,
    JOB_NAME VARCHAR(100) NOT NULL,
    JOB_KEY VARCHAR(32) NOT NULL,
    constraint JOB_INST_UN unique (JOB_NAME, JOB_KEY)
) ;
CREATE TABLE BATCH_JOB_EXECUTION  (
    JOB_EXECUTION_ID BIGINT  NOT NULL PRIMARY KEY ,
    VERSION BIGINT  ,
    JOB_INSTANCE_ID BIGINT NOT NULL,
    CREATE_TIME TIMESTAMP NOT NULL,
    START_TIME TIMESTAMP DEFAULT NULL ,
    END_TIME TIMESTAMP DEFAULT NULL ,
    STATUS VARCHAR(10) ,
    EXIT_CODE VARCHAR(2500) ,
    EXIT_MESSAGE VARCHAR(2500) ,
    LAST_UPDATED TIMESTAMP,
    constraint JOB_INST_EXEC_FK foreign key (JOB_INSTANCE_ID)
    references BATCH_JOB_INSTANCE(JOB_INSTANCE_ID)
) ;
CREATE TABLE BATCH_JOB_EXECUTION_PARAMS  (
    JOB_EXECUTION_ID BIGINT NOT NULL ,
    PARAMETER_NAME VARCHAR(100) NOT NULL ,
    PARAMETER_TYPE VARCHAR(100) NOT NULL ,
    PARAMETER_VALUE VARCHAR(2500) ,
    IDENTIFYING CHAR(1) NOT NULL ,
    constraint JOB_EXEC_PARAMS_FK foreign key (JOB_EXECUTION_ID)
    references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) ;
CREATE TABLE BATCH_STEP_EXECUTION  (
    STEP_EXECUTION_ID BIGINT  NOT NULL PRIMARY KEY ,
    VERSION BIGINT NOT NULL,
    STEP_NAME VARCHAR(100) NOT NULL,
    JOB_EXECUTION_ID BIGINT NOT NULL,
    CREATE_TIME TIMESTAMP NOT NULL,
    START_TIME TIMESTAMP DEFAULT NULL ,
    END_TIME TIMESTAMP DEFAULT NULL ,
    STATUS VARCHAR(10) ,
    COMMIT_COUNT BIGINT ,
    READ_COUNT BIGINT ,
    FILTER_COUNT BIGINT ,
    WRITE_COUNT BIGINT ,
    READ_SKIP_COUNT BIGINT ,
    WRITE_SKIP_COUNT BIGINT ,
    PROCESS_SKIP_COUNT BIGINT ,
    ROLLBACK_COUNT BIGINT ,
    EXIT_CODE VARCHAR(2500) ,
    EXIT_MESSAGE VARCHAR(2500) ,
    LAST_UPDATED TIMESTAMP,
    constraint JOB_EXEC_STEP_FK foreign key (JOB_EXECUTION_ID)
    references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) ;
CREATE TABLE BATCH_STEP_EXECUTION_CONTEXT  (
    STEP_EXECUTION_ID BIGINT NOT NULL PRIMARY KEY,
    SHORT_CONTEXT VARCHAR(2500) NOT NULL,
    SERIALIZED_CONTEXT TEXT ,
    constraint STEP_EXEC_CTX_FK foreign key (STEP_EXECUTION_ID)
    references BATCH_STEP_EXECUTION(STEP_EXECUTION_ID)
) ;
CREATE TABLE BATCH_JOB_EXECUTION_CONTEXT  (
    JOB_EXECUTION_ID BIGINT NOT NULL PRIMARY KEY,
    SHORT_CONTEXT VARCHAR(2500) NOT NULL,
    SERIALIZED_CONTEXT TEXT ,
    constraint JOB_EXEC_CTX_FK foreign key (JOB_EXECUTION_ID)
    references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) ;
CREATE SEQUENCE BATCH_STEP_EXECUTION_SEQ MAXVALUE 9223372036854775807 NO CYCLE;
CREATE SEQUENCE BATCH_JOB_EXECUTION_SEQ MAXVALUE 9223372036854775807 NO CYCLE;
CREATE SEQUENCE BATCH_JOB_SEQ MAXVALUE 9223372036854775807 NO CYCLE;
```

From postgres container, verify tables were created:
```shell
\d
```
This should produce the following output:
```shell
postgres=# \d
                      List of relations
 Schema |             Name             |   Type   |  Owner
--------+------------------------------+----------+----------
 public | batch_job_execution          | table    | postgres
 public | batch_job_execution_context  | table    | postgres
 public | batch_job_execution_params   | table    | postgres
 public | batch_job_execution_seq      | sequence | postgres
 public | batch_job_instance           | table    | postgres
 public | batch_job_seq                | sequence | postgres
 public | batch_step_execution         | table    | postgres
 public | batch_step_execution_context | table    | postgres
 public | batch_step_execution_seq     | sequence | postgres
(9 rows)
```

# Add database connection properties to `application.properties`
With these properties in place, Spring Boot will auto-configure a JDBC DataSource bean in the application context that we can use in our batch application.
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/my-postgres
spring.datasource.username=postgres
spring.datasource.password=postgres369
```

# Launch the job, passing the input file as a parameter:
```shell
mvn clean package -Dmaven.test.skip=true
java -jar target/batch-0.0.1-SNAPSHOT.jar --spring.batch.job.names=job1 inputFile=src/main/resources/billing-2023-01.csv
```

Now inspect the batch metadata in the database:
```shell
docker exec postgres psql -U postgres -c 'select * from BATCH_JOB_INSTANCE;'
```

This should provide similar output to the following:
```shell
 job_instance_id | version |  job_name  |             job_key              
-----------------+---------+------------+----------------------------------
               1 |       0 | BillingJob | d41d8cd98f00b204e9800998ecf8427e
               2 |       0 | BillingJob | c0cb4257f9f2b2fa119bbebfb801772f
```

Now check the batch execution metadata:
```shell
docker exec postgres psql -U postgres -c 'select * from BATCH_JOB_EXECUTION;'
```

This should provide similar output to the following:
```shell
 job_execution_id | version | job_instance_id |        create_time         | start_time | end_time |  status   | exit_code | exit_message |        last_updated        
------------------+---------+-----------------+----------------------------+------------+----------+-----------+-----------+--------------+----------------------------
                1 |       1 |               1 | 2024-01-22 18:26:51.66586  |            |          | COMPLETED | COMPLETED |              | 2024-01-22 18:26:51.681818
                2 |       1 |               2 | 2024-01-22 18:28:13.077419 |            |          | COMPLETED | COMPLETED |              | 2024-01-22 18:28:13.089359 
```

Finally, check the job parameters metadata:
```shell
docker exec postgres psql -U postgres -c 'select * from BATCH_JOB_EXECUTION_PARAMS;'
```

This should provide similar output to the following:
```shell
 job_execution_id | parameter_name |  parameter_type  |            parameter_value             | identifying 
------------------+----------------+------------------+----------------------------------------+-------------
                2 | input.file     | java.lang.String | src/main/resources/billing-2023-01.csv | Y
(1 row)
```

Idempotency is a Spring Batch design choice to prevent the same job being run more than once. Test idempotency by running the job again:
```shell
java -jar target/batch-0.0.1-SNAPSHOT.jar --spring.batch.job.names=job1 inputFile=src/main/resources/billing-2023-01.csv
```

You should receive the following exception:
```shell
java.lang.IllegalStateException: Failed to execute ApplicationRunner
        at org.springframework.boot.SpringApplication.callRunner(SpringApplication.java:763) ~[spring-boot-3.0.5.jar!/:3.0.5]
        ...
Caused by: org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException: 
        A job instance already exists and is complete for parameters={'input.file':'{value=src/main/resources/billing-2023-01.csv, type=class java.lang.String, identifying=true}'}.  
        If you want to run this job again, change the parameters.        
```

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.0.5/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.0.5/maven-plugin/reference/html/#build-image)
* [Spring Batch](https://docs.spring.io/spring-boot/docs/3.0.5/reference/htmlsingle/#howto.batch)

### Guides
The following guides illustrate how to use some features concretely:

* [Creating a Batch Service](https://spring.io/guides/gs/batch-processing/)

