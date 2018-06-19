package com.lhalcyon.dapp.manager;

import com.lhalcyon.dapp.model.HLWallet;

import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;

import java.security.SecureRandom;

import io.github.novacrypto.bip32.ExtendedPrivateKey;
import io.github.novacrypto.bip32.networks.Bitcoin;
import io.github.novacrypto.bip39.MnemonicGenerator;
import io.github.novacrypto.bip39.SeedCalculator;
import io.github.novacrypto.bip39.Words;
import io.github.novacrypto.bip39.wordlists.English;
import io.github.novacrypto.bip44.AddressIndex;
import io.github.novacrypto.bip44.BIP44;
import io.reactivex.Flowable;

/**
 * <pre>
 * Create by  :    L
 * Create Time:    2018/6/19
 * Brief Desc :
 * </pre>
 */
public class InitWalletManager {


    /**
     * @return 生成一组随机的助记词
     */
    public Flowable<String> generateMnemonics() {
        StringBuilder sb = new StringBuilder();
        byte[] entropy = new byte[Words.TWELVE.byteLength()];
        new SecureRandom().nextBytes(entropy);
        new MnemonicGenerator(English.INSTANCE)
                .createMnemonic(entropy, sb::append);
        String mnemonics = sb.toString();
//        Logger.w("助记词:" + mnemonics);
        return Flowable.just(mnemonics);
    }

    public Flowable<HLWallet> generateWallet(String password, String theMnemonics) {
        Flowable<String> flowable = Flowable.just(theMnemonics);

        return flowable
                .map(mnemonics -> {
                    // eth
                    AddressIndex addressIndex = BIP44
                            .m()
                            .purpose44()
                            .coinType(60)
                            .account(0)
                            .external()
                            .address(0);
                    ExtendedPrivateKey rootKey = ExtendedPrivateKey.fromSeed(new SeedCalculator().calculateSeed(mnemonics, ""), Bitcoin.MAIN_NET);
                    System.out.println("mnemonics:"+mnemonics);
                    String extendedBase58 = rootKey.extendedBase58();
                    System.out.println("extendedBase58:"+extendedBase58);

                    ExtendedPrivateKey childPrivateKey = rootKey.derive(addressIndex, AddressIndex.DERIVATION);
                    String childExtendedBase58 = childPrivateKey.extendedBase58();

                    ECKeyPair ecKeyPair = Keys.deserialize(childPrivateKey.extendedKeyByteArray());
                    String privateKey = ecKeyPair.getPrivateKey().toString(16);
                    String publicKey = ecKeyPair.getPublicKey().toString(16);
                    childPrivateKey.extendedKeyByteArray();
                    String address = Keys.getAddress(publicKey);
                    int i = 0;

                    System.out.println("address:"+address);
//                    System.out.println("publicKey:"+publicKey);
//                    String fullAddress = "0x"+ Keys.getAddress(childEcKeyPair);
//                    WalletFile walletFile = Wallet.createLight(password, childEcKeyPair);
//                    System.out.println("full address:"+fullAddress + " -> 0x");

                    return new HLWallet();
                });
    }


    // ---------------- singleton stuff --------------------------
    public static InitWalletManager shared() {
        return InitWalletManager.Holder.singleton;
    }

    private InitWalletManager() {

    }

    private static class Holder {

        private static final InitWalletManager singleton = new InitWalletManager();

    }
}
