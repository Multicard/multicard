package ch.cas.html5.multicardgame.implementation;


import ch.cas.html5.multicardgame.service.PlaygroundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ch.cas.html5.multicardgame.entity.*;
import ch.cas.html5.multicardgame.repository.*;

import java.util.List;
import java.util.Optional;

@Service
public class PLaygroundServiceImpl implements PlaygroundService {
    @Autowired
    private ch.cas.html5.multicardgame.repository.PlaygroundRepository playgroundRepository;

    public void setPlaygroundRepository(PlaygroundRepository playgroundRepository) {
        this.playgroundRepository = playgroundRepository;
    }

    public List<Playground> retrievePlaygrounds() {
        List<Playground> playgrounds = playgroundRepository.findAll();
        return playgrounds;
    }

    public Playground getPlayground(String playgroundId) {
        Optional<Playground> optPlayground = playgroundRepository.findById(playgroundId);
        if (optPlayground.isPresent()) {
            return optPlayground.get();
        }
        return null;
    }

    public Playground savePlayground(Playground playground){
        return playgroundRepository.save(playground);
    }

    public void deletePlayground(String playgroundId){
        playgroundRepository.deleteById(playgroundId);
    }

}
