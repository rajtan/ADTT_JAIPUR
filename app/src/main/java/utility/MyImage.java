package utility;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.rto_driving_test_rajasthan.R;

import java.io.File;

/**
 * Created by cnvg on 30/3/17.
 */

public class MyImage {


    public  static  void displayImage(Context mContext, String pictureUri, ImageView iv){
        Glide.with(mContext)
                .load(new File(pictureUri))
                .placeholder(R.drawable.home_icon)
                .into(iv);


    }
}
