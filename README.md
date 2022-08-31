# Spring Boot Kotlin with Debezium and Kafka

---

This is an example project that uses Debezium and captures the data changes in the db and then sends those changes to the specific Kafka topic.

### How to run

* Firstly, clone the project your local.
* After run `docker-compose up` command to be able to have `postgres` and `Kafka` applications locally.
* Then run the project using `mvn spring-boot:run`
* Once everything is up and running, check the `http://localhost:8080` to be able to see Kafka topics.
* After that you can try to enter `postgres` container then `cli` using below commands.
```
docker exec -it postgres_new bash
psql -U user colleaguedb
```
* Once you entered the `postgres` cli then create the table.
```sql
CREATE TABLE public.colleague
(
  id integer NOT NULL,
  email character varying(255),
  name character varying(255),
  CONSTRAINT colleague_pkey PRIMARY KEY (id)
);
```
* And finally, you can add or update or delete the records to that table and you will be able to see in the logs and also on `Kafka-UI`, the changes are captured.
```sql
INSERT INTO colleague(ID, NAME, EMAIL) VALUES('1','ufuk','ufukhalis@gmail.com');
UPDATE colleague SET EMAIL='ufuk@gmail.com', NAME='halis' WHERE ID = 1;
DELETE FROM colleague WHERE ID = 1;
```
