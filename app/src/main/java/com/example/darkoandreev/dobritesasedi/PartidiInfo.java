package com.example.darkoandreev.dobritesasedi;

/**
 * Created by darko.andreev on 6/2/2017.
 */

public class PartidiInfo {

    public String partidaNomer;
    public String partidaBalance;
    public String partidaPropertyRefs;

    public String getHolderAccount() {
        return holderAccount;
    }

    public void setHolderAccount(String holderAccount) {
        this.holderAccount = holderAccount;
    }

    public String holderAccount;

    public String getPartidaNomer() {
        return partidaNomer;
    }

    public void setPartidaNomer(String partidaNomer) {
        this.partidaNomer = partidaNomer;
    }

    public String getPartidaBalance() {
        return partidaBalance;
    }

    public void setPartidaBalance(String partidaBalance) {
        this.partidaBalance = partidaBalance;
    }

    public String getPartidaPropertyRefs() {
        return partidaPropertyRefs;
    }

    public void setPartidaPropertyRefs(String partidaPropertyRefs) {
        this.partidaPropertyRefs = partidaPropertyRefs;
    }
}
