package com.djzhao.smarttool.util.torch;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.util.Log;

public class TorchUtils {
    public static boolean IS_TORCH_ON = false;
    private static CameraManager manager;
    private static Camera camera;

    public static void openFlash(Context context, boolean isFront) {
        try {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            // if (Build.VERSION.SDK_INT > 30) {
                manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
                if (manager != null) {
                    if (isFront) {
                        manager.setTorchMode("1", true);
                    } else {
                        manager.setTorchMode("0", true);
                    }
                }
            } else {
                if (isFront) {
                    camera = Camera.open(1);
                } else {
                    camera = Camera.open();
                }
                camera.startPreview();
                Camera.Parameters parameters = camera.getParameters();
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
                camera.setParameters(parameters);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeFlash(boolean isFront) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        // if (Build.VERSION.SDK_INT >= 30) {
            try {
                if (manager == null) {
                    return;
                }
                if (isFront) {
                    manager.setTorchMode("1", false);
                } else {
                    manager.setTorchMode("0", false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (camera == null) {
                return;
            }
            camera.getParameters().setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(camera.getParameters());
            camera.stopPreview();
            camera.setPreviewCallback(null);
            camera.release();
            camera = null;
        }
    }

    public static int frontFlashLightId() {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                try {
                    String[] ids = manager.getCameraIdList();
                    if (ids.length > 1) {
                        String id = ids[1];
                        CameraCharacteristics chars = manager.getCameraCharacteristics(id);
                        boolean isFlashAvailable = chars.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                        if (isFlashAvailable) {
                            return 1;
                        }
                    }
                } catch (Exception e) {
                    Log.e("dj", e.getMessage());
                }
            } else {
                Camera.getCameraInfo(i, cameraInfo);
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    return i;
                }
            }
        }
        return -1;
    }
}
