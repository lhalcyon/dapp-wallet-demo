package com.lhalcyon.dapp.model;

import org.web3j.crypto.WalletFile;

public class HLWallet {

    public String mnemonics;

    public String name;

    public String address;

    public WalletFile walletFile;


    public HLWallet() {
    }


    public HLWallet(String mnemonics, String name, String address) {
        this.mnemonics = mnemonics;
        this.name = name;
        this.address = address;
    }

    public boolean hasMnemonics(){
        return mnemonics != null;
    }

    public HLWallet(WalletFile walletFile) {
        this.walletFile = walletFile;
    }


}
