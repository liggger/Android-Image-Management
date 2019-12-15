package uk.ac.shef.oak.storeimage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import pl.aprilapps.easyphotopicker.ChooserType;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.aprilapps.easyphotopicker.MediaFile;
import pl.aprilapps.easyphotopicker.MediaSource;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    private ImageViewModel imageViewModel;
    private int REQUEST_CODE_PERMISSIONS = 101;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};
    private Activity activity;
    private EasyImage easyImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity= this;
        imageView = (ImageView) findViewById(R.id.imageView);

        // Convert the bitmap to byte[]
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.p_2, null);
        byte[] a = getBitmapAsByteArray(bitmap);

        imageViewModel = ViewModelProviders.of(this).get(ImageViewModel.class);
        getImage(imageViewModel);

        System.out.println(allPermissionsGranted());
        if(allPermissionsGranted()){

            initEasyImage();
            System.out.println("yes");
        } else{
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }

//        initEasyImage();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_camera);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                EasyImage.openCamera(getActivity(), 0);
                easyImage.openCameraForImage(MainActivity.this);
            }
        });
    }

    public void insertImage(ImageViewModel imageViewModel, byte[] a) {
        Image image = new Image(a);
        imageViewModel.insertOneImage(image);
    }

    public void getImage(ImageViewModel imageViewModel) {
        LiveData<Image> image = imageViewModel.getImageToDisplay();
        imageViewModel.getImageToDisplay().observe(this, new Observer<Image>() {
            @Override
            public void onChanged(Image image) {
                if (image == null) {
                    System.out.println("NO");
                } else {
                    byte[] a = image.getImage();
                    Bitmap test = BitmapFactory.decodeByteArray(a, 0, a.length);
                    System.out.println("YES");
                    imageView.setImageBitmap(test);
                }
            }
        });
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }


    private void initEasyImage() {
        easyImage = new EasyImage.Builder(this)
                .setChooserTitle("Pick media")
                .setCopyImagesToPublicGalleryFolder(false)
//                .setChooserType(ChooserType.CAMERA_AND_DOCUMENTS)
                .setChooserType(ChooserType.CAMERA_AND_GALLERY)
                .setFolderName("EasyImage sample")
                .allowMultiple(true)
                .build();
    }

//    private void initEasyImage() {
//        EasyImage.configuration(this)
//                .setImagesFolderName("EasyImage sample")
//                .setCopyTakenPhotosToPublicGalleryAppFolder(true)
//                .setCopyPickedImagesToPublicGalleryAppFolder(false)
//                .setAllowMultiplePickInGallery(true);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        easyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onMediaFilesPicked(MediaFile[] imageFiles, MediaSource source) {
                for (MediaFile imageFile : imageFiles) {
                    Log.d("EasyImage", "Image file returned: " + imageFile.getFile().toString());
                }
                Bitmap bitmap= BitmapFactory.decodeFile(imageFiles[0].getFile().getAbsolutePath());
                Bitmap scaleBitmap = scaleBitmap(bitmap, 0.25f);
                imageView.setImageBitmap(scaleBitmap);
                byte[] a = getBitmapAsByteArray(bitmap);
//                insertImage(imageViewModel, a);
            }

            @Override
            public void onImagePickerError(@NonNull Throwable error, @NonNull MediaSource source) {
                //Some error handling
                error.printStackTrace();
            }

            @Override
            public void onCanceled(@NonNull MediaSource source) {
                //Not necessary to remove any files manually anymore
            }
        });

//        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
//            @Override
//            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
//                //Some error handling
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onImagesPicked(List<File> imageFiles, EasyImage.ImageSource source, int type) {
//                System.out.println(imageFiles.size());
//                System.out.println(imageFiles);
//                Bitmap bitmap= BitmapFactory.decodeFile(imageFiles.get(0).getAbsolutePath());
//                Bitmap scaleBitmap = scaleBitmap(bitmap, 0.25f);
//                imageView.setImageBitmap(scaleBitmap);
//                byte[] a = getBitmapAsByteArray(bitmap);
////                insertImage(imageViewModel, a);
//            }
//
//            @Override
//            public void onCanceled(EasyImage.ImageSource source, int type) {
//                //Cancel handling, you might wanna remove taken photo if it was canceled
//                if (source == EasyImage.ImageSource.CAMERA) {
//                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(getActivity());
//                    if (photoFile != null) photoFile.delete();
//                }
//            }
//        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE_PERMISSIONS){
            if(allPermissionsGranted()){
            } else{
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private boolean allPermissionsGranted(){

        for(String permission : REQUIRED_PERMISSIONS){
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    public Activity getActivity() {
        return activity;
    }

    private Bitmap scaleBitmap(Bitmap origin, float ratio) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(ratio, ratio);
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (newBM.equals(origin)) {
            return newBM;
        }
        origin.recycle();
        return newBM;
    }

}
