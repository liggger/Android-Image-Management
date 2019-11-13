package com.liggger.imagemanagement.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.liggger.imagemanagement.model.Image;

public class ImageViewModel extends AndroidViewModel {
    private ImageRepository imageRepository;

    private LiveData<Image> imageToDisplay;

    public ImageViewModel(@NonNull Application application) {
        super(application);
        imageRepository = new ImageRepository(application);
        imageToDisplay = imageRepository.getOneImage();
    }

    public LiveData<Image> getImageToDisplay() {
        if (imageToDisplay == null) {
            imageToDisplay = new MutableLiveData<Image>();
        }
        return imageToDisplay;
    }

    public void insertOneImage(Image image) {
        imageRepository.insertOneImage(image);
    }
    public void generateNewImage() {
        imageRepository.generateNewImage();
    }

}
