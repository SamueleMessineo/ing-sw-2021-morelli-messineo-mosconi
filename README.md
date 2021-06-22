![game banner](./banner.png)

Final project for the Software Engineering course at Polytechnic of Milan, 2021.

## Group PSP42
- **[Bruno Morelli](https://github.com/BrunoMor99)** - bruno.morelli@mail.polimi.it
- **[Samuele Messineo](https://github.com/SamueleMessineo)** - samuele.messineo@mail.polimi.it
- **[Alberto Mosconi](https://github.com/albertomosconi)** - albertomaria.mosconi@mail.polimi.it

[//]: # (symbols ✔️ ✖️)

## Development progress
| Functionality | Status |
|--- | --- | 
|Basic rules|✔|
|Complete rules|✔️|
|Server|✔️|
|CLI|✔️|
|GUI|✔️|
|Persistence|✔️|
|Offline games|✔️|
|Multiple games|✔️|
|Disconnection resilience|✔️|
|Parameter editor|✖️|

## How to start
This game can be played either online or offline. In order to play online you have to start the server using this command 
```
$ java -jar PSP42-server.jar
```
At this point the client can be started in two ways:
- **As CLI**
   ```
  $ java -jar PSP42-client.jar cli
  ```
- **As GUI (default)**
  ```
  $ java -jar PSP42-client.jar 
  ```
 
When playing online the user has to select the server ip address and the port. The port is 31415.

  

## Build

## Documentation and testing
