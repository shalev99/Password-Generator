package com.RomTal.java;

/***
 * GUI Manager Interface
 */
public interface IView {
    /***
     * Update table of data
     */
    public void rebuildPanel();

    /***
     * get name of account
     * @return selected key of account
     */
    public String getSelection();
    /***
     * get key of account
     * @return selected name of account
     */
    public String getSelectionKey();

    /***
     * set selection to null after deleting account
     */
    public void setSelection();

    /***
     * view/copy parameter of account
     * @param password password of an account
     */
    public void displayCopy(String password);

    /***
     * popup warning if account not been selected
     */
    public void displayWarning();
}
