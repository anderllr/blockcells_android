package br.com.blockcells.blockcells.funcs;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by anderson on 10/05/17.
 */

public class PrefTravado {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "br.com.blockcells.travado";

    private static final String IS_TRAVADO = "IsTravado";


    public PrefTravado(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setTravado(boolean isTravado) {
        editor.putBoolean(IS_TRAVADO, isTravado);
        editor.commit();
    }

    public boolean isTravado() {
        return pref.getBoolean(IS_TRAVADO, true);
    }
}
