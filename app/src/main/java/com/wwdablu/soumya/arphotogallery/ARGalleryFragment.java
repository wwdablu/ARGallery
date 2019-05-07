package com.wwdablu.soumya.arphotogallery;

import com.google.ar.core.Frame;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.ux.ArFragment;

import java.util.LinkedList;

public class ARGalleryFragment extends ArFragment implements ViewRenderCallback {

    private ViewRenderHelper viewRenderHelper;
    private LinkedList<String> galleryImagePaths;
    private int currentIndex;

    @Override
    public void onResume() {
        super.onResume();
        getPlaneDiscoveryController().hide();
        getPlaneDiscoveryController().setInstructionView(null);

        if(viewRenderHelper == null) {
            viewRenderHelper = new ViewRenderHelper(this);
        }

        galleryImagePaths = GalleryUtil.getAllImages(getActivity());
        getArSceneView().getScene().addOnUpdateListener(updateListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        removeUpdateListener();
    }

    @Override
    public void onViewClicked() {
        showImage();
    }

    private void removeUpdateListener() {
        getArSceneView().getScene().removeOnUpdateListener(updateListener);
    }

    private void showImage() {
        viewRenderHelper.showImage(getActivity(), getNextImage(), this);
    }

    private String getNextImage() {

        int size = galleryImagePaths.size();
        if(size == 0) {
            return "";
        }

        if(currentIndex >= size) {
            currentIndex = 0;
        }

        return galleryImagePaths.get(currentIndex++);
    }

    private Scene.OnUpdateListener updateListener = frameTime -> {

        Frame frame = getArSceneView().getArFrame();
        if(frame == null) {
            return;
        }

        for(Plane plane : frame.getUpdatedTrackables(Plane.class)) {
            removeUpdateListener();
            showImage();
            break;
        }
    };
}
