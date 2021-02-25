package ch.cas.html5.multicardgame.implementation;


import ch.cas.html5.multicardgame.entity.Playground;
import ch.cas.html5.multicardgame.entity.User;
import ch.cas.html5.multicardgame.repository.PlaygroundRepository;
import ch.cas.html5.multicardgame.repository.UserRepository;
import ch.cas.html5.multicardgame.service.PlaygroundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PLaygroundServiceImpl implements PlaygroundService {
    @Autowired
    private PlaygroundRepository PlaygroundRepository;

    public void setPlaygroundRepository(PlaygroundRepository PlaygroundRepository) {
        this.PlaygroundRepository = PlaygroundRepository;
    }

    public List<Playground> retrievePlaygrounds() {
        List<Playground> Playgrounds = PlaygroundRepository.findAll();
        return Playgrounds;
    }

    public Playground getPlayground(String PlaygroundId) {
        Optional<Playground> optPlayground = PlaygroundRepository.findById(PlaygroundId);
        if (optPlayground.isPresent()) {
            return optPlayground.get();
        }
        return null;
    }

    public Playground savePlayground(Playground Playground){
        return PlaygroundRepository.save(Playground);
    }

    public void deletePlayground(String PlaygroundId){
        PlaygroundRepository.deleteById(PlaygroundId);
    }

}
