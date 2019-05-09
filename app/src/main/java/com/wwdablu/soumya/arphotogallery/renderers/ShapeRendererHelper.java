package com.wwdablu.soumya.arphotogallery.renderers;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.rendering.Texture;
import com.wwdablu.soumya.arphotogallery.ARGalleryFragment;
import com.wwdablu.soumya.arphotogallery.RenderCallback;

public class ShapeRendererHelper extends RenderHelper {

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
                            new Vector3(0.1f, 0.1f, -0.5f),
                            material);

                    addViewToFrame(modelRenderable, arGalleryFragment);
                }));
    }
}
