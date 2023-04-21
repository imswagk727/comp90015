# COMP90015 Distributed system
Course resources COMP90015 - Distributed System - 2023 Semester 1

## How to run assignment 1 - multi-threaded-Dictionary-server

1. Change working directory to file path  
```bash
cd replace_to_your_folder_path/comp90015/asgmt1/submit
```
2. Run server  
```bash
java –jar DictionaryServer.jar <port> <file_name.txt>

# example :
java -jar DictionaryServer.jar 8888 dictionary.txt
```
3. Run client  
```bash
java –jar DictionaryClient.jar <server-address> <server-port>

# example :
java -jar DictionaryClient.jar localhost 8888
```

<img src="https://raw.githubusercontent.com/imswagk727/comp90015/main/asgmt1/demo_picture/server.png" style="width: 60%">
<img src="https://raw.githubusercontent.com/imswagk727/comp90015/main/asgmt1/demo_picture/client.png" style="width: 70%">
