package com.wwdablu.soumya.arphotogallery;

import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.ar.core.Anchor;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Trackable;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.List;

abstract class RenderHelper {

    protected RenderCallback mRenderCallback;
    protected TransformableNode transformableNode;

    RenderHelper(@Nullable RenderCallback callback) {
        mRenderCallback = callback;
    }

    protected abstract void rotateOnZAxis(boolean enable, long timeInMs);

    protected void addViewToFrame(@NonNull ViewRenderable viewRenderable, @NonNull ARGalleryFragment arGalleryFragment) {

        Frame frame = arGalleryFragment.getArSceneView().getArFrame();
        Point center = getScreenCenter(arGalleryFragment);

        if(frame != null) {
            List<HitResult> result = frame.hitTest(center.x, center.y);
            for(HitResult hit : result) {
                Trackable trackable = hit.getTrackable();
                if (trackable instanceof Plane && ((Plane) trackable).isPoseInPolygon(hit.getHitPose())) {
                    addNodeToScene(viewRenderable, hit.createAnchor(), arGalleryFragment);
                    break;
                }
            }
        }
    }

    private void addNodeToScene(@NonNull Renderable renderable, Anchor createAnchor, @NonNull ARGalleryFragment arGalleryFragment) {
        AnchorNode anchorNode = new AnchorNode(createAnchor);
        transformableNode = new TransformableNode(arGalleryFragment.getTransformationSystem());
        transformableNode.setRenderable(renderable);
        transformableNode.setParent(anchorNode);

        arGalleryFragment.getArSceneView().getScene().addChild(anchorNode);

        transformableNode.setOnTapListener((hitTestResult, motionEvent) -> {
            if(mRenderCallback != null) {
                mRenderCallback.onRenderableClicked();
            }
        });

        transformableNode.select();
        if(mRenderCallback != null) {
            mRenderCallback.onRenderableRendered();
        }
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
