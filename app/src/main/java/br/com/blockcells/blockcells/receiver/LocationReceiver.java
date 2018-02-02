package br.com.blockcells.blockcells.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import br.com.blockcells.blockcells.funcs.BlockService;

/**
 * Created by anderson on 14/12/16.
 */

public class LocationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
     //       Intent i = new Intent(context, ActPrincipal.class);
         //   i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          //  i.addFlags(Intent.);
        //    context.startActivity(i);
            Intent myIntent = new Intent(context, BlockService.class);
            context.startService(myIntent);

            Toast.makeText(context, "Iniciou no boot", Toast.LENGTH_SHORT).show();
        }

    }
}
