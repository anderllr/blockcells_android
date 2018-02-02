package br.com.blockcells.blockcells.funcs;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by anderson on 10/05/17.
 */

public class PrefSpeed {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "br.com.blockcells.speed";

    private static final String IS_SPEED_CHANGED = "IsSpeedChanged";

    public PrefSpeed(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setSpeedChanged(boolean isSpeedChanged) {
        editor.putBoolean(IS_SPEED_CHANGED, isSpeedChanged);
        editor.commit();
    }

    public boolean isSpeedChanged() {
        return pref.getBoolean(IS_SPEED_CHANGED, true);
    }

}
