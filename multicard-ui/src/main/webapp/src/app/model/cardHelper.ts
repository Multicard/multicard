import {Card, Stack} from './game.model';

export const createCardsForStack = (stack: Stack) => {
  if (stack === undefined) {
    return [];
  }

  const cards = new Array(stack.numberOfCards).fill(new Card());
  if (stack.isFaceUp) {
    const topCard = cards[stack.numberOfCards - 1];
    topCard.isFaceUp = true;
    topCard.card = stack.topCard;
  }
  return cards;
};
