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

import exceptions.NotInDatabaseException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;

/**
 *
 * @author Lucas HtFilia Lebihan
 */
public class Game {
    
    private final Player player1;
    
    private final Player player2;
    
    private final Connection connection;
     
    public Game(Player player1, Player player2, Connection connection) {
        if (player1.equals(player2)) {
            throw new IllegalArgumentException("Can't match a player with himself.");
        }
        
        this.player1 = player1;
        this.player2 = player2;
        this.connection = connection;
    }
    
    public Player getFirstPlayer() {
        return player1;
    }
    
    public Player getSecondPlayer() {
        return player2;
    }
    
    public void endGame() 
            throws NotInDatabaseException, SQLException {
        Random rand = new Random();
        double randValue = rand.nextDouble();
        System.out.print("Fin du match entre " + player1.getName() + " et "
                         + player2.getName() + "\nResultat : Victoire de ");
        if (randValue < player1.getElo().probWin(player2.getElo())) {
            player1.getElo().changeElo(player2.getElo(), player1.getFactorK(), 1);
            player2.getElo().changeElo(player1.getElo(), player2.getFactorK(), 0);
            System.out.println(player1.getName());
        } else {
            player1.getElo().changeElo(player2.getElo(), player1.getFactorK(), 0);
            player2.getElo().changeElo(player1.getElo(), player2.getFactorK(), 1);
            System.out.println(player2.getName());
        }
        player1.updatePlayer(connection);
        player2.updatePlayer(connection);
        player1.removeGame(this);
        player2.removeGame(this);
        player1.hasPlayedGame();
        player2.hasPlayedGame();
        System.out.println("Nouveau classement : \n" + player1.toString() 
                            + "\n" + player2.toString());
    }
}
