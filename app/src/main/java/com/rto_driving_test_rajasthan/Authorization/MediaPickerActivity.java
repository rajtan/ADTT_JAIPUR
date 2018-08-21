package com.rto_driving_test_rajasthan.Authorization;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.rto_driving_test_rajasthan.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import utility.BaseActivity;

/**
 * Created by cnvg on 2/3/17.
 */

public abstract class MediaPickerActivity extends BaseActivity {
    public int IMAGE_DIMENSION = 640;

    protected static final int REQ_CODE_PICK_FROM_GALLERY = 1;
    protected static final int REQ_CODE_PICK_FROM_GALLERY_WITH_CROP = 2;

    protected static final int REQ_CODE_PICK_FROM_CAMERA = 3;
    protected static final int REQ_CODE_PICK_FROM_CAMERA_WITH_CROP = 4;

    protected static final int REQ_CODE_VIDEO_FROM_CAMERA = 5;
    protected static final int REQ_CODE_VIDEO_FROM_GALLERY = 6;

    protected static final int REQ_CODE_CROP_PHOTO = 7;
    final int REQ_CODE_READ_WRITE_PER = 8;
    protected static final int REQ_CODE_FILE_PICKER = 9;
    private static final String IMAGE_UNSPECIFIED = "image/*";
    private Uri capturedImageUri, cropImageUri;

    public enum MediaPicker {
        Gellery, GelleryWithCropper, Camera, CameraWithCropper, VideoCamera, VideoGallery, FilePicker

    }

    public MediaPicker mediaPicker;

