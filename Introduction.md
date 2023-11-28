# Introduction
In Redis, master-slave replication is a feature that allows you to create copies (slaves) of a Redis database from a primary instance (master). This replication mechanism is useful for various purposes, including data redundancy, load balancing, and high availability

## Master Node

* The master node is the primary Redis instance that holds the original dataset.
* It accepts write operations and serves as the source of truth for the data.

## Slave Node

* Slave nodes are copies of the master node. They replicate the data from the master.
* Slaves are read-only by default, meaning they can handle read operations but not write operations.

## Replication Process:

* The master node continuously sends a stream of commands to its slave nodes, keeping them updated with the latest changes to the dataset.
* The replication process occurs asynchronously, and slaves might lag behind the master, but they eventually catch up.

## Use Cases:

* High Availability: If the master node fails, one of the slaves can be promoted to a master, ensuring continuity of service.
* Load Balancing:    Distribute read operations among multiple slave nodes, reducing the load on the master node.
* Data Redundancy:   Protect against data loss by having multiple copies of the dataset.
