package ch.cas.html5.multicardgame.services;

import ch.cas.html5.multicardgame.entity.Action;
import ch.cas.html5.multicardgame.repository.ActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActionServiceImpl {

    @Autowired
    private ActionRepository actionRepository;

    public void setActionRepository(ActionRepository actionRepository) {
        this.actionRepository = actionRepository;
    }

    public List<Action> retrieveActions() {
        List<Action> action = actionRepository.findAll();
        return action;
    }

    public Action getAction(String cardId) {
        Optional<Action> optEmp = actionRepository.findById(cardId);
        return optEmp.get();
    }

    public Action saveAction(Action action) {
        return actionRepository.save(action);
    }

    public void deleteAction(String actionId) {
        actionRepository.deleteById(actionId);
    }

    public void updateAction(Action action) {
        actionRepository.save(action);
    }

}

