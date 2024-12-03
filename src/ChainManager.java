import java.util.List;

public class ChainManager {
    private final List<ReplicaLoc> replicas;

    public ChainManager(List<ReplicaLoc> replicas) {
        this.replicas = replicas;
    }

    /**
     * returneaza primul nod din lantul de replicare.
     *
     * @return ReplicaLoc - Primul nod din lant.
     * @throws ReplicaChainException daca lanțul este gol.
     */
    public ReplicaLoc getFirstNode() throws ReplicaChainException {
        if (replicas == null || replicas.isEmpty()) {
            throw new ReplicaChainException("Replication chain is empty. No nodes available.");
        }
        return replicas.get(0);
    }

    /**
     * returneaza ultimul nod din lantul de replicare.
     *
     * @return ReplicaLoc - Ultimul nod din lant.
     * @throws ReplicaChainException daca lantul este gol.
     */
    public ReplicaLoc getLastNode() throws ReplicaChainException {
        if (replicas == null || replicas.isEmpty()) {
            throw new ReplicaChainException("Replication chain is empty. No nodes available.");
        }
        return replicas.get(replicas.size() - 1);
    }
}
