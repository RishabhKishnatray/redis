![image](https://github.com/RishabhKishnatray/redis/assets/89369743/78ae5221-1eab-4d60-bb1c-7f69b590d84b)

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


# Redis Master Setup
For setup Redis master you need to install some dependency on you server

## Download dependency of redis
Download Redis GPG key, which is used for package verification and security.
```
curl -fsSL https://packages.redis.io/gpg | sudo gpg --dearmor -o /usr/share/keyrings/redis-archive-keyring.gpg
```
Add the Redis repository
```
echo "deb [signed-by=/usr/share/keyrings/redis-archive-keyring.gpg] https://packages.redis.io/deb $(lsb_release -cs) main" | sudo tee /etc/apt/sources.list.d/redis.list
```

## Install the Redis package from the repository
```
sudo apt-get update
sudo apt-get install redis -y
```

## After installing the redis, Now you can check the redis installation by login into the redis-cli
```
sudo systemctl start redis-server
redis-cli ping
```

## Check Redis Version
```
redis-server --version
```

## Redis Master Configuration

To configure redis master you need to edit Redis configuration file and it is usually located at "/etc/redis/redis.conf" on Linux systems. You can edit this file to modify various settings, such as port and binding address and more.
```
sudo nano /etc/redis/redis.conf
```
Locate the line “protected-mode” and Change the values of the protected-mode
```
protected-mode no
```
Locate the line “bind 127.0.0.1 ::1” & Change the entering IP address by the values of the connections you want the Redis server to listen for
```              
bind <Master_IP>
```

Once you are done making changes, save and close the file. Then restart the Redis service to apply the changes:
```
sudo systemctl restart redis-server
```

***
# Redis slave Setup
For setup slave you can to perform the same steps like master but the configuration file settings are different 

## Redis Slave Configuration
To configure redis slave you need to edit Redis configuration file and it is usually located at "/etc/redis/redis.conf " on Linux systems. You can edit this file to modify various settings, such as port and binding address and more.
Locate the line “protected-mode” and Change the values of the protected-mode
```
protected-mode no
```
Locate the line “bind 127.0.0.1 ::1” & replicaof 127.0.0.0 6379 and Change the entering IP address by the values of the connections you want the Redis server to listen for
```              
bind <Slave_Ip>
replicaof <masterip> <masterport>
```
After changing the parameter values in the configuration file now we need to restart the redis server as follows.
```
sudo systemctl restart redis-server.service
```
***
# Validation of Redis Data Replication  Write on Master, Read on Slave

Beform perform this you should ensure that Redis master-slave is correctly configured.
Run the below command on both the master and slave servers to check their status:
```
redis-cli ping
```
You should receive a "PONG" response from both servers, indicating that they are running.

## Check Replication Status:
You can check the replication status by using the following command on the slave server:
```
redis-cli info replication
```
Look for the role field in the output. It should be "slave," and the master_host and master_port fields should contain the correct information about the master.

## Write Data to the Master Node
Use a Redis  command-line interface to connect to the Redis master node.

Example command for writing data:
```
redis-cli -h master_ip -p master_port
SET key value
```

## Read Data from the Slave Node
Connect to the Redis slave node using a Redis  command-line interface.

Example command for reading data:
```
redis-cli -h slave_ip -p slave_port
GET key
```

Confirm that the data read from the slave node matches the data written to the master. The value retrieved from the slave should be the same as the one written to the master.
***
# Manual Failover in Redis: Promoting a Slave to Master
Perform a manual failover in Redis by stopping the original master node, promoting a slave node to become the new master

Stop the Redis Master Node
```
redis-cli -h master_ip -p master_port SHUTDOWN
```
## Verify New Master Status
Check the status of the new master node to ensure it has taken over:
```
redis-cli -h <new_master_ip> -p <new_master_port>
INFO replication
```
## Reconfigure the Original Master as a Slave
Update the configuration of the original master to replicate from the new master
```
redis-cli -h <original_master_ip> -p <original_master_port>
SLAVEOF <new_master_ip> <new_master_port>
```

## Verify Replication Status
Check the replication status to ensure that the original master is successfully replicating from the new master  
```
redis-cli -h <original_master_ip> -p <original_master_port>
INFO replication
```
*** 
# Conclusion
While manual failover is a straightforward process, it might not be the most efficient or automated solution, especially in a production environment where high availability is crucial. Redis Sentinel is specifically designed to address this need by providing automated monitoring and failover capabilities

## Install Sentinel 
Install Redis Sentinel on all redis servers using below commands:
```
sudo apt install redis-sentinel
sudo systemctl enable redis-sentinel
```

## Configure Redis Sentinel
This section demonstrates how to configure the "/etc/redis/sentinel.conf" file to monitor the master server. On both servers, add this configuration file at the following path: `/etc/redis/sentinel.conf`.
```
sudo nano /etc/redis/sentinel.conf
sentinel monitor mymaster <MASTER_NODE_IP> 6379 <quorum>
sentinel down-after-milliseconds mymaster 30000           
sentinel failover-timeout mymaster 60000
sentinel parallel-syncs mymaster 1
```
## Check the Redis Setup
Retrieve information about the replication configuration and status of a Redis server. When you run the following command:
```
redis-cli
info replication
```
## To run the command redis-server /etc/redis/sentinel.conf --sentinel on both servers to monitor the master Redis server.
```
redis-server /etc/redis/sentinel.conf --sentinel
```
