import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;
import java.util.*;

public class ReplicaServer extends UnicastRemoteObject implements ReplicaServerInterface {
    private final String name; // numele replicii
    private final Map<String, String> fileStorage = new HashMap<>(); // stocarea locala a fisierelor

    public ReplicaServer(String name) throws RemoteException {
        super();
        this.name = name;
    }

    @Override
    public synchronized void write(String fileName, String data) throws RemoteException {
        // salvarea locala a fisierului
        fileStorage.put(fileName, data);
        System.out.println("[" + name + "] File written: " + fileName);
    }

    @Override
    public synchronized String read(String fileName) throws RemoteException {
        // returneaza continutul fisierului sau mesajul "File not found"
        return fileStorage.getOrDefault(fileName, "File not found");
    }

    public static void main(String[] args) {
        // verificarea argumentelor de linie de comanda
        if (args.length != 1) {
            System.err.println("Usage: java ReplicaServer <replica_name>");
            return;
        }

        String replicaName = args[0];

        try {
            // crearea unui registry RMI local pe un port unic pentru fiecare replica
            LocateRegistry.createRegistry(1100 + Integer.parseInt(replicaName)); // Port bazat pe numele replicii

            // crearea obiectului ReplicaServer
            ReplicaServer replica = new ReplicaServer(replicaName);

            // inregistrarea in RMI registry
            Naming.rebind("ReplicaServer" + replicaName, replica);

            // conectarea la MasterServer si inregistrarea replicii
            try {
                MasterServerInterface master = (MasterServerInterface) Naming.lookup("//localhost/MasterServer");
                ReplicaLoc location = new ReplicaLoc(replicaName, "localhost", true);
                master.registerReplicaServer(replicaName, location);
                System.out.println("[" + replicaName + "] Registered with MasterServer.");
            } catch (Exception e) {
                System.err.println("Error: Failed to register with MasterServer. Check the MasterServer is running.");
                return; // oprirea initializarii daca nu se poate inregistra
            }

            System.out.println("Replica Server " + replicaName + " is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
