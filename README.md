## COMP90015 Distributed system
### Course resources COMP90015 - Distributed System - 2023 Semester 1

|  Part |           Lecture           |      Tutorial     |
|-------|-----------------------------|-------------------|
| Part 1| L0 &1  Admin & intro        | week02  tute1     |
|       | L2 Sockets                  | week03  tute2     |
|       | L3 Threads                  | week04  tute3     |
| Part 2| L4 DS Models                | week05  tute4     |
|       | L5 OS DS                    | week06  tute5     |
|       |                             | week07  tute6     |
|       | L6 RMI Programming          | week08  tute7     |
| Part 3| L7 Security                 | week09  tute8     |
|       |                             | week10  tute9     |
|       | L8 Distributed File Systems | week11  tute10    |
|       | L10 Naming service          | week12  tute11    |

refer to this link: [COMP90015](http://clouds.cis.unimelb.edu.au/652/LectureSlides.html)



### How to run assignment 1 - multi-threaded-Dictionary-server

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
