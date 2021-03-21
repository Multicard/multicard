import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Player} from '../model/game.model';
import {Observable} from 'rxjs';

const MULTICARD_GAME_PLAYER_KEY = 'MULTICARD_GAME.player';
const PLAYER_REST_API_URL = '/api/Players';

@Injectable({providedIn: 'root'})
export class PlayerService {

  constructor(private http: HttpClient) {
  }

  checkPassword(playerId: string, password: string): Observable<boolean> {
    return this.http.get<boolean>(`${PLAYER_REST_API_URL}/${playerId}/pwd`, {headers: {pwd: password}});
  }

  loadPlayerFromLocalStorage(): Player {
    return Player.deserializePlayer(localStorage.getItem(MULTICARD_GAME_PLAYER_KEY));
  }

  storePlayerInLocalStorage(player: Player) {
    localStorage.setItem(MULTICARD_GAME_PLAYER_KEY, JSON.stringify(player));
  }
}
