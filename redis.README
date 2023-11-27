# Install Redis on Ubuntu server

## Install dependency to compile redis
```
sudo apt-get install build-essential
sudo apt-get install tcl8.5
```

## Download and install

Download latest version via wget
```
wget http://download.redis.io/releases/redis-3.0.1.tar.gz
```

Untar the downloaded package
```
tar xzf redis-3.0.1.tar.gz
```

Change directory downloaded folder
```
cd redis-3.0.1
```

Then compile and install 
```
make
make test
sudo make install
```

## Install redis server globally
```
cd utils
sudo ./install_server.sh
```

Setup redis to automatically running on boot
```
sudo update-rc.d redis_6379 defaults
```

## Test installation

Run on console
```
redis-cli
```

you will see redis console like this
```
127.0.0.1:6379>
```
