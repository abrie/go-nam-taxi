package na.nbii.tillapp;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

/**
 * Created by abrie on 15-10-04.
 */
public class SoundEffects {
    SoundPool soundPool;
    int successSound;
    int failureSound;

    public SoundEffects(Context context) {
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        failureSound = soundPool.load(context, R.raw.failure, 1);
        successSound = soundPool.load(context, R.raw.success, 2);
    }

    public void signalSuccess() {
        soundPool.play(successSound, 1, 1, 0, 0, 1);
    }

    public void signalError() {
        soundPool.play(failureSound, 1, 1, 0, 0, 1);
    }
}
