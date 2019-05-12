package com.wwdablu.soumya.arphotogallery;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.ar.core.Frame;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.ux.ArFragment;
import com.wwdablu.soumya.arphotogallery.renderers.RenderHelper;
import com.wwdablu.soumya.arphotogallery.renderers.ShapeRendererHelper;
import com.wwdablu.soumya.arphotogallery.renderers.ViewRenderHelper;

import java.util.LinkedList;

public class ARGalleryFragment extends ArFragment implements RenderCallback {

    public enum RenderBy {
        Plane,
        Shape
    }

    private RenderHelper renderHelper;
    private LinkedList<String> galleryImagePaths;
    private RenderBy renderBy;

    private int currentIndex;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        renderBy = RenderBy.Shape;
    }

    @Override
    public void onResume() {
        super.onResume();
        getPlaneDiscoveryController().hide();
        getPlaneDiscoveryController().setInstructionView(null);

        switch (renderBy) {

            case Plane:
                if(renderHelper == null) {
                    renderHelper = new ViewRenderHelper(this);
                }
                break;

            case Shape:
                default:
                    if(renderHelper == null) {
                        renderHelper = new ShapeRendererHelper(ShapeRendererHelper.Shape.Cube, this);
                    }
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
    public void onRenderableClicked() {

        switch (renderBy) {
            case Plane:
                showImage();
                break;

            case Shape:
                showShapeImage();
                break;
        }

    }

    @Override
    public void onRenderableRendered() {
        //
    }

    public void rotate(boolean enable) {
        renderHelper.rotateOnZAxis(enable, 5000);
    }

    private void removeUpdateListener() {
        getArSceneView().getScene().removeOnUpdateListener(updateListener);
    }

    private void showImage() {
        ((ViewRenderHelper)renderHelper).showImage(getActivity(), getNextImage(), this);
    }

    private void showShapeImage() {
        ((ShapeRendererHelper) renderHelper).showShape(getActivity(), this, getNextImage());
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

            switch (renderBy) {
                case Plane:
                    showImage();
                    break;

                case Shape:
                default:
                    showShapeImage();
            }

            break;
        }
    };
}
