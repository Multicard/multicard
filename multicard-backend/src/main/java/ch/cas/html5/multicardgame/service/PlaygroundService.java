package ch.cas.html5.multicardgame.service;

import ch.cas.html5.multicardgame.entity.Playground;

import java.util.List;


public interface PlaygroundService {

    public List<Playground> retrievePlaygrounds();

    public Playground getPlayground(String PlaygroundId);

    public Playground savePlayground(Playground Playground);

    public void deletePlayground(String PlaygroundId);
}
