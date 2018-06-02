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
public class NameException extends Exception {
    
    public NameException() {
        System.out.println("Something went wrong with the username.");
    }
    
    public NameException(String message) {
        System.out.println(message);
    }
}
