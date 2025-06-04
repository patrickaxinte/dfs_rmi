# DFS RMI Example

This project contains a minimal distributed file system implemented using Java RMI. It demonstrates a master server coordinating several replica servers that store data, as well as a simple client used to read and write files.

## Prerequisites

- **JDK 21** (or later)
- No manual start of `rmiregistry` is necessary; both `MasterServer` and each `ReplicaServer` create their own RMI registries when launched.

## Compilation

From the project root run:

```bash
javac src/*.java
```

All class files are placed in the `src` directory.

## Running

1. **Start the master server**

   ```bash
   java -cp src MasterServer
   ```

2. **Start one or more replica servers** in separate terminals. Each replica takes a unique numeric identifier which determines its RMI registry port.

   ```bash
   java -cp src ReplicaServer 1
   java -cp src ReplicaServer 2
   ```

3. **Run the client**

   ```bash
   java -cp src Client
   ```

The client contacts the master server to obtain the list of replicas and performs a basic write followed by a read from one of the replicas.
