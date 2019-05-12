# ARGallery
Using Sceneform to render bitmaps from Gallery using ViewRenderable and ShapeRenderable.  
  
# Sample  
Check the sample folder and inside it you will be able to find videos and images of the output.  

# Explanation  
In here I tried to show images (or bitmaps) from the gallery of the user. Tried to show it using ViewRenderable and ShapeRenderable.  
  
__ViewRenderable__  
This can basically displays view after inflating view XMLs and then displaying then in the AR context. For this example we are inflating an ImageView and then it is displayed in the AR context. This can be though of as the 2D views.  
  
__ShapeRenderable__  
This is a bit complex and we can draw 3D shapes in the AR context. To use the bitmap we need to create a texture and then create a material from it, which would then be used to create the renderable object.  
  
__Rotation Animation__  
This has been simply created using ObjectAnimator. We use the Quaternion object which comprises of a Vector3 (which defines the X, Y and Z axis information) along with the rotation. We pass in an array of this Quaternion objects which is then used on the localRotation property and used to animate the renderables.
