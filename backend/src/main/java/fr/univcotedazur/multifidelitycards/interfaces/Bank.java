package fr.univcotedazur.multifidelitycards.interfaces;

/**
 * Bank interface
 */
public interface Bank {

    /**
     * pay with credit card
     * @param creditCard credit card
     * @param value amount to pay
     * @return true if OK else false KO
     */
    boolean pay(String creditCard, double value) ;
}
