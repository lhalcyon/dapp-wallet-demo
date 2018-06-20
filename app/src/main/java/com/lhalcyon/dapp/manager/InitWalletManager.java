package com.lhalcyon.dapp.manager;

import android.content.Context;

import com.lhalcyon.dapp.model.HLWallet;
import com.orhanobut.logger.Logger;

import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;

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
     * generate a random group of mnemonics
     * 生成一组随机的助记词 
     */
    public Flowable<String> generateMnemonics() {
        StringBuilder sb = new StringBuilder();
        byte[] entropy = new byte[Words.TWELVE.byteLength()];
        new SecureRandom().nextBytes(entropy);
        new MnemonicGenerator(English.INSTANCE)
                .createMnemonic(entropy, sb::append);
        String mnemonics = sb.toString();
        return Flowable.just(mnemonics);
    }

    /**
     * 
     * @param context app context 上下文
     * @param password the wallet password(not the bip39 password) 钱包密码(而不是BIP39的密码)
     * @param mnemonics 助记词
     * @return wallet 钱包
     */
    public Flowable<HLWallet> generateWallet(Context context, String password, String mnemonics) {
        Flowable<String> flowable = Flowable.just(mnemonics);

        return flowable
                .map(s -> {
                    ECKeyPair keyPair = generateKeyPair(s);
                    WalletFile walletFile = Wallet.createLight(password, keyPair);
                    return new HLWallet();
                });
    }

    /**
     * generate key pair to create eth wallet
     * 生成KeyPair , 用于创建钱包
     */
    private ECKeyPair generateKeyPair(String mnemonics){
        // 1. we just need eth wallet for now
        AddressIndex addressIndex = BIP44
                .m()
                .purpose44()
                .coinType(60)
                .account(0)
                .external()
                .address(0);
        // 2. calculate seed from mnemonics , then get master/root key
        ExtendedPrivateKey rootKey = ExtendedPrivateKey.fromSeed(new SeedCalculator().calculateSeed(mnemonics, ""), Bitcoin.MAIN_NET);
        Logger.i("mnemonics:" + mnemonics);
        String extendedBase58 = rootKey.extendedBase58();
        Logger.i("extendedBase58:" + extendedBase58);

        // 3. get child private key deriving from master/root key
        ExtendedPrivateKey childPrivateKey = rootKey.derive(addressIndex, AddressIndex.DERIVATION);
        String childExtendedBase58 = childPrivateKey.extendedBase58();
        Logger.i("childExtendedBase58:" + childExtendedBase58);

        // 4. get key pair
        byte[] privateKeyBytes = childPrivateKey.getKey();
        ECKeyPair keyPair = ECKeyPair.create(privateKeyBytes);

        // we 've gotten what we need
        String privateKey = childPrivateKey.getPrivateKey();
        String publicKey = childPrivateKey.neuter().getPublicKey();
        String address = Keys.getAddress(keyPair);

        Logger.i("privateKey:"+privateKey);
        Logger.i("publicKey:"+publicKey);
        Logger.i("address:"+"0x"+address);

        return keyPair;
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
