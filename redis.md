# Install Redis Master on Linux

## Introduction

 Redis is a NoSQL open-source, in-memory data structure server that can be used as a database, cache, or message broker. It is often used for caching web pages and reducing the load on servers

## Prerequisites

### Minimum Requirements
| Requirement           | Minimum Recommendation | 
|  ----------           |           ---          |
| **RAM:**              |    2 GB or Higher      |
| **Disk Space (ROM):** |  08 Gigabyte or Higher |
| **OS Supported:**     |     Ubuntu 20.04       |

### Important Ports
| Port Number           | Description            | 
|  ----------           |           ---          |
|  **6379**            | Default port for Redis |

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
## Configuration
The Redis configuration file is usually located at /etc/redis/redis.conf on Linux systems. You can edit this file to modify various settings, such as port and binding address and more.
```
sudo nano /etc/redis/redis.conf
```
Locate the line “bind 127.0.0.1 ::1”
Change the IP address by entering the values of the connections you want the Redis server to listen for
```
bind 172.16.1.2
```
In order to add multiple IP addresses, simply separate the IP addresses with a space like:
```
bind 172.16.1.4 172.16.1.6
```
However, if you want the server to listen to all the interfaces on the network, you can comment out the bind line entirely

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
