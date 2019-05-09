package com.wwdablu.soumya.arphotogallery.renderers;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.google.ar.sceneform.rendering.ViewRenderable;
import com.wwdablu.soumya.arphotogallery.ARGalleryFragment;
import com.wwdablu.soumya.arphotogallery.R;
import com.wwdablu.soumya.arphotogallery.RenderCallback;

public final class ViewRenderHelper extends RenderHelper {

    private ViewRenderable mViewRenderable;

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
