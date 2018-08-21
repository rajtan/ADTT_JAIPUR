package utility;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.ArrayAdapter;

import com.rto_driving_test_rajasthan.Adapters.OnItemClickAdapter;

import java.util.ArrayList;



/**
 * Created by PankajRana on 11/9/2016.
 */

public class DialogUtill {

    public static void showListSelection(Context context, int titleResId, final
    OnItemClickAdapter onItemClickAdapter, final ArrayList<String> items) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if (0 != titleResId) {
            builder.setTitle(titleResId);
        }
//        ArrayList<String> list=new ArrayList<String>();
//        for (int k=0;k<items.size();k++){
//            if (items.get(k) instanceof ClassGradeModel){
//                ClassGradeModel cm=((ClassGradeModel)items.get(k));
//                list.add(cm.grade);
//            }
//
//
//        }
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                context, android.R.layout.simple_list_item_1);
        arrayAdapter.addAll(items);

        builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int position) {
                String item = arrayAdapter.getItem(position);
                if (null != onItemClickAdapter) {
                    onItemClickAdapter.click(position, null, position);

                }

            }
        });

        AlertDialog dialog = builder.create();

        builder.show();

    }

}
