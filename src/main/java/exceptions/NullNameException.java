/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 *
 * @author Lucas HtFilia Lebihan
 */
public class NullNameException extends NameException {
    public NullNameException() {
        System.out.println("A username can't be null.");
    }
}
