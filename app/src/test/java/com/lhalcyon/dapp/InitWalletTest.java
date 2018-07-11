package com.lhalcyon.dapp;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import org.junit.Before;
import org.junit.Test;
import org.web3j.utils.Numeric;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import io.github.novacrypto.bip32.ExtendedPrivateKey;
import io.github.novacrypto.bip32.networks.Bitcoin;
import io.github.novacrypto.bip39.SeedCalculator;
import io.github.novacrypto.bip44.AddressIndex;
import io.github.novacrypto.bip44.BIP44;

/**
 * <pre>
 * Create by  :    L
 * Create Time:    2018/6/19
 * Brief Desc :
 * </pre>
 */
public class InitWalletTest {

    String mnemonics = "traffic buddy void three pink bronze radar rule science grab orbit surge";

    @Before
    public void setup() {
        PrettyFormatStrategy strategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(true)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(0)         // (Optional) How many method line to show. Default 2
                .methodCount(2)
                .tag("halcyon")
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(strategy));
    }

    @Test
    public void testCreateWallet() {
        System.out.println(mnemonics);
        byte[] seed = new SeedCalculator().calculateSeed(mnemonics, "");
        for (byte b : seed) {
            int i = b & 0xff;
            System.out.print(i + ",");
        }
        System.out.println("\n");
        String s = toHexString(seed, 0, seed.length, true);
        System.out.println(s);
    }

    private String toHexString(byte[] input, int offset, int length, boolean withPrefix) {
        StringBuilder stringBuilder = new StringBuilder();
        if (withPrefix) {
            stringBuilder.append("0x");
        }
        for (int i = offset; i < offset + length; i++) {
            String s = String.format("%02x", input[i] & 0xFF);
            stringBuilder.append(s);
        }

        return stringBuilder.toString();
    }

    @Test
    public void testAddressIndex() {
        AddressIndex addressIndex = BIP44
                .m()
                .purpose44()
                .coinType(60)
                .account(0)
                .external()
                .address(0);
        System.out.println(addressIndex.toString());
    }

    @Test
    public void testHmacSha512() throws Exception {
        String HMAC_SHA512 = "HmacSHA512";
        byte[] bytes = new SeedCalculator().calculateSeed(mnemonics, "");
        final Mac hmacSha512 = Mac.getInstance(HMAC_SHA512);
        final SecretKeySpec keySpec = new SecretKeySpec("Bitcoin seed".getBytes(), HMAC_SHA512);
        hmacSha512.init(keySpec);
        byte[] aFinal = hmacSha512.doFinal(bytes);
        System.out.println(aFinal);
        String s = Numeric.toHexString(aFinal);
        System.out.println(s);


    }

    @Test
    public void testPri() {
        byte[] seed = new SeedCalculator().calculateSeed(mnemonics, "");
        ExtendedPrivateKey privateKey = ExtendedPrivateKey.fromSeed(seed, Bitcoin.MAIN_NET);
        System.out.println(123);
    }
}
