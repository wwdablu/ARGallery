package com.wwdablu.soumya.arphotogallery.renderers;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.rendering.Texture;
import com.wwdablu.soumya.arphotogallery.ARGalleryFragment;
import com.wwdablu.soumya.arphotogallery.RenderCallback;

public class ShapeRendererHelper extends RenderHelper {

    private static final float vectorCenterX = 0.0f; //x left/right
    private static final float vectorCenterY = 0.5f; //y height
    private static final float vectorCenterZ = 0.5f; //z far

    public enum Shape {
        Cube
    }

    private Shape shape;
    private ModelRenderable modelRenderable;

    public ShapeRendererHelper(Shape shape, @Nullable RenderCallback callback) {
        super(callback);
        this.shape = shape;
    }

    public void showShape(@NonNull Context context,
                          @NonNull ARGalleryFragment arGalleryFragment,
                          @NonNull String imagePath) {

        Texture.builder()
            .setSource(context, Uri.parse(imagePath))
            .build()
            .thenAccept(texture -> MaterialFactory.makeOpaqueWithTexture(context, texture)
                .thenAccept(material -> {

                    modelRenderable = ShapeFactory.makeCube(
                            new Vector3(0.5f, 0.5f, 0.5f),
                            new Vector3(vectorCenterX, vectorCenterY, -vectorCenterZ),
                            material);

                    addViewToFrame(modelRenderable, arGalleryFragment);
                }));
    }

    @Override
    public void rotateOnZAxis(boolean enable, long timeInMs) {

        Quaternion orientation1 = Quaternion.axisAngle(new Vector3(vectorCenterX, vectorCenterY, -vectorCenterZ), 0);
        Quaternion orientation2 = Quaternion.axisAngle(new Vector3(vectorCenterX, vectorCenterY, -vectorCenterZ), 120);
        Quaternion orientation3 = Quaternion.axisAngle(new Vector3(vectorCenterX, vectorCenterY, -vectorCenterZ), 240);
        Quaternion orientation4 = Quaternion.axisAngle(new Vector3(vectorCenterX, vectorCenterY, -vectorCenterZ), 360);

        rotateOnZAxis(enable, timeInMs, orientation1, orientation2, orientation3, orientation4);
    }
}
