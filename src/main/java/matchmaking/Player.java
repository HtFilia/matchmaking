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
import exceptions.NullNameException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Lucas HtFilia Lebihan
 */
public class Player {
    
    private String name;
    
    private Elo elo;
    
    private int factorK;
    
    private int numberGamesPlayed;
    
    private boolean isPlaying;
    
    private List<Game> games;
    
    public Player(String name) throws NullNameException {
        if (name == null) {
            throw new NullNameException();
        }
        this.name = name;
        this.elo = new Elo();
        this.factorK = 40;
        this.numberGamesPlayed = 0;
        this.isPlaying = false;
        this.games = new ArrayList<>();
    }
    
    public Player(String name, Elo elo, int factorK, int numberGamesPlayed)
            throws NullNameException {
        if (name == null) {
            throw new NullNameException();
        }
        this.name = name;
        this.elo = elo;
        this.factorK = factorK;
        this.numberGamesPlayed = numberGamesPlayed;
        this.isPlaying = false;
        this.games = new ArrayList<>();
    }
    
    public Player(Connection connection, String name) 
            throws NotInDatabaseException, SQLException {
        int eloValue = 1000;
        int factorK = 40;
        int numberGamesPlayed = 0;
        ResultSet rsPlayerDB;
        String stringPlayerDB = "SELECT * FROM `players` WHERE `Pseudo` = ?";
        PreparedStatement psPlayerDB = connection.prepareStatement(stringPlayerDB);
        psPlayerDB.setString(1, name);
        rsPlayerDB = psPlayerDB.executeQuery();
        if (rsPlayerDB.next()) {
            eloValue = rsPlayerDB.getInt("Elo");
            factorK = rsPlayerDB.getInt("Factor K");
            numberGamesPlayed = rsPlayerDB.getInt("Number Games Played");
        } else {
            throw new NotInDatabaseException();
        }
        
        this.name = name;
        this.elo = new Elo(eloValue);
        this.factorK = factorK;
        this.numberGamesPlayed = numberGamesPlayed;
        this.isPlaying = false;
        this.games = new ArrayList<>();
    }
    
    public String getName() {
        return this.name;
    }
    
    public Elo getElo() {
        return elo;
    }
    
    public int getFactorK() {
        return factorK;
    }
    
    public boolean isPlaying() {
        return isPlaying;
    }
    
    public Game currentGame() {
        if (games.size() > 0) {
            return games.get(0);
        }
        return null;
    }
    
    public void hasPlayedGame() {
        this.numberGamesPlayed += 1;
        if (factorK == 40 && (numberGamesPlayed > 30 || elo.getEloValue() > 2300)) {
            factorK = 20;
        }
        if (factorK != 10 && elo.getEloValue() > 2400 && numberGamesPlayed > 30) {
            factorK = 10;
        }
    }
    
    public void setElo(int elo) {
        this.elo.setEloValue(elo);
    }
    
    public void updatePlayer(Connection connection)
            throws SQLException {
        int eloValue = elo.getEloValue();
        // I was a bit lazy to do this in one single request
        // and it's easier to understand and modify this way.
        String requestElo = "UPDATE `Players` SET `Elo` = ? WHERE `Pseudo` = ?";
        String requestFactorK = "UPDATE `Players` SET `Factor K` = ? WHERE `Pseudo` = ?";
        String requestNbGames = "UPDATE `Players` SET `Number Games Played` = ? WHERE `Pseudo` = ?";
        PreparedStatement psRequestElo = connection.prepareStatement(requestElo);
        PreparedStatement psRequestFactorK = connection.prepareStatement(requestFactorK);
        PreparedStatement psRequestNbGames = connection.prepareStatement(requestNbGames);
        psRequestElo.setInt(1, eloValue);
        psRequestFactorK.setInt(1, factorK);
        psRequestNbGames.setInt(1, numberGamesPlayed);
        psRequestElo.setString(2, name);
        psRequestFactorK.setString(2, name);
        psRequestNbGames.setString(2, name);
        psRequestElo.execute();
        psRequestFactorK.execute();
        psRequestNbGames.execute();
    }
    
    public void lookingForGame(GameQueue gameQueue) {
        gameQueue.addPlayer(this);
    }
    
    public void addGame(Game game) {
        games.add(game);
        isPlaying = true;
    }
    
    public void removeGame(Game game) {
        games.remove(game);
        if (games.size() == 0) {
            isPlaying = false;
        }
    }
    
    @Override
    public String toString() {
        String toPrint = "Username: " + this.name + "\nElo: " + this.elo;
        return toPrint;
    }
    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Player)) {
            return false;
        }
        
        Player player = (Player) o;
        return (this.name.equals(player.getName()));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.name);
        return hash;
    }
}
