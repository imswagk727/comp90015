# COMP90015 Distributed system
Course resources COMP90015 - Distributed System - 2023 Semester 1

|  Part |           Lecture           |      Tutorial     |
|-------|-----------------------------|-------------------|
| Part 1| L0 &1  Admin & intro        | w2  tute 1        |
|       | L2 Sockets                  | w3  tute2         |
|       | L3 Threads                  | w4  tute3         |
| Part 2| L4 DS Models                | w5  tute4         |
|       | L5 OS DS                    | w6  tute5         |
|       |                             | w7  tute6         |
|       | L6 RMI Programming          | w8  tute7         |
| Part 3| L7 Security                 | w9  tute8         |
|       |                             | w10 tute9         |
|       | L8 Distributed File Systems | w11 tute10        |
|       | L10 Naming service          | w12 tute11        |

refer to this link: [COMP90015](http://clouds.cis.unimelb.edu.au/652/LectureSlides.html)



#### How to run assignment 1 - multi-threaded-Dictionary-server

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
Server             |  Client
:-------------------------:|:-------------------------:
<img src="https://raw.githubusercontent.com/imswagk727/comp90015/main/asgmt1/demo_picture/server.png" style="width: 100%">  |  <img src="https://raw.githubusercontent.com/imswagk727/comp90015/main/asgmt1/demo_picture/client.png" style="width: 100%">
