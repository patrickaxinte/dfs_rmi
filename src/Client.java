import java.rmi.*;
import java.util.List;

public class Client {
    public static void main(String[] args) {
        try {
            // conectarea la MasterServer
            MasterServerInterface master = (MasterServerInterface) Naming.lookup("//localhost/MasterServer");
            System.out.println("Fetching replica locations...");

            // obtinerea locatiilor replicilor
            List<ReplicaLoc> replicas = master.getReplicaLocations("anyFile");
            if (replicas.isEmpty()) {
                System.out.println("No replicas available.");
                return;
            }

            // utilizarea ChainManager pentru gestionarea replicilor
            ChainManager chainManager = new ChainManager(replicas);

            // determinarea nodului pentru operatii (primul sau ultimul nod)
            boolean readFromLast = true; // poti seta la false pentru a citi de pe primul nod
            ReplicaLoc targetReplica;
            try {
                targetReplica = readFromLast ? chainManager.getLastNode() : chainManager.getFirstNode();
            } catch (ReplicaChainException e) {
                System.err.println("Error: " + e.getMessage());
                return;
            }

            // conectarea la ReplicaServer
            ReplicaServerInterface replica = (ReplicaServerInterface)
                    Naming.lookup("//" + targetReplica.getHost() + "/ReplicaServer" + targetReplica.getId());

            // scrierea datelor pe primul nod (indiferent de nodul pentru citire)
            String fileName = "testFile.txt";
            String fileContent = "Hello from Client!";
            replica.write(fileName, fileContent);
            System.out.println("File written: " + fileName);

            // citirea datelor de pe replica selectata
            String retrievedData = replica.read(fileName);
            System.out.println("File content: " + retrievedData);

        } catch (java.rmi.NotBoundException e) {
            System.err.println("Error: Unable to bind to remote object. Ensure the server is running.");
        } catch (java.rmi.ConnectException e) {
            System.err.println("Error: Could not connect to the server. Ensure the RMI registry is active and reachable.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
