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

/**
 *
 * @author Lucas HtFilia Lebihan
 */
public class Elo {
    
    private int elo;
    
    public Elo() {
        this.elo = 1000;
    }
    
    public Elo(int elo) {
        this.elo = elo;
    }
    
    public int getEloValue() {
        return this.elo;
    }
    
    public void setEloValue(int elo) {
        this.elo = elo;
    }
    
    public void addDeltaElo(int deltaElo) {
        this.elo += deltaElo;
    }
    
    public double probWin(Elo elo) {
        return 1 / (1 + Math.pow(10, (elo.getEloValue() - this.elo) / 400));
    }
    
    public void changeElo(Elo elo, int factorK, int resultGame) {
        int deltaElo = (int) (factorK * (resultGame - this.probWin(elo)));
        this.addDeltaElo(deltaElo);
        elo.addDeltaElo(-deltaElo);
    }
}
