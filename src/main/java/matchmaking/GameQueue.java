/*
 * The MIT License
 *
 * Copyright 2018 Lucas HtFilia Lebihan.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package matchmaking;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lucas HtFilia Lebihan
 */
public class GameQueue {
    
    private List<Player> playersWaiting;
    
    private final double treshold;
    
    private final Connection connection;
    
    public GameQueue(int treshold, Connection connection) {
        this.playersWaiting = new ArrayList<>();
        this.treshold = treshold;
        this.connection = connection;
    }
    
    public void addPlayer(Player player) {
        this.playersWaiting.add(player);
    }
    
    public boolean shouldMatch(Player player1, Player player2) {
        double probWin = player1.getElo().probWin(player2.getElo());
        return (probWin < treshold);
    }
    
    public int numberPlayersWaiting() {
        return playersWaiting.size();
    }
    
    public void findMatch() {
        boolean shouldMatch;
        int index = 1;
        for (; index < playersWaiting.size(); ++index) {
            shouldMatch = shouldMatch(playersWaiting.get(0), playersWaiting.get(index));
            if (shouldMatch) {
                break;
            }
        }
        Game game = new Game(playersWaiting.get(0), playersWaiting.get(index), connection);
        playersWaiting.get(0).addGame(game);
        playersWaiting.get(index).addGame(game);
        playersWaiting.remove(0);
        playersWaiting.remove(index);
    }
}
