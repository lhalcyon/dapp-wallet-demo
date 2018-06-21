package com.lhalcyon.dapp.ui.base;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * <pre>
 * Create by  :    L
 * Create Time:    2018/4/17
 * Brief Desc :   viewpager fragment嵌套导致isVisibleToUser不一致
 * </pre>
 */
public class FragmentUserVisibleController {

    private static final String TAG = "UserVisibleController";
    public static boolean DEBUG = false;
    @SuppressWarnings("FieldCanBeLocal")
    private String fragmentName;
    private boolean waitingShowToUser;
    private Fragment fragment;
    private UserVisibleCallback userVisibleCallback;
    private List<OnUserVisibleListener> userVisibleListenerList;

    public FragmentUserVisibleController(Fragment fragment, UserVisibleCallback userVisibleCallback) {
        this.fragment = fragment;
        this.userVisibleCallback = userVisibleCallback;
        //noinspection ConstantConditions
        this.fragmentName = DEBUG ? fragment.getClass().getSimpleName() : null;
    }

    public void activityCreated() {
        if (DEBUG) {
            Log.d(TAG, fragmentName + ": activityCreated, userVisibleHint=" + fragment.getUserVisibleHint());
        }
        if (fragment.getUserVisibleHint()) {
            Fragment parentFragment = fragment.getParentFragment();
            if (parentFragment != null && !parentFragment.getUserVisibleHint()) {
                if (DEBUG) {
                    Log.d(TAG, fragmentName + ": activityCreated, parent " + parentFragment.getClass().getSimpleName() + " is hidden, therefore hidden self");
                }
                userVisibleCallback.setWaitingShowToUser(true);
                userVisibleCallback.callSuperSetUserVisibleHint(false);
            }
        }
    }

    public void resume() {
        if (DEBUG) {
            Log.d(TAG, fragmentName + ": resume, userVisibleHint=" + fragment.getUserVisibleHint());
        }
        if (fragment.getUserVisibleHint()) {
            userVisibleCallback.onVisibleToUserChanged(true, true);
            callbackListener(true, true);
            if (DEBUG) {
                Log.i(TAG, fragmentName + ": visibleToUser on resume");
            }
        }
    }

    public void pause() {
        if (DEBUG) {
            Log.d(TAG, fragmentName + ": pause, userVisibleHint=" + fragment.getUserVisibleHint());
        }
        if (fragment.getUserVisibleHint()) {
            userVisibleCallback.onVisibleToUserChanged(false, true);
            callbackListener(false, true);
            if (DEBUG) {
                Log.w(TAG, fragmentName + ": hiddenToUser on pause");
            }
        }
    }
    @SuppressLint("RestrictedApi")
    public void setUserVisibleHint(boolean isVisibleToUser) {
        Fragment parentFragment = fragment.getParentFragment();
        if (DEBUG) {
            String parent;
            if (parentFragment != null) {
                parent = "parent " + parentFragment.getClass().getSimpleName() + " userVisibleHint=" + parentFragment.getUserVisibleHint();
            } else {
                parent = "parent is null";
            }
            Log.d(TAG, fragmentName + ": setUserVisibleHint, userVisibleHint=" + isVisibleToUser + ", " + (fragment.isResumed() ? "resume" : "pause") + ", " + parent);
        }

        if (isVisibleToUser) {
            if (parentFragment != null && !parentFragment.getUserVisibleHint()) {
                if (DEBUG) {
                    Log.d(TAG, fragmentName + ": setUserVisibleHint, parent " + parentFragment.getClass().getSimpleName() + " is hidden, therefore hidden self");
                }
                userVisibleCallback.setWaitingShowToUser(true);
                userVisibleCallback.callSuperSetUserVisibleHint(false);
                return;
            }
        }

        if (fragment.isResumed()) {
            userVisibleCallback.onVisibleToUserChanged(isVisibleToUser, false);
            callbackListener(isVisibleToUser, false);
            if (DEBUG) {
                if (isVisibleToUser) {
                    Log.i(TAG, fragmentName + ": visibleToUser on setUserVisibleHint");
                } else {
                    Log.w(TAG, fragmentName + ": hiddenToUser on setUserVisibleHint");
                }
            }
        }

        if (fragment.getActivity() != null) {
            List<Fragment> childFragmentList = fragment.getChildFragmentManager().getFragments();
            if (isVisibleToUser) {
                // 显示待显示的子Fragment
                if (childFragmentList != null && childFragmentList.size() > 0) {
                    for (Fragment childFragment : childFragmentList) {
                        if (childFragment instanceof UserVisibleCallback) {
                            UserVisibleCallback userVisibleCallback = (UserVisibleCallback) childFragment;
                            if (userVisibleCallback.isWaitingShowToUser()) {
                                if (DEBUG) {
                                    Log.d(TAG, fragmentName + ": setUserVisibleHint, show child " + childFragment.getClass().getSimpleName());
                                }
                                userVisibleCallback.setWaitingShowToUser(false);
                                childFragment.setUserVisibleHint(true);
                            }
                        }
                    }
                }
            } else {
                // 隐藏正在显示的子Fragment
                if (childFragmentList != null && childFragmentList.size() > 0) {
                    for (Fragment childFragment : childFragmentList) {
                        if (childFragment instanceof UserVisibleCallback) {
                            UserVisibleCallback userVisibleCallback = (UserVisibleCallback) childFragment;
                            if (childFragment.getUserVisibleHint()) {
                                if (DEBUG) {
                                    Log.d(TAG, fragmentName + ": setUserVisibleHint, hidden child " + childFragment.getClass().getSimpleName());
                                }
                                userVisibleCallback.setWaitingShowToUser(true);
                                childFragment.setUserVisibleHint(false);
                            }
                        }
                    }
                }
            }
        }
    }

    private void callbackListener(boolean isVisibleToUser, boolean invokeInResumeOrPause) {
        if (userVisibleListenerList != null && userVisibleListenerList.size() > 0) {
            for (OnUserVisibleListener listener : userVisibleListenerList) {
                listener.onVisibleToUserChanged(isVisibleToUser, invokeInResumeOrPause);
            }
        }
    }

    /**
     * 当前Fragment是否对用户可见
     */
    @SuppressWarnings("unused")
    public boolean isVisibleToUser() {
        return fragment.isResumed() && fragment.getUserVisibleHint();
    }

    public boolean isWaitingShowToUser() {
        return waitingShowToUser;
    }

    public void setWaitingShowToUser(boolean waitingShowToUser) {
        this.waitingShowToUser = waitingShowToUser;
    }

    public void addOnUserVisibleListener(OnUserVisibleListener listener) {
        if (listener != null) {
            if (userVisibleListenerList == null) {
                userVisibleListenerList = new LinkedList<>();
            }
            userVisibleListenerList.add(listener);
        }
    }

    public void removeOnUserVisibleListener(OnUserVisibleListener listener) {
        if (listener != null && userVisibleListenerList != null) {
            userVisibleListenerList.remove(listener);
        }
    }

    public interface UserVisibleCallback {
        boolean isWaitingShowToUser();

        void setWaitingShowToUser(boolean waitingShowToUser);

        boolean isVisibleToUser();

        void callSuperSetUserVisibleHint(boolean isVisibleToUser);

        void onVisibleToUserChanged(boolean isVisibleToUser, boolean invokeInResumeOrPause);
    }

    public interface OnUserVisibleListener {
        void onVisibleToUserChanged(boolean isVisibleToUser, boolean invokeInResumeOrPause);
    }
}


