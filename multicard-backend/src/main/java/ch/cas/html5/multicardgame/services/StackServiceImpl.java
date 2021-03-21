package ch.cas.html5.multicardgame.services;

import ch.cas.html5.multicardgame.entity.Stack;
import ch.cas.html5.multicardgame.repository.StackRepository;
import org.springframework.stereotype.Service;

@Service
public class StackServiceImpl {

    private final StackRepository stackRepository;

    public StackServiceImpl(StackRepository stackRepository) {
        this.stackRepository = stackRepository;
    }

    public Stack saveStack(Stack stack){
        return stackRepository.save(stack);
    }

    public void deleteStack(String stackId){
        stackRepository.deleteById(stackId);
    }

}

