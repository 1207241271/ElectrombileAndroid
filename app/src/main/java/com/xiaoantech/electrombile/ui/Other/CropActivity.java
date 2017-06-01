package com.xiaoantech.electrombile.ui.Other;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.xiaoantech.electrombile.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by yangxu on 2017/4/26.
 */

public class CropActivity extends Activity {
    private LinearLayout imgSave;
    private ImageView imgView,imgScreenshot;
    private Bitmap bitmap = null;

    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;

    private int mode = NONE;
    private float oldDist;
    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();
    private PointF start = new PointF();
    private PointF mid = new PointF();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);

        imgView =(ImageView)findViewById(R.id.screenshot_img);
        imgScreenshot = (ImageView)findViewById(R.id.screenshot);
        imgSave = (LinearLayout)findViewById(R.id.img_save);



        //解析intent数据 还原uri
        Intent intent = getIntent();
        Uri imageUri = intent.getData();
        final String IMEI = intent.getStringExtra("IMEI");




//        srcPic = (ImageView) findViewById(R.id.src_pic);

        try {
            bitmap = getThumbnail(imageUri);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (bitmap != null){
            imgView.setImageBitmap(bitmap);
            imgView.setOnTouchListener(touch);
            imgSave.setOnClickListener(imgClick);
        }

    }

    View.OnClickListener imgClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            imgView.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(imgView.getDrawingCache());

            int w = imgScreenshot.getWidth();
            int h = imgScreenshot.getHeight();

            int left = imgScreenshot.getLeft();
            int right = imgScreenshot.getRight();
            int top = imgScreenshot.getTop();
            int bottom = imgScreenshot.getBottom();

            Bitmap targetBitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(targetBitmap);
            Path path = new Path();

            path.addCircle((float)((right-left) / 2),((float)((bottom-top)) / 2), (float)(w / 2),
                    Path.Direction.CCW);

            canvas.clipPath(path);
            int center = (bottom+top)/2;
            canvas.drawBitmap(bitmap,new Rect(left,center - w/2,right,center + w/2),new Rect(left,center - w/2,right,center + w/2),null);

            targetBitmap = Bitmap.createBitmap(targetBitmap,left,center - w/2,w,w);
            imgScreenshot.setImageBitmap(targetBitmap);
            saveMyBitmaptoUserFile(targetBitmap);

            Toast.makeText(getBaseContext(), "保存成功", Toast.LENGTH_LONG).show();
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
    };



    View.OnTouchListener touch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            ImageView view = (ImageView) v;
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    savedMatrix.set(matrix); // 把原始 Matrix对象保存起来
                    start.set(event.getX(), event.getY()); // 设置x,y坐标
                    mode = DRAG;
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                    mode = NONE;
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    oldDist = spacing(event);
                    if (oldDist > 10f) {
                        savedMatrix.set(matrix);
                        midPoint(mid, event); // 求出手指两点的中点
                        mode = ZOOM;
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mode == DRAG) {
                        matrix.set(savedMatrix);

                        matrix.postTranslate(event.getX() - start.x, event.getY()
                                - start.y);
                    } else if (mode == ZOOM) {
                        float newDist = spacing(event);
                        if (newDist > 10f) {
                            matrix.set(savedMatrix);
                            float scale = newDist / oldDist;
                            matrix.postScale(scale, scale, mid.x, mid.y);
                        }
                    }
                    break;
            }
            System.out.println(event.getAction());
            view.setImageMatrix(matrix);

            return true;
        }
    };

    //求两点距离
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    //求两点间中点
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    public void bitmapRelease() {
        if (bitmap != null && !bitmap.isRecycled()) {
            // 回收并且置为null
            bitmap.recycle();
            bitmap = null;
            System.gc();
        }
    }

    public Bitmap getThumbnail(Uri uri) throws FileNotFoundException, IOException {
        InputStream input = this.getContentResolver().openInputStream(uri);

        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1))
            return null;

        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

        double ratio = (originalSize > 300) ? (originalSize / 300) : 1.0;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        input = this.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return bitmap;
    }

    private static int getPowerOfTwoForSampleRatio(double ratio) {
        int k = Integer.highestOneBit((int) Math.floor(ratio));
        if (k == 0) return 1;
        else return k;
    }

    //bitmap写文件
    public void saveMyBitmaptoUserFile(Bitmap mBitmap) {


        File f = new File(this.getExternalFilesDir(null),"user.png");

        FileOutputStream fOut = null;
        try {
            f.createNewFile();
            fOut = new FileOutputStream(f);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveMyBitmaptoIMEIFile(Bitmap mBitmap,String IMEI) {


        File f = new File(this.getExternalFilesDir(null),IMEI + ".png");

        FileOutputStream fOut = null;
        try {
            f.createNewFile();
            fOut = new FileOutputStream(f);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bitmapRelease();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


}
