package utility;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;


import com.rto_driving_test_rajasthan.R;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by prashantm on 3/18/2016.
 */
public class ErrorLayout {
    public static final long LENGTH_SHORT = 1500L;
    public static final long LENGTH_LONG = 5500L;

    public TextView mErrorTv;

    private Animation mMoveUpAnim, mMoveDownAnim;
    private final Handler handler = new Handler();
    private AtomicBoolean mErrorShown;
    Vibrator vVibrator;


    public enum MsgType {
        Error, Success, Info, Warning
    }



    public ErrorLayout(View view) {
        mErrorTv = (TextView) view.findViewById(R.id.error_tv);
        vVibrator = (Vibrator) view.getContext().getSystemService(Context.VIBRATOR_SERVICE);
        mErrorTv.setVisibility(View.GONE);
        mMoveUpAnim = AnimationUtils.loadAnimation(view.getContext(), R.anim.move_up);
        mMoveDownAnim = AnimationUtils.loadAnimation(view.getContext(), R.anim.move_down);
        // FontLoader.setRobotoMediumTypeface(mErrorTv,mErrorTv1);
        mErrorShown = new AtomicBoolean(false);
    }


    public void showAlert(String error, MsgType msgType) {

        mErrorTv.setVisibility(View.VISIBLE);
        mErrorTv.setText(error);
        int color = 0;
        switch (msgType) {
            case Error:
                color = Color.RED;
                break;
            case Success:

                color = Color.GREEN;
                break;
            case Info:
                color = Color.DKGRAY;
                break;
            case Warning:
                color = Color.YELLOW;
                break;

            default:
                color = Color.RED;
                break;
        }
        mErrorTv.setBackgroundColor(color);
        if (!mErrorShown.get()) {
            mErrorShown.set(true);
            vVibrator.vibrate(150);
            mErrorTv.startAnimation(mMoveDownAnim);
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    mErrorTv.startAnimation(mMoveUpAnim);
                    mErrorShown.set(false);

                    if (null != mErrorLayoutListener) {
                        mErrorLayoutListener.onErrorHidden();
                    }
                }
            }, 100 <= error.length() ? LENGTH_LONG : LENGTH_SHORT);
        }

    }


    public void showError(String error) {

        mErrorTv.setVisibility(View.VISIBLE);
        mErrorTv.setText(error);

        mErrorTv.setBackgroundColor(Color.RED);
        if (!mErrorShown.get()) {
            mErrorShown.set(true);
            vVibrator.vibrate(150);
            mErrorTv.startAnimation(mMoveDownAnim);
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    mErrorTv.startAnimation(mMoveUpAnim);
                    mErrorShown.set(false);

                    if (null != mErrorLayoutListener) {
                        mErrorLayoutListener.onErrorHidden();
                    }
                }
            }, 100 <= error.length() ? LENGTH_LONG : LENGTH_SHORT);
        }

    }


    private ErrorLayoutListener mErrorLayoutListener;

    public void setErrorLayoutListener(ErrorLayoutListener listener) {
        this.mErrorLayoutListener = listener;
    }

    public interface ErrorLayoutListener {
        void onErrorShown();

        void onErrorHidden();
    }
}
