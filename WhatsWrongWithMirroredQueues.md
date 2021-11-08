# What is wrong with Mirrored Queues

## 1. HA queue synchronization

### 1.1. How it works

- There is a single leader queue and one or more mirror queues
- All reads and writes go through the leader queue, and the leader then replicates all the commands (write, read, ack,
  nack etc) to the mirrors
- Once all the live mirrors have the message, the leader will send a confirmation to the publisher. At this point, if
  the leader failed, a mirror would get promoted to leader, and the queue would remain available, with no data loss.

### 1.2. What's wrong with the model ?

* **When a broker goes offline and comes back again, any data it had in mirrors gets discarded**
  Now that the mirror is back online but empty, the administrators have a decision to make - to synchronize the mirror
  or not. `Synchronize` means replicate the current messages from the leader to the mirror.

* **Synchronization is blocking**  causing the whole queue to become unavailable. When a queue is large, the impact is
  much greater. Synchronization has been known to cause memory related issues on the cluster sometimes even causing
  synchronization to get stuck requiring reboot. During the sync, the queue is unavailable. No new messages are
  processed or accepted until the sync is complete.

## 2. Quorum Queues

- **The issues of synchronization are gone**. When brokers come back online, they do not discard their data. All
  messages remain on disk, and the leader simply replicates messages from where it left off.
- **Replication of messages to a returning follower is non-blocking**. So queues do not get so impacted by new followers
  or rejoining followers. The only impact can be network utilization.