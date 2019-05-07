package com.wwdablu.soumya.arphotogallery;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.QuaternionEvaluator;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ViewRenderable;

final class ViewRenderHelper extends RenderHelper {

    private ViewRenderable mViewRenderable;
    private ObjectAnimator orbitAnimation;

    public ViewRenderHelper(@Nullable RenderCallback callback) {
        super(callback);
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

        if(mRenderCallback != null) {
            mRenderCallback.onRenderableRendered();
        }
    }

    @Override
    protected void rotateOnZAxis(boolean enable, long timeInMs) {

        if(enable && orbitAnimation == null) {
            Quaternion orientation1 = Quaternion.axisAngle(new Vector3(0.0f, 1.0f, 0.0f), 0);
            Quaternion orientation2 = Quaternion.axisAngle(new Vector3(0.0f, 1.0f, 0.0f), 120);
            Quaternion orientation3 = Quaternion.axisAngle(new Vector3(0.0f, 1.0f, 0.0f), 240);
            Quaternion orientation4 = Quaternion.axisAngle(new Vector3(0.0f, 1.0f, 0.0f), 360);

            orbitAnimation = new ObjectAnimator();
            orbitAnimation.setObjectValues(orientation1, orientation2, orientation3, orientation4);

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
                addViewToFrame(mViewRenderable, arGalleryFragment);
            });
    }
}
