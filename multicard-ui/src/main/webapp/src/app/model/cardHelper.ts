import {CardDTO, HandDTO} from '../../app-gen/generated-model';

export const getCardImage = (card: CardDTO) => 'assets/cards/' + (card.faceUp ? card.name : 'BLUE_BACK') + '.svg';

export const createCardsForHand = (hand: HandDTO): CardDTO[] => {
  if (hand === undefined) {
    return [];
  }

  if (hand?.cards !== undefined && hand.cards.length > 0 ) {
    return hand.cards;
  } else {
    return new Array(hand?.cardCount).fill({faceUp: false});
  }
};
