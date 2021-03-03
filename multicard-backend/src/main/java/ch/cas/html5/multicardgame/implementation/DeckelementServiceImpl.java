package ch.cas.html5.multicardgame.implementation;

import ch.cas.html5.multicardgame.entity.Deckelement;
import ch.cas.html5.multicardgame.repository.DeckelementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeckelementServiceImpl {
    @Autowired
    private DeckelementRepository deckelementRepository;

    public void setDeckelementRepository(DeckelementRepository deckelementRepository) {
        this.deckelementRepository = deckelementRepository;
    }

    public List<Deckelement> retrieveDecks() {
        List<Deckelement> deckelements = deckelementRepository.findAll();
        return deckelements;
    }

    public Deckelement getDeckelement(String deckelementId) {
        Optional<Deckelement> optDeckelement = deckelementRepository.findById(deckelementId);
        if (optDeckelement.isPresent()) {
            return optDeckelement.get();
        }
        return null;
    }

    public Deckelement saveDeckelement(Deckelement deckelement){
        return deckelementRepository.save(deckelement);
    }

    public void deleteDeckelement(String deckelementId){
        deckelementRepository.deleteById(deckelementId);
    }


}
