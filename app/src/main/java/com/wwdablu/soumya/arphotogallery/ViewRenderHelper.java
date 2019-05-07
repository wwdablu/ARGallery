package com.wwdablu.soumya.arphotogallery;

import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.google.ar.core.Anchor;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Trackable;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.List;

final class ViewRenderHelper {

    private ViewRenderable mViewRenderable;
    private ViewRenderCallback mViewRenderCallback;

    public ViewRenderHelper(@Nullable ViewRenderCallback callback) {
        mViewRenderCallback = callback;
    }

    public boolean isViewContainerPresent() {
        return mViewRenderable != null;
    }

    public void showImage(@NonNull Context context,
                          @NonNull final String galleryPath,
                          @NonNull ARGalleryFragment arGalleryFragment) {

        if(!isViewContainerPresent()) {
            createViewRenderable(context, galleryPath, arGalleryFragment);
            return;
        }

        ((ImageView)mViewRenderable.getView().findViewById(R.id.iv_image_container))
                .setImageURI(Uri.parse(galleryPath));
    }

    private void createViewRenderable(@NonNull Context context,
                                      @NonNull final String galleryPath,
                                      @NonNull ARGalleryFragment arGalleryFragment) {

        ViewRenderable.builder()
            .setView(context, R.layout.gallery_image_renderer)
            .build()
            .thenAccept(viewRenderable -> {
                mViewRenderable = viewRenderable;
                ((ImageView)mViewRenderable.getView().findViewById(R.id.iv_image_container))
                    .setImageURI(Uri.parse(galleryPath));
                addViewToFrame(arGalleryFragment);
            });
    }

    private void addViewToFrame(@NonNull ARGalleryFragment arGalleryFragment) {

        Frame frame = arGalleryFragment.getArSceneView().getArFrame();
        Point center = getScreenCenter(arGalleryFragment);

        if(frame != null) {
            List<HitResult> result = frame.hitTest(center.x, center.y);
            for(HitResult hit : result) {
                Trackable trackable = hit.getTrackable();
                if (trackable instanceof Plane && ((Plane) trackable).isPoseInPolygon(hit.getHitPose())) {
                    addNodeToScene(hit.createAnchor(), arGalleryFragment);
                    break;
                }
            }
        }
    }

    private void addNodeToScene(Anchor createAnchor, @NonNull ARGalleryFragment arGalleryFragment) {
        AnchorNode anchorNode = new AnchorNode(createAnchor);
        TransformableNode transformableNode = new TransformableNode(arGalleryFragment.getTransformationSystem());
        transformableNode.setRenderable(mViewRenderable);
        transformableNode.setParent(anchorNode);

        arGalleryFragment.getArSceneView().getScene().addChild(anchorNode);

        transformableNode.setOnTapListener((hitTestResult, motionEvent) -> {
            if(mViewRenderCallback != null) {
                mViewRenderCallback.onViewClicked();
            }
        });

        transformableNode.select();
    }

    private Point getScreenCenter(@NonNull ARGalleryFragment arGalleryFragment) {

        if(arGalleryFragment.getView() == null) {
            return new Point(0,0);
        }

        int w = arGalleryFragment.getView().getWidth()/2;
        int h = arGalleryFragment.getView().getHeight()/2;
        return new Point(w, h);
    }
}
