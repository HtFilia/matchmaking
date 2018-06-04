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
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lucas HtFilia Lebihan
 */
public final class Simulation {
    
    private Connection connection;
    private Server server;
    private List<Client> clients;
    
    public Simulation(int treshold) 
            throws SQLException, ClassNotFoundException, NotInDatabaseException {
        connection = connectDB();
        server = initServer(treshold);
        initClients();
    }
    
    public Connection connectDB()
            throws SQLException, ClassNotFoundException {
        final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        final String DB_URL = "jdbc:mysql://localhost:3306/matchmakingtest?"
                + "useUnicode=yes"
                + "&characterEncoding=UTF-8" 
                + "&useJDBCCompliantTimezoneShift=true"
                + "&useLegacyDatetimeCode=false&serverTimezone=UTC";
        final String DB_USER = "root";
        final String DB_PWD = "";
        Class.forName(JDBC_DRIVER);
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PWD);
    }
    
    public List<String> extractPlayersName() 
            throws SQLException {
        List<String> playersNames = new ArrayList<>();
        String request = "SELECT `Pseudo` FROM `players`";
        Statement stmt = connection.prepareStatement(request);
        ResultSet rs = stmt.executeQuery(request);
        while (rs.next()) {
            playersNames.add(rs.getString("Pseudo"));
        }
        return playersNames;
    }
    
    public Player extractPlayerFromDB(String name) 
            throws SQLException, NotInDatabaseException {
        Player player = new Player(connection, name);
        return player;
    }
    
    public List<Player> extractPlayersFromDB()
            throws SQLException, NotInDatabaseException {
        List<Player> players = new ArrayList<>();
        List<String> playersNames = extractPlayersName();
        for (String name : playersNames) {
            players.add(extractPlayerFromDB(name));
        }
        return players;
    }
    
    public void addClient(Player player) {
        Client client = new Client(player, server);
        clients.add(client);
    }
    
    public void initClients()
            throws SQLException, NotInDatabaseException {
        List<Player> players = extractPlayersFromDB();
        players.forEach((player) -> {
            addClient(player);
        });
    }
    
    public Server initServer(int treshold) {
        GameQueue gameQueue = new GameQueue(treshold, connection);
        return new Server(gameQueue, connection);
    }
    
    public List<Thread> createClientThreads() {
        List<Thread> clientThreads = new ArrayList<>();
        clients.forEach((client) -> {
            clientThreads.add(new Thread(client));
        });
        return clientThreads;
    }
    
    public Thread createServerThread() {
        return new Thread(server);
    }
    
    public static void main(String[] args) 
            throws SQLException, ClassNotFoundException, NotInDatabaseException {
        Simulation simulation = new Simulation(15);
        simulation.createServerThread().start();
        simulation.createClientThreads().forEach((thread) -> {
            thread.start();
        });
    }
}
