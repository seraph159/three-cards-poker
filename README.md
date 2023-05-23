# Three Cards Poker Game

This is a Three Cards Poker game implemented using JavaFX. The game utilizes multi-threaded programming and network programming to support multiplayer functionality.

<div align="center">
  <img src="https://i.ibb.co/HhDpCF1/3cardspokerdemo.png" alt="3CardsPoker" width="50%">
</div>

## Requirements

- Java Development Kit (JDK) 8 or higher
- JavaFX SDK (included with JDK 8 and JDK 11, or separate download for JDK 9+)

## Installation

1. Clone the repository or download the source code.
2. Ensure that the JavaFX SDK is properly configured in your development environment.
3. Build the project using your preferred build tool (preferably Apache Maven).
4. Run the compiled application.

## Usage

1. Launch the application.
2. Specify the network settings (e.g., host IP address and port) for the multiplayer mode.
3. Each player will be dealt three cards.
4. Follow the on-screen prompts to make decisions during the game.
5. The winner will be determined based on the hand rankings.

## Multi-threaded Programming

The application leverages multi-threaded programming to support concurrent gameplay and networking. The main thread is responsible for managing the game flow and updating the user interface. The networking tasks are performed on separate threads to handle incoming and outgoing messages, ensuring smooth communication between players.

## Network Programming

The network programming aspect enables multiplayer functionality, allowing multiple players to join a game session over a network. The application uses sockets and a client-server model to establish connections between players. Players can connect to a host by specifying the host's IP address and port. The network communication is asynchronous, enabling real-time updates and interaction between players.

## Folder Structure

The project directory contains the following files and folders:

- `3CardsPoker_ClientSide/`: Contains the source code files for the client side.
- `3CardsPoker_ServerSide/`: Contains the source code files for the server side.

## License

This project is licensed under the [Apache License 2.0](LICENSE).
