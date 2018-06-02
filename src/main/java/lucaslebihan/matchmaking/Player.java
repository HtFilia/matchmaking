/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lucaslebihan.matchmaking;

import exceptions.NotInDatabaseException;
import exceptions.NullNameException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 *
 * @author Lucas HtFilia Lebihan
 */
public class Player {
    
    private String name;
    
    private int elo;
    
    public Player(String name) throws NullNameException {
        if (name == null) {
            throw new NullNameException();
        }
        this.name = name;
        this.elo = 1000;
    }
    
    public Player(Connection connection, String name) 
            throws NotInDatabaseException, SQLException {
        ResultSet rsPlayerDB;
        String stringPlayerDB = "SELECT * FROM Players WHERE Pseudo = ?";
        PreparedStatement psPlayerDB = connection.prepareStatement(stringPlayerDB);
        psPlayerDB.setString(1, name);
        rsPlayerDB = psPlayerDB.executeQuery();
        boolean canCreate = !(rsPlayerDB.next());
        if (canCreate) {
            this.name = name;
            this.elo = rsPlayerDB.getInt("Elo");
        } else {
            throw new NotInDatabaseException();
        }
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getElo() {
        return this.elo;
    }
    
    public void setElo(int elo) {
        this.elo = elo;
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
