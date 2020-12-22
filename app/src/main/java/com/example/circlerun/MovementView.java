package com.example.circlerun;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.HashSet;

public class MovementView extends SurfaceView implements SurfaceHolder.Callback {
    private int xPos;
    private int yPos;

    private int xVel;
    private int yVel;

    private int width;
    private int height;

    private int circleRadius;
    private Paint circlePaint;

    UpdateThread updateThread;

    ArrayList<Integer> list =new ArrayList<>();

    public MovementView(Context context) {
        super(context);
        getHolder().addCallback(this);
        list.add(Color.RED);
        list.add(Color.GREEN);
        list.add(Color.GRAY);
        list.add(Color.BLUE);
        list.add(Color.BLACK);
        list.add(Color.CYAN);
        list.add(Color.LTGRAY);
        list.add(Color.MAGENTA);
        list.add(Color.YELLOW);
        list.add(Color.DKGRAY);
        list.add(Color.BLUE);
        circleRadius = 200;
        circlePaint = new Paint();
//        circlePaint.setColor(list,);

        xVel = 7;
        yVel = 7;

    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawColor(Color.WHITE);
        canvas.drawCircle(xPos, yPos, circleRadius, circlePaint);

    }

    public void updatePhysics() {
        xPos += xVel;
        yPos += yVel;
        if (yPos - circleRadius < 0 || yPos + circleRadius > height) {
            //В случае ударов о верх или низ холста
            if (yPos - circleRadius < 0) {
                //Удар о верхнюю грань
                circlePaint.setColor(list.get((int)(Math.random()*10)));
                yPos = circleRadius;
            }else{
                //Удар о нижнюю грань
                circlePaint.setColor(list.get((int)(Math.random()*10)));
                yPos = height - circleRadius;
            }
            //Меняем направление шарика
            yVel *= -1;
        }
        if (xPos - circleRadius < 0 || xPos + circleRadius > width) {
            //В случае столкновений с правой или левой стенками
            if (xPos - circleRadius < 0) {
                circlePaint.setColor(list.get((int)(Math.random()*10)));
                //В случае столкновений с левой стенкой
                xPos = circleRadius;
            } else {
                circlePaint.setColor(list.get((int)(Math.random()*10)));
                //В случае столкновений с правой стенкой
                xPos = width - circleRadius;
            }
            //Меняем x направление на обратное
            xVel *= -1;
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        Rect surfaceFrame = holder.getSurfaceFrame();
        width = surfaceFrame.width();
        height = surfaceFrame.height();

        xPos = width / 2;
        yPos = circleRadius;

        updateThread = new UpdateThread(this);
        updateThread.setRunning(true);
        updateThread.start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;

        updateThread.setRunning(false);
        while (retry) {
            try {
                updateThread.join();
                retry = false;
            } catch (InterruptedException e) {

            }
        }
    }
}
