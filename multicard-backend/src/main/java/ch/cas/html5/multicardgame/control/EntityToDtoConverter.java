package ch.cas.html5.multicardgame.control;

import ch.cas.html5.multicardgame.dto.CardDTO;
import ch.cas.html5.multicardgame.dto.PlayedCardDTO;
import ch.cas.html5.multicardgame.dto.PlayerScoreDTO;
import ch.cas.html5.multicardgame.dto.ScoreDTO;
import ch.cas.html5.multicardgame.entity.Card;
import ch.cas.html5.multicardgame.entity.Score;
import ch.cas.html5.multicardgame.enums.Action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public final class EntityToDtoConverter {

    public List<CardDTO> convertCards(Set<Card> cards, Boolean anonymous){

        ArrayList cardsdto = new ArrayList();
        for (Card card : cards){
            cardsdto.add(new CardDTO(card.getId(), (anonymous ? "N/A" : card.getName()), card.getSort(), !anonymous));
        }
        Collections.sort(cardsdto);
        return cardsdto;
    }

    public List<PlayedCardDTO> convertPlayedCards(Set<Card> cards, List<ch.cas.html5.multicardgame.entity.Action> actions){
        List<PlayedCardDTO> cardsdto = new ArrayList() ;
        for (int i = actions.size(); i > 0; i--) {
            if (actions.get(i-1).getAction().equals(Action.CLIENT_CARD_PLAYED)){
                Card c = getCardFromPlayedCards(actions.get(i-1).getCards_id().stream().findFirst().get(), cards);
                if (c != null){
                    PlayedCardDTO playedCard = new PlayedCardDTO();
                    playedCard.setId(c.getId());
                    playedCard.setName(c.getName());
                    playedCard.setSort(c.getSort());
                    playedCard.setFaceUp(Boolean.TRUE);
                    playedCard.setPlayerId(c.getPlayer().getId());
                    cardsdto.add(playedCard);
                }
            }
        }
        return cardsdto;
    }

    private Card getCardFromPlayedCards(String card_id, Set<Card> cards){
        for (Card card : cards){
            if (card.getId().equals(card_id)){
                return card;
            }
        }
        return null;
    }

    public List<ScoreDTO> convertGameScore(List<Score> scores){
        if (scores.size() < 1) { return null; }
        int currentRound = scores.get(0).getRound();
        ArrayList<ScoreDTO> dtoList = new ArrayList<>();
        for (int i=1; i <= currentRound; i = i + 1){
            ScoreDTO scoredto = new ScoreDTO();
            scoredto.setRound(i);
            scoredto.setPlayerScores(getPLayerScore(scores, i));
            dtoList.add(scoredto);
        }
        return dtoList;
    }

    private List<PlayerScoreDTO> getPLayerScore(List<Score> scores, int round){
        ArrayList<PlayerScoreDTO> playerScoreList = new ArrayList<>();
        for (Score score : scores){
            if (score.getRound().intValue() == round){
                PlayerScoreDTO newdto = new PlayerScoreDTO();
                newdto.setScore(score.getScore());
                newdto.setPlayerId(score.getPlayerId());
                playerScoreList.add(newdto);
            }
        }
        return playerScoreList;
    }
}
