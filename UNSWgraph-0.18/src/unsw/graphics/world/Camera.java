package unsw.graphics.world;

import com.jogamp.opengl.GL3;
import unsw.graphics.CoordFrame3D;
import unsw.graphics.Matrix4;
import unsw.graphics.Shader;
import unsw.graphics.geometry.Point3D;
import unsw.graphics.scene.MathUtil;

/**
 * The camera is a SceneObject that can be moved, rotated and scaled like any other, as well as
 * attached to any parent in the scene tree.
 * 
 * TODO: You need to implement the setView() method.
 *
 * @author malcolmr
 * @author Robert Clifton-Everest
 */
public class Camera{
    
    /**
     * The aspect ratio is the ratio of the width of the window to the height.
     */
    private float myAspectRatio;
    private float scale;
    private Point3D Position;
    private float startRotation = 180.0f;
    
    private float RotationX;
    private float RotationY;
    private float RotationZ;
    private float MovementSpeed = 0.5f;
    
    public Camera() {
    	
    	myAspectRatio = 1.0f;
    	scale = 1;
    	Position = new Point3D(0, 0, 0);
    
    	RotationX = 0;
        RotationY = startRotation;
        RotationZ = 0;
    	
    }

    
    public Camera(float x, float y , float z) {
    	
    	myAspectRatio = 1.0f;
    	scale = 1;
    	Position = new Point3D(x, y, z);
    
    	RotationX = 0;
        RotationY = startRotation;
        RotationZ = 0;
    	
    }
    
    public void setPosition(Point3D point){
    	
    	Position = point;
    	
    }
    
    public Point3D getPosition(){
    	
    	return Position;
    }
    
    public void setHeight(float Y){
    	Position = new Point3D( Position.getX(), Y, Position.getZ());
    }
    
    public void setView(GL3 gl, CoordFrame3D frame, Avatar ava) {

    	
    	frame = frame.translate(Position)
    			.rotateX(RotationX)
    			.rotateY(RotationY)
    			.rotateZ(RotationZ)
    			.scale(scale, scale, scale);
    	
    	
    	CoordFrame3D viewFrame = CoordFrame3D.identity();
    	
    	float Gscale = ava.getScale()*scale;
    	Matrix4 mat = frame.getMatrix();
    	//System.out.println(mat.toString());
    	//System.out.println("X:"+mat.getValues()[12] + ", Y:"+mat.getValues()[13] + ", Z:"+mat.getValues()[14]);
    	//System.out.println("---------");
    	
    	Point3D Gposition = new Point3D(mat.getValues()[12], mat.getValues()[13], mat.getValues()[14]);
    	
    	viewFrame = viewFrame.scale(1/Gscale, 1/Gscale, 1/Gscale)
    			.rotateX(MathUtil.normaliseAngle(-ava.getRotx()-RotationX))
    			.rotateY(MathUtil.normaliseAngle(-ava.getRoty()-RotationY))
    			.rotateZ(MathUtil.normaliseAngle(-ava.getRotz()-RotationZ))
    			.translate(-Gposition.getX(),-Gposition.getY(),-Gposition.getZ());
    	
        Shader.setViewMatrix(gl, viewFrame.getMatrix());

    }

    public void reshape(int width, int height) {
        myAspectRatio = (1f * width) / height;            
    }

    /**
     * Transforms a point from camera coordinates to world coordinates. Useful for things like mouse
     * interaction
     * 
     * @param x
     * @param y
     * @return
     */
//    public Point2D fromView(float x, float y) {
//        Matrix3 mat = Matrix3.translation(getGlobalPosition())
//                .multiply(Matrix3.rotation(getGlobalRotation()))
//                .multiply(Matrix3.scale(getGlobalScale(), getGlobalScale()))
//                .multiply(Matrix3.scale(myAspectRatio, 1));
//        return mat.multiply(new Vector3(x,y,1)).asPoint2D();
//    }

    public float getAspectRatio() {
        return myAspectRatio;
    }
    
    
}

