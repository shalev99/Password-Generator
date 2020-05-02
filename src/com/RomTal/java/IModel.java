package com.RomTal.java;

/***
 * Password Generator Manger Interface
 */
public interface IModel {
    /***
     * Generate a new Password
     */
    public void generatePassword();

    /***
     * Generate a new password from a specific characters dictionary
     * @param startingChar Character dictionary
     */
    public void generatePassword(char startingChar);

    /***
     * Generate a new password in length that specific length(minimum 16 characters)
     * @param length Password length (minimum 16 characters)
     */
    public void generatePassword(int length);

    /***
     * checking if password equal to string
     * @param compareString password to compare
     * @return
     */
    public boolean equals(String compareString);

    /***
     * checking if password include symbols
     * @return
     */
    public boolean hasSymbols();

    /***
     * convert to string
     * @return
     */
    public String toString();
}
