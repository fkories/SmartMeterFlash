package comfkoriessmartmeterflash.httpsgithub.smartmeterflash;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private CameraDevice myCamera;
    private CameraManager myCameraManager;
    private String[] myCameraIds;
    private String myRearCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void blink(View view) { //connected to the button
//        boolean hasLED = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
//        if (hasLED) {
//            getCamera();
//            //turnOnFlash();
//        }
        getCamera();
        checkForFlash();
        EditText pinText = (EditText) findViewById(R.id.pin);
        int number = Integer.parseInt(pinText.getText().toString());
//        doBlink(number);
        blinkPIN(getBlinks(number));
    }

    public void blinkOne(View view) {
        doBlink(1);
    }

    public void checkForFlash() {
        boolean hasFlash = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        if(!hasFlash) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Sorry, your device doesn't support flash light!")
                    .setTitle("Error");
            // Add the buttons
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private int[] getBlinks(int number) {
        //do someting
        int temp = number;
        int[] rtrn = new int[4];
        for (int i=0; temp > 0; i++) {
            rtrn[i] = (int)((double)temp % (double)10);
            temp = temp - rtrn[i];
            temp = (int)((double)temp / (double)10);
        }
        return rtrn;
    }

    private void blinkPIN(int[] number) {
        //prepare for pin submission with two blinks
        doBlink(2);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i=0; i<4; i++) {
            doBlink(number[3-i]);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void doBlink(int times) {
        if (myRearCamera != null) {
            try {
                for (int i = 0; i < times; i++) {
                    myCameraManager.setTorchMode(myRearCamera, true);
                    Thread.sleep(500);
                    myCameraManager.setTorchMode(myRearCamera, false);
                    Thread.sleep(500);
                }
            } catch (Exception e) {

            }
        }
    }

    private void getCamera() {

        if (myCamera == null) {
            try {
                myCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                myCameraIds = myCameraManager.getCameraIdList();
                myRearCamera = myCameraIds[0];
//                for (int i=0; i<5; i++) {
//                    myCameraManager.setTorchMode(myRearCamera, true);
//                    Thread.sleep(500);
//                    myCameraManager.setTorchMode(myRearCamera, false);
//                    Thread.sleep(500);
//                }

                //String cameraList = getCameraIdList();
                //myCamera = myCamera.open();
                //myCamera = myCamera.getParameters();
            } catch (Exception e) {

            }
        }
    }
}