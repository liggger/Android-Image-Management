package uk.ac.shef.oak.storeimage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    private ImageViewModel imageViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imageView);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.p_2, null);
        byte[] a = getBitmapAsByteArray(bitmap);
//        imageView.setImageBitmap(bitmap);

        imageViewModel = ViewModelProviders.of(this).get(ImageViewModel.class);
//        insertImage(imageViewModel, a);
        getImage(imageViewModel);

//        Bitmap test = getImage(imageViewModel);
//        imageView.setImageBitmap(test);
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
        System.out.println(image.getValue());
//        byte[] a = image.getValue().getImage();
//        System.out.println(image.getValue().getImage_id());
//        return BitmapFactory.decodeByteArray(a, 0, a.length);
    }
    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }
}
