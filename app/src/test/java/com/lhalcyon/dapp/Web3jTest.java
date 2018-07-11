package com.lhalcyon.dapp;

import org.junit.Before;
import org.junit.Test;

import java.text.Normalizer;
import java.util.concurrent.ExecutionException;


/**
 * <pre>
 * Create by  :    L
 * Create Time:    2018/7/4
 * Brief Desc :
 * </pre>
 */
public class Web3jTest {

    String ethNode = "https://mainnet.infura.io/CK6jy212yPqKCUrpJB7u";

    @Before
    public void setup() throws ExecutionException, InterruptedException {

    }

    @Test
    public void testNormal(){
        String mnemonic = "traffic buddy void three pink bronze radar rule science grab orbit surge";
        String normalize = Normalizer.normalize(mnemonic, Normalizer.Form.NFKD);
        System.out.println(normalize);
    }

    @Test
    public void testD(){
        byte[]  iBuf = new byte[4];
        System.out.println(iBuf);
    }
}