    public void PickMedia(MediaPicker mp) {
        this.mediaPicker = mp;
        int permissionCheck1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck1 == 0) {
            if (mp == MediaPicker.Gellery) {
                openGallery(REQ_CODE_PICK_FROM_GALLERY);
            } else if (mp == MediaPicker.GelleryWithCropper) {
                openGallery(REQ_CODE_PICK_FROM_GALLERY_WITH_CROP);
            } else if (mp == MediaPicker.Camera) {
                openCamera(REQ_CODE_PICK_FROM_CAMERA);
            } else if (mp == MediaPicker.CameraWithCropper) {
                openCamera(REQ_CODE_PICK_FROM_CAMERA_WITH_CROP);
            } else if (mp == MediaPicker.FilePicker) {
                openFilePicker(REQ_CODE_FILE_PICKER);
            }
        } else {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest
                                .permission.WRITE_EXTERNAL_STORAGE},
                        REQ_CODE_READ_WRITE_PER);

            }

        }
    }

    public void PickVideo(MediaPicker mp, long duration) {
        if (mp == MediaPicker.VideoCamera) {

            openVideoCamera(duration);
        } else if (mp == MediaPicker.VideoGallery) {

            Intent intent = new Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("video/*");
            startActivityForResult(intent,
                    REQ_CODE_VIDEO_FROM_GALLERY);
            // openGallery(REQ_CODE_PICK_FROM_GALLERY_WITH_CROP);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initTmpUris() {

        File proejctDirectory = new File(
                Environment.getExternalStorageDirectory() + File.separator
                        + this.getResources().getString(
                        R.string.app_name));
        if (!proejctDirectory.exists()) {
            proejctDirectory.mkdir();
        } /*
         * else { // delete all old files for (File file :
		 * proejctDirectory.listFiles()) { if
		 * (file.getName().startsWith("tmp_")) { file.delete(); } } }
		 */
        File tempDirectory = new File(proejctDirectory, "temp");
        if (!tempDirectory.exists()) {
            tempDirectory.mkdir();
        } else {
            // delete all old files
            for (File file : tempDirectory.listFiles()) {
                if (file.getName().startsWith("tmp_")
                        || file.getName().startsWith("croped_")) {
                    file.delete();
                }
            }

        }
        capturedImageUri = Uri.fromFile(new File(tempDirectory, "tmp_"
                + 0 + ".jpg"));

        File extraOutputFile = new File(tempDirectory, "croped_"
                + String.valueOf(System.currentTimeMillis()) + ".jpg");

        extraOutputFile.setWritable(true);
        cropImageUri = Uri.fromFile(extraOutputFile);

    }

    @TargetApi(23)
    private void openCamera(int requestCode) {

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);

        if (permissionCheck == 0) {

            initTmpUris();
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    capturedImageUri);
            intent.putExtra("return-data", true);
            try {
                startActivityForResult(intent,
                        requestCode);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }

        } else {

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        requestCode);

            }
        }
        return;
    }

    protected void openFilePicker(int RequstCode) {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File"),
                    RequstCode);
        } catch (ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }

    }

    protected void openGallery(int RequstCode) {

        if (Build.VERSION.SDK_INT < 19) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(
                    Intent.createChooser(intent, "Complete action using"),
                    RequstCode);
        } else {

            Intent intent = new Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent,
                    RequstCode);
        }

    }

    @Override
    @TargetApi(23)
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQ_CODE_PICK_FROM_GALLERY: {

                boolean showRationale = shouldShowRequestPermissionRationale(permissions[0]);
                if (!showRationale && grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "Please go to app setting and set on permission", Toast
                            .LENGTH_LONG)
                            .show();

                }

                if (!showRationale && permissions.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PickMedia(MediaPicker.Gellery);

                }
            }
            break;
            case REQ_CODE_PICK_FROM_GALLERY_WITH_CROP: {

                boolean showRationale = shouldShowRequestPermissionRationale(permissions[0]);
                if (!showRationale && grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "SHOW DIALOG", Toast.LENGTH_LONG).show();

                }
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PickMedia(MediaPicker.GelleryWithCropper);
                }

            }
            break;
            case REQ_CODE_PICK_FROM_CAMERA: {

                boolean showRationale = shouldShowRequestPermissionRationale(permissions[0]);
                if (!showRationale && grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    //Toast.makeText(this, "SHOW DIALOG", Toast.LENGTH_LONG).show();

                }

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PickMedia(MediaPicker.Camera);
                }
            }
            break;

            case REQ_CODE_PICK_FROM_CAMERA_WITH_CROP: {

                boolean showRationale = shouldShowRequestPermissionRationale(permissions[0]);
                if (!showRationale && grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "SHOW DIALOG", Toast.LENGTH_LONG).show();

                }
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PickMedia(MediaPicker.CameraWithCropper);
                }
            }
            break;

            case REQ_CODE_READ_WRITE_PER: {

                boolean showRationale = shouldShowRequestPermissionRationale(permissions[0]);
                if (!showRationale && grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "SHOW DIALOG", Toast.LENGTH_LONG).show();

                }
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PickMedia(mediaPicker);
                }
            }
            break;

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    /**
     * pass time limit in seconds if you unlimited then pass 0
     */

    protected void openVideoCamera(long durationInSeconds) {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (0 != durationInSeconds) {
            intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, durationInSeconds);
        }
        intent.putExtra("EXTRA_VIDEO_QUALITY", 0);
        startActivityForResult(intent, REQ_CODE_VIDEO_FROM_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_PICK_FROM_CAMERA_WITH_CROP:
                if (resultCode == RESULT_OK) {
                    cropImage();
                } else {
                    onMediaPickCanceled(MediaPicker.CameraWithCropper);
                }
                break;
            case REQ_CODE_PICK_FROM_GALLERY_WITH_CROP:
                if (resultCode == RESULT_OK && data.getData() != null) {
                    initTmpUris();
                    capturedImageUri = data.getData();
                    cropImage();
                } else {
                    onMediaPickCanceled(MediaPicker.GelleryWithCropper);
                }
                break;
            case REQ_CODE_CROP_PHOTO:
                if (resultCode == RESULT_OK) {

                    String imagePath = cropImageUri.getPath();
                    File file = new File(imagePath);
                    String compressIMagePAth = saveCompressImage(get_Picture_bitmap(file));

                    onSingleImageSelected(REQ_CODE_CROP_PHOTO, file, imagePath,
                            get_Picture_bitmap(file),compressIMagePAth);

                } else {
                    onMediaPickCanceled(MediaPicker.Camera);
                }
                break;
            case REQ_CODE_VIDEO_FROM_CAMERA:
            case REQ_CODE_VIDEO_FROM_GALLERY:
                if (resultCode == RESULT_OK) {
                    Uri vid = data.getData();
                    onVideoCaptured(getVideoPathFromURI(vid));
                } else {
                    onMediaPickCanceled(MediaPicker.VideoCamera);
                }
                break;
            case REQ_CODE_PICK_FROM_GALLERY: {
                if (resultCode == RESULT_OK) {

                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    File file = new File(picturePath);
                    String compressIMagePAth = saveCompressImage(get_Picture_bitmap(file));

                    onSingleImageSelected(REQ_CODE_PICK_FROM_GALLERY, file, picturePath,
                            get_Picture_bitmap(file),compressIMagePAth);
                } else {
                    onMediaPickCanceled(MediaPicker.Gellery);
                }
            }
            break;

            case REQ_CODE_PICK_FROM_CAMERA: {

                if (resultCode == RESULT_OK) {

                    String imagePath = capturedImageUri.getPath();
                    File file = new File(imagePath);
                    String compressIMagePAth = saveCompressImage(get_Picture_bitmap(file));

                    onSingleImageSelected(REQ_CODE_PICK_FROM_CAMERA, file, imagePath,
                            get_Picture_bitmap(file),compressIMagePAth);

                } else {
                    onMediaPickCanceled(MediaPicker.Camera);
                }

            }
            break;
            case REQ_CODE_FILE_PICKER: {

                if (resultCode == RESULT_OK) {


                    String FilePath = data.getData().getPath();

                    File file = new File(FilePath);
                    String compressIMagePAth = saveCompressImage(get_Picture_bitmap(file));


                    onSingleImageSelected(REQ_CODE_FILE_PICKER, file, FilePath,
                            null,compressIMagePAth);

                }

            }
            break;

            default:
                break;
        }
    }
    private String saveCompressImage(Bitmap image) {

        File proejctDirectory = new File(
                Environment.getExternalStorageDirectory() + File.separator
                        + this.getResources().getString(
                        R.string.app_name));
        if (!proejctDirectory.exists()) {
            proejctDirectory.mkdir();
        } else {
            proejctDirectory.delete();
            proejctDirectory.mkdir();
        }


        File tempDirectory_ = new File(proejctDirectory, "temp");
        File tempDirectory = new File(tempDirectory_, System.currentTimeMillis() + ".jpg");


        if (tempDirectory == null) {
            Log.d("MEDIA PICKER",
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return "";
        }
        try {
            FileOutputStream fos = new FileOutputStream(tempDirectory);
            image.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d("MEDIA PICKER", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d("MEDIA PICKER", "Error accessing file: " + e.getMessage());
        }

        return tempDirectory.getAbsolutePath();
    }

    public String getRealPathFromURI(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = this.getContentResolver().query(contentUri, proj, null,
                    null, null);
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /*
     * This method use for Crop image taken from camera
     */
    private void cropImage() {
        // Use existing crop activity.
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(capturedImageUri, IMAGE_UNSPECIFIED);
        intent.putExtra("outputX", IMAGE_DIMENSION);
        intent.putExtra("outputY", IMAGE_DIMENSION);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropImageUri);

        startActivityForResult(intent, REQ_CODE_CROP_PHOTO);
    }

    public int IMAGE_DIMENSION_SQUARE = 640;

    public int IMAGE_DIMENSION_RECT_WIDTH = 1023;
    public int IMAGE_DIMENSION_RECT_HEIGHT = 335;

    public void cropImage(Uri URI__, boolean isRactanle) {
        // Use existing crop activity.
        initTmpUris();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(URI__, IMAGE_UNSPECIFIED);

        if (isRactanle) {

            intent.putExtra("outputX", IMAGE_DIMENSION_RECT_WIDTH);
            intent.putExtra("outputY", IMAGE_DIMENSION_RECT_HEIGHT);
            intent.putExtra("aspectX", 3);
            intent.putExtra("aspectY", 1);

        } else {
            intent.putExtra("outputX", IMAGE_DIMENSION_SQUARE);
            intent.putExtra("outputY", IMAGE_DIMENSION_SQUARE);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);

        }
        intent.putExtra("scale", true);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropImageUri);

        startActivityForResult(intent, REQ_CODE_CROP_PHOTO);
    }

    private String getVideoPathFromURI(Uri contentUri) {
        String videoPath = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null,
                null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            videoPath = cursor.getString(column_index);
        }
        cursor.close();
        return videoPath;
    }

    public Bitmap get_Picture_bitmap(File nFile) {
        long size_file = getFileSize(nFile);
        size_file = (size_file) / 1000;// in Kb now
        int ample_size = 1;
        if (size_file > 251 && size_file < 1500) {
            ample_size = 2;
        } else if (size_file >= 1500 && size_file < 4500) {
            ample_size = 4;
        } else if (size_file >= 4500 && size_file <= 8000) {
            ample_size = 8;
        } else if (size_file > 8000) {
            ample_size = 16;
        }
        Bitmap bitmap = null;
        BitmapFactory.Options bitoption = new BitmapFactory.Options();
        bitoption.inSampleSize = ample_size;
        Bitmap bitmapPhoto = BitmapFactory.decodeFile(nFile.getAbsolutePath(),
                bitoption);
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(nFile.getAbsolutePath());
        } catch (IOException e) {
            // Auto-generated catch block
            e.printStackTrace();
        }
        int orientation = exif
                .getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
        Matrix matrix = new Matrix();

        if ((orientation == 3)) {
            matrix.postRotate(180);

        } else if (orientation == 6) {
            matrix.postRotate(90);

        } else if (orientation == 8) {
            matrix.postRotate(270);

        } else {
            matrix.postRotate(0);

        }
        bitmap = Bitmap.createBitmap(bitmapPhoto, 0, 0, bitmapPhoto.getWidth(),
                bitmapPhoto.getHeight(), matrix, true);
        return bitmap;
    }

    public long getFileSize(final File file) {
        if (file == null || !file.exists())
            return 0;
        if (!file.isDirectory())
            return file.length();
        final List<File> dirs = new LinkedList<File>();
        dirs.add(file);
        long result = 0;
        while (!dirs.isEmpty()) {
            final File dir = dirs.remove(0);
            if (!dir.exists())
                continue;
            final File[] listFiles = dir.listFiles();
            if (listFiles == null || listFiles.length == 0)
                continue;
            for (final File child : listFiles) {
                result += child.length();
                if (child.isDirectory())
                    dirs.add(child);
            }
        }
        return result;
    }

    protected abstract void onSingleImageSelected(int starterCode,
                                                  File fileUri, String imagPath, Bitmap bitmap,String compressPath);

    protected abstract void onVideoCaptured(String videoPath);

    protected abstract void onMediaPickCanceled(MediaPicker reqCode);

}
