package ch.cas.html5.multicardgame.services;

import ch.cas.html5.multicardgame.entity.Connection;
import ch.cas.html5.multicardgame.repository.ConnectionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConnectionServiceImpl {
    private final ConnectionRepository connectionRepository;

    public ConnectionServiceImpl(ConnectionRepository connectionRepository) {
        this.connectionRepository = connectionRepository;
    }

    public Connection getConnection(String connectionId) {
        Optional<Connection> optEmp = connectionRepository.findById(connectionId);
        return optEmp.orElse(null);
    }

    public Connection saveConnection(Connection connection){
        return connectionRepository.save(connection);
    }

    public void deleteConnection(Connection connection){
        connectionRepository.delete(connection);
    }

    public List<Connection> getConnectionByPlayer(String playerId) { return connectionRepository.getConnectionByPlayer(playerId); }

}
