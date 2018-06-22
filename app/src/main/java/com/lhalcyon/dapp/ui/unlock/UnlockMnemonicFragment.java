package com.lhalcyon.dapp.ui.unlock;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;

import com.lhalcyon.dapp.R;
import com.lhalcyon.dapp.bindlite.Perform;
import com.lhalcyon.dapp.config.Tag;
import com.lhalcyon.dapp.databinding.FragmentUnlockMnemonicsBinding;
import com.lhalcyon.dapp.manager.InitWalletManager;
import com.lhalcyon.dapp.model.HLWallet;
import com.lhalcyon.dapp.stuff.HLError;
import com.lhalcyon.dapp.stuff.HLSubscriber;
import com.lhalcyon.dapp.stuff.ScheduleCompat;
import com.lhalcyon.dapp.ui.MainActivity;
import com.lhalcyon.dapp.ui.base.BaseFragment;

import java.util.concurrent.TimeUnit;

import io.github.novacrypto.bip39.MnemonicValidator;
import io.github.novacrypto.bip39.Validation.InvalidChecksumException;
import io.github.novacrypto.bip39.Validation.InvalidWordCountException;
import io.github.novacrypto.bip39.Validation.UnexpectedWhiteSpaceException;
import io.github.novacrypto.bip39.Validation.WordNotFoundException;
import io.github.novacrypto.bip39.wordlists.English;
import io.reactivex.Flowable;

/**
 * <pre>
 * Create by  :    L
 * Create Time:    2018/6/22
 * Brief Desc :
 * </pre>
 */
public class UnlockMnemonicFragment extends BaseFragment<FragmentUnlockMnemonicsBinding> {

    public final Perform<View> call = new Perform<>(view -> {
        switch (view.getId()) {
            case R.id.btn_unlock:
                attemptUnlock();
                break;
            default:
                break;
        }
    });

    private void attemptUnlock() {
        String mnemonics = mBinding.etMnemonic.getText().toString().trim();
        String password = mBinding.etPassword.getText().toString().trim();
        String repassword = mBinding.etRePassword.getText().toString().trim();

        Flowable
                .just(mnemonics)
                .filter(s -> validateInput(s,password,repassword))
                .flatMap(s -> InitWalletManager.shared().importMnemonic(mContext,password,s))
                .compose(ScheduleCompat.apply())
                .subscribe(new HLSubscriber<HLWallet>(mContext,true) {
                    @Override
                    protected void success(HLWallet data) {
                        Snackbar.make(mBinding.getRoot(),"Unlock Success",Snackbar.LENGTH_LONG).show();
                        Flowable.just(1)
                                .delay(2000, TimeUnit.MILLISECONDS)
                                .compose(ScheduleCompat.apply())
                                .subscribe(integer -> {
                                    startActivity(new Intent(mContext,MainActivity.class).putExtra(Tag.INDEX,1));
                                    getActivity().finish();
                                });
                    }

                    @Override
                    protected void failure(HLError error) {
                        Snackbar.make(mBinding.getRoot(),error.getMessage(),Snackbar.LENGTH_LONG)
                                .show();
                    }
                });
    }

    private boolean validateInput(String mnemonics, String password, String repassword) {
        // validate empty
        if (TextUtils.isEmpty(mnemonics) || TextUtils.isEmpty(password) || TextUtils.isEmpty(repassword)){
            ScheduleCompat.snackInMain(mBinding.getRoot(),"please input data");
            return false;
        }
        // validate password
        if (!TextUtils.equals(password,repassword)){
            ScheduleCompat.snackInMain(mBinding.getRoot(),"password does not match");
            return false;
        }
        // validate mnemonic
        try {
            MnemonicValidator.ofWordList(English.INSTANCE).validate(mnemonics);
        } catch (InvalidChecksumException e) {
            e.printStackTrace();
            ScheduleCompat.snackInMain(mBinding.getRoot(),e.getMessage());
            return false;
        } catch (InvalidWordCountException e) {
            e.printStackTrace();
            ScheduleCompat.snackInMain(mBinding.getRoot(),e.getMessage());
            return false;
        } catch (WordNotFoundException e) {
            e.printStackTrace();
            ScheduleCompat.snackInMain(mBinding.getRoot(),e.getMessage());
            return false;
        } catch (UnexpectedWhiteSpaceException e) {
            e.printStackTrace();
            ScheduleCompat.snackInMain(mBinding.getRoot(),e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mBinding.setCall(call);
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_unlock_mnemonics;
    }
}
