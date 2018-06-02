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
public class NotInDatabaseException extends NameException {
    public NotInDatabaseException() {
        System.out.println("This player is not registered yet.");
    }
}
