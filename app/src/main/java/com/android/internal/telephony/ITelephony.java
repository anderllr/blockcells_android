package com.android.internal.telephony;

/**
 * Created by anderson on 18/11/16.
 */
public interface ITelephony  {
    boolean endCall();

    void answerRingingCall();

    void silenceRinger();
}
