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
import java.sql.DriverManager;
import java.sql.SQLException;
import org.junit.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 *
 * @author Lucas HtFilia Lebihan
 */
public class TestGame {
    
    private static Elo eloPlayer1;
    private static Elo eloPlayer2;
    private static Player player1;
    private static Player player2;
    private static Connection connection;
            
    @BeforeClass
    public static void connectDB() 
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
        connection  = DriverManager.getConnection(DB_URL, DB_USER, DB_PWD);
    }
    
    @Before
    public void initPlayers() throws NullNameException {
        eloPlayer1 = new Elo();
        eloPlayer2 = new Elo();
        player1 = new Player("player1", eloPlayer1, 40, 0);
        player2 = new Player("player2", eloPlayer2, 40, 0);
    }
    
    @Test
    public void initGameTest() {
        Game game1 = new Game(player1, player2, connection);
        assertEquals(game1.getFirstPlayer(), player1);
        assertEquals(game1.getSecondPlayer(), player2);
    }
    
    @Test
    public void endGameTest() 
            throws NotInDatabaseException, SQLException {
        Game game2 = new Game(player1, player2, connection);
        game2.endGame();
        assertNotEquals(player1.getElo().getEloValue(), 1000);
        assertNotEquals(player2.getElo().getEloValue(), 1000);
    }
}
