# Quorum Queues Internals - RabbitMQ

- New Replicated queue type in `RabbitMQ 3.8`
- Raft consensus algorithm

## 1. consensus algorithm

- Agreement on a value in a distributed system
- In Raft this "value" is a log of operations
    - Log Replication

### 1.1. A log of Quorum queue operations

```
1. PUBLISH(M1) ==> 2. PUBLISH(M2) ==> 3. CONSUME ==> 4. ACK(M1) ==> 5. PUBLISH(M3) ==> 6. ACK(M2)
```

- Monotonically indexed log
- Includes all operations that change the queue state (Not just messages/events)
- Includes message bodies
    - Quorum queues do not use the RabbitMQ message store

**So, it's all about the log**

- The log is replicated across multiple nodes
- Is persisted to disk
- Is "agreed on"(consensus)
- Used to calculate the current queue state
- No operations are evaluated until they are safe
    - Written to a quorum (majority) of nodes
    - NB:fsync! (flushed to underlying disk)

### 2. Log Truncation

- Without it the log would grow and exhaust the disk
- The current state can be snapshotted and put on disk instead of prefix of log entries
    - Truncation is more efficient than incremental "log cleaning"
- But the state contains message bodies! Potentially millions!
    - Not efficient
        - Write amplification
    - How can we snapshot without (re)writing message bodies to disk?
    - Quorum queues are FIFO(ish) queues

### 3. Potential problems

- Consumers that continuously rejects messages
    - violating FIFO properties
    - _**Don't do this**_
    - Configure poison message handling - put a limit on no of retries
- Consumers that never acks and never disconnects
    - configure `consumer_timeout`
  







