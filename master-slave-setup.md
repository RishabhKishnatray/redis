# Install Redis on Linux

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


## Redis Master Setup
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
## Check Redis Version
```
redis-server --version
```
## After installing the redis, Now you can check the redis installation by login into the redis-cli
```
sudo systemctl start redis-server
redis-cli ping
```

## Redis Master Configuration

TO configure redis master you need to edit Redis configuration file and it is usually located at "/etc/redis/redis.conf" on Linux systems. You can edit this file to modify various settings, such as port and binding address and more.
```
sudo nano /etc/redis/redis.conf
```
Locate the line “bind 127.0.0.1 ::1”
Change the entering IP address by the values of the connections you want the Redis server to listen for
```
bind 172.16.1.2
```

Once you are done making changes, save and close the file. Then restart the Redis service to apply the changes:
```
sudo systemctl restart redis-server
```

***
***

# Setup Master-slave
After the installation of redis in all the servers you should install sentinel in all the servers to configure master-slave
## Install Sentinel
Install Redis Sentinel using below commands:
```
sudo apt install redis-sentinel
sudo systemctl enable redis-sentinel
```
## Redis Replication Configuration
After installing the redis now we are configuring the master and slave server as follows:

## Configuring Master Server
To configure the master server , opening the redis configuration file "/etc/redis/redis.conf" then change the below parameter value in the configuration file.
```
protected-mode no              
bind <Master_IP>
```
After changing the parameter values in the configuration file now we need to restart the redis server as follows.
```
sudo systemctl restart redis-server.service
```
## Configuring Slave Server
To configure the slave server, opening the redis configuration file "/etc/redis/redis.conf"  then change the below parameter value in the configuration file.
```
protected-mode no
bind <Slave_Ip>
replicaof <masterip> <masterport>
```
After changing the parameter values in the configuration file now we need to restart the redis server as follows.
```
sudo systemctl restart redis-server.service
```
## Configure Redis Sentinel
This section demonstrates how to configure the `sentinel.conf` file to monitor the master server. On both servers, add this configuration file at the following path: `/etc/redis/sentinel.conf`.
```
sudo nano /etc/redis/sentinel.conf
sentinel monitor mymaster <MASTER_NODE_IP> 6379 quorum
sentinel down-after-milliseconds mymaster 30000           
sentinel failover-timeout mymaster 60000
sentinel parallel-syncs mymaster 1
```
## Check the Redis Setup
Retrieve information about the replication configuration and status of a Redis server. When you run the following command:
```
info replication
```
## To run the command redis-server /etc/redis/sentinel.conf --sentinel on both servers to monitor the master Redis server.
```
redis-server /etc/redis/sentinel.conf --sentinel
```
