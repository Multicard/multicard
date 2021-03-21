package ch.cas.html5.multicardgame.services;

import ch.cas.html5.multicardgame.repository.HandRepository;
import org.springframework.stereotype.Service;

@Service
public class HandServiceImpl {
    private final ch.cas.html5.multicardgame.repository.HandRepository handRepository;

    public HandServiceImpl(HandRepository handRepository) {
        this.handRepository = handRepository;
    }

    public void deleteHand(String handId) {
        handRepository.deleteById(handId);
    }

}
