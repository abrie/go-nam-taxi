package vision;

/**
 * Created by abrie on 15-10-03.
 */

import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;

/**
 * Factory for creating a tracker and associated graphic to be associated with a new barcode.  The
 * multi-processor uses this factory to create barcode trackers as needed -- one for each barcode.
 */
class BarcodeTrackerFactory implements MultiProcessor.Factory<Barcode> {
    private GraphicOverlay<BarcodeGraphic> mGraphicOverlay;
    private BarcodeTrackEventHandler barcodeTrackEventHandler;

    public interface BarcodeTrackEventHandler {
        void onBarcodeDetected(Barcode barcode);
    }

    BarcodeTrackerFactory(GraphicOverlay<BarcodeGraphic> barcodeGraphicOverlay, BarcodeTrackEventHandler handler) {
        mGraphicOverlay = barcodeGraphicOverlay;
        barcodeTrackEventHandler = handler;
    }

    @Override
    public Tracker<Barcode> create(Barcode barcode) {
        BarcodeGraphic graphic = new BarcodeGraphic(mGraphicOverlay);
        barcodeTrackEventHandler.onBarcodeDetected(barcode);
        return new BarcodeGraphicTracker(mGraphicOverlay, graphic);
    }

}
