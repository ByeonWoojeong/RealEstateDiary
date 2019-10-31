package app.cosmos.ghrealestatediary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;


public class DrawingView extends View {

    private ArrayList<Path> paths = new ArrayList<Path>();
    private ArrayList<Path> unPaths = new ArrayList<Path>();
    private Path drawPath;
    private Paint drawPaint, canvasPaint;
    private int paintColor = 0xFF000000, paintAlpha = 255, paintTransparent = Color.TRANSPARENT, paintTestColor1 = Color.BLUE, paintTestColor2 = Color.WHITE;
    public Canvas drawCanvas;
    private Bitmap canvasBitmap;
    private float brushSize, lastBrushSize;
    private boolean erase = false;
    private boolean drawing = false;

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayerType(View.LAYER_TYPE_SOFTWARE, drawPaint);
        setupDrawing();
    }

    private void  setupDrawing() {
        brushSize = getResources().getInteger(R.integer.small_size);
        lastBrushSize = brushSize;
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(brushSize);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
        startNew();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        if(!erase){
            canvas.drawPath(drawPath, drawPaint);
        }else{
            drawCanvas.drawPath(drawPath, drawPaint);
        }
//        for(Path p : paths){
//            canvas.drawPath(p, drawPaint);
//        }
//        canvas.drawPath(drawPath, drawPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                unPaths.clear();
                drawPath.reset();
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
//                if(erase) {
//                    drawPath.lineTo(touchX, touchY);
//                    drawCanvas.drawPath(drawPath, drawPaint);
//                    drawPath.reset();
//                    drawPath.moveTo(touchX, touchY);
//                } else {
//                    drawPath.lineTo(touchX, touchY);
//                }
//                drawing = true;
//                break;
                drawPath.lineTo(touchX, touchY);
                drawing = true;
                break;
            case MotionEvent.ACTION_UP:
                drawPath.lineTo(touchX, touchY);
                drawCanvas.drawPath(drawPath, drawPaint);
                paths.add(drawPath);
                drawPath = new Path();
                break;
            default:
                return false;
        }
        invalidate();
        return true;

    }

    public void setColor(String newColor) {
        invalidate();
        if (newColor.startsWith("#")) {
            paintColor = Color.parseColor(newColor);
            drawPaint.setColor(paintColor);
            drawPaint.setShader(null);
        } else {
            int patternID = getResources().getIdentifier(newColor, "drawable", "app.cosmos.ghrealestatediary");
            Bitmap patternBMP = BitmapFactory.decodeResource(getResources(), patternID);
            BitmapShader patternBMPshader = new BitmapShader(patternBMP, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
            drawPaint.setColor(0xFFFFFFFF);
            drawPaint.setShader(patternBMPshader);
        }
    }

    public void setBrushSize(float newSize) {
        float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, newSize, getResources().getDisplayMetrics());
        brushSize = pixelAmount;
        drawPaint.setStrokeWidth(brushSize);
    }

    public void setLastBrushSize(float lastSize){
        lastBrushSize=lastSize;
    }

    public float getLastBrushSize(){
        return lastBrushSize;
    }

    public void setErase(boolean isErase){
        erase = isErase;
        if (erase) {
            setBrushSize(33);
            drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            erase = false;
        } else {
            setBrushSize(0);
            drawPaint.setColor(paintColor);
            drawPaint.setStrokeWidth(getResources().getInteger(R.integer.small_size));
            drawPaint.setXfermode(null);
            erase = true;
        }
    }

    public void unDo(){
        if(paths.size() > 0){
            unPaths.add(paths.remove(paths.size() -1));
            invalidate();
        }else{
            Toast.makeText(getContext(), "더이상 지울 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }
    public void reDo (){
        if (unPaths.size()>0)
        {
            paths.add(unPaths.remove(unPaths.size()-1));
            invalidate();
        } else {
            Toast.makeText(getContext(), "모두 복구하였습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    public void startNew() {
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        drawing = false;
        invalidate();
    }

    public boolean isDrawing() {
        return drawing;
    }

    public int getPaintAlpha() {
        return Math.round((float) paintAlpha / 255 * 100);
    }

    public void setPaintAlpha(int newAlpha) {
        paintAlpha = Math.round((float) newAlpha / 100 * 255);
        drawPaint.setColor(paintColor);
        drawPaint.setAlpha(paintAlpha);
    }
}
