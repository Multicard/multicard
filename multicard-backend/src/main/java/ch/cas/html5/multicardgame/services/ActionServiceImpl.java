package ch.cas.html5.multicardgame.services;

import ch.cas.html5.multicardgame.entity.Action;
import ch.cas.html5.multicardgame.repository.ActionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActionServiceImpl {

    private final ActionRepository actionRepository;

    public ActionServiceImpl(ActionRepository actionRepository) {
        this.actionRepository = actionRepository;
    }

    public void deleteAction(String actionId) {
        actionRepository.deleteById(actionId);
    }

    public Long getNextValFromSeq(){ return actionRepository.getNextValueSeq(); }

    public List<Action> getActionsSorted(String gameId) {
        return actionRepository.getActionSorted(gameId);
    }

}

