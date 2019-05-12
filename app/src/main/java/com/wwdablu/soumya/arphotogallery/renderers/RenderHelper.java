package com.wwdablu.soumya.arphotogallery.renderers;

import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.animation.LinearInterpolator;

import com.google.ar.core.Anchor;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Trackable;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.QuaternionEvaluator;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.ux.TransformableNode;
import com.wwdablu.soumya.arphotogallery.ARGalleryFragment;
import com.wwdablu.soumya.arphotogallery.RenderCallback;

import java.util.List;

public abstract class RenderHelper {

    RenderCallback mRenderCallback;
    private TransformableNode transformableNode;

    private ObjectAnimator orbitAnimation;

    RenderHelper(@Nullable RenderCallback callback) {
        mRenderCallback = callback;
    }

    public abstract void rotateOnZAxis(boolean enable, long timeInMs);

    void rotateOnZAxis(boolean enable, long timeInMs, Quaternion... quaternionArray) {
        if(enable && orbitAnimation == null) {

            orbitAnimation = new ObjectAnimator();
            orbitAnimation.setObjectValues((Object[]) quaternionArray);

            // Next, give it the localRotation property.
            orbitAnimation.setPropertyName("localRotation");

            // Use Sceneform's QuaternionEvaluator.
            orbitAnimation.setEvaluator(new QuaternionEvaluator());

            //  Allow orbitAnimation to repeat forever
            orbitAnimation.setRepeatCount(ObjectAnimator.INFINITE);
            orbitAnimation.setRepeatMode(ObjectAnimator.RESTART);
            orbitAnimation.setInterpolator(new LinearInterpolator());
            orbitAnimation.setAutoCancel(true);

            orbitAnimation.setTarget(transformableNode);
            orbitAnimation.setDuration(timeInMs);
        }

        if(enable) {
            orbitAnimation.start();
        } else {
            orbitAnimation.cancel();
        }
    }

    protected void addViewToFrame(@NonNull Renderable renderable, @NonNull ARGalleryFragment arGalleryFragment) {

        Frame frame = arGalleryFragment.getArSceneView().getArFrame();
        Point center = getScreenCenter(arGalleryFragment);

        if(frame != null) {
            List<HitResult> result = frame.hitTest(center.x, center.y);
            for(HitResult hit : result) {
                Trackable trackable = hit.getTrackable();
                if (trackable instanceof Plane && ((Plane) trackable).isPoseInPolygon(hit.getHitPose())) {
                    addNodeToScene(renderable, hit.createAnchor(), arGalleryFragment);
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
