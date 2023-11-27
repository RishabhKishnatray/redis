# Install Redis Master on Ubuntu server

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
## After installing the redis server, now we are checking the installation by login into the redis-cli
```
sudo systemctl start redis-server
redis-cli ping
```
## Configuration
The Redis configuration file is usually located at "/etc/redis/redis.conf" on Linux systems. You can edit this file to modify various settings, such as port, binding address, and more.
