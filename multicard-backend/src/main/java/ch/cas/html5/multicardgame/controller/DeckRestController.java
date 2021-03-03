package ch.cas.html5.multicardgame.controller;

import ch.cas.html5.multicardgame.implementation.DeckServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
public class DeckRestController {
    @Autowired
    private DeckServiceImpl deckServiceImpl;


}
