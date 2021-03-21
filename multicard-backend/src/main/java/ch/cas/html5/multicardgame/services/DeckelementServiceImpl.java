package ch.cas.html5.multicardgame.services;

import ch.cas.html5.multicardgame.entity.Deckelement;
import ch.cas.html5.multicardgame.repository.DeckelementRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeckelementServiceImpl {
    private final DeckelementRepository deckelementRepository;

    public DeckelementServiceImpl(DeckelementRepository deckelementRepository) {
        this.deckelementRepository = deckelementRepository;
    }

    public Deckelement saveDeckelement(Deckelement deckelement){
        return deckelementRepository.save(deckelement);
    }

    public List<Deckelement> getDeckelementsByDeck(String deckId) {
        return deckelementRepository.getDeckelementsByDeck(deckId);
    }


}
