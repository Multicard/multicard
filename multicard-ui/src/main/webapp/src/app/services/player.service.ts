import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Player} from '../model/game.model';

const MULTICARD_GAME_PLAYER_KEY = 'MULTICARD_GAME.player';
const REST_API_URL = '/api/Games';

@Injectable({providedIn: 'root'})
export class PlayerService {

  constructor(private http: HttpClient) {
  }

  loadPlayerFromLocalStorage(): Player {
    return Player.deserializePlayer(localStorage.getItem(MULTICARD_GAME_PLAYER_KEY));
  }

  storePlayerInLocalStorage(player: Player) {
    localStorage.setItem(MULTICARD_GAME_PLAYER_KEY, JSON.stringify(player));
  }
}
