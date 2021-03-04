package ch.cas.html5.multicardgame.services;

import ch.cas.html5.multicardgame.entity.Stack;
import ch.cas.html5.multicardgame.repository.StackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StackServiceImpl {

    @Autowired
    private StackRepository stackRepository;

    public void setStackRepository(StackRepository stackRepository) {
        this.stackRepository = stackRepository;
    }

    public List<Stack> retrieveStacks() {
        List<Stack> stacks = stackRepository.findAll();
        return stacks;
    }

    public Stack getStack(String stackId) {
        Optional<Stack> optStack = stackRepository.findById(stackId);
        if (optStack.isPresent()) {
            return optStack.get();
        }
        return null;
    }

    public Stack saveStack(Stack stack){
        return stackRepository.save(stack);
    }

    public void deleteStack(String stackId){
        stackRepository.deleteById(stackId);
    }

}

