package com.example.firstactivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class ShapesAnimationView extends View {

    private final Paint paintShape = new Paint();
    private final Paint paintPoint = new Paint();
    private final List<Path> shapePaths = new ArrayList<>();
    private final List<float[]> shapePoints = new ArrayList<>();
    private final Handler handler = new Handler();
    private int currentShapeIndex = 0;
    private int currentPointIndex = 0;
    private float pointX = 0;
    private float pointY = 0;
    private final Runnable onComplete;

    public ShapesAnimationView(Context context, Runnable onComplete) {
        super(context);
        this.onComplete = onComplete;
        init();
    }

    public ShapesAnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.onComplete = null;
        init();
    }

    private void init() {
        paintShape.setStyle(Paint.Style.STROKE);
        paintShape.setColor(0xFF2196F3);
        paintShape.setStrokeWidth(8f);

        paintPoint.setStyle(Paint.Style.FILL);
        paintPoint.setColor(0xFFFF0000);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (currentShapeIndex < shapePaths.size()) {
            canvas.drawPath(shapePaths.get(currentShapeIndex), paintShape);
        }
        canvas.drawCircle(pointX, pointY, 25f, paintPoint);
    }

    public void startFullAnimation() {
        shapePaths.clear();
        shapePoints.clear();

        post(() -> {
            addLinePath();
            addCirclePath();
            addRectanglePath();
            addTrianglePath();
            addStarPath();
            addZigZagPath();
            addSpiralPath();
            addInfinityPath();
            addHeartPath();
            addWavePath();
            addPentagonPath();
            addLeafPath();
            startNextShape();
        });
    }

    private void startNextShape() {
        if (currentShapeIndex >= shapePoints.size()) {
            if (onComplete != null) onComplete.run();
            return;
        }

        float[] points = shapePoints.get(currentShapeIndex);
        currentPointIndex = 0;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentPointIndex >= points.length - 1) {
                    currentShapeIndex++;
                    postDelayed(() -> startNextShape(), 1000);
                } else {
                    pointX = points[currentPointIndex];
                    pointY = points[currentPointIndex + 1];
                    currentPointIndex += 2;
                    invalidate();
                    handler.postDelayed(this, 30);
                }
            }
        }, 100);
    }

    // Original Shapes...
    private void addLinePath() {
        float w = getWidth(), h = getHeight();
        Path path = new Path();
        path.moveTo(w * 0.2f, h / 2);
        path.lineTo(w * 0.8f, h / 2);
        shapePaths.add(path);
        shapePoints.add(generateLinePoints(w * 0.2f, h / 2, w * 0.8f, h / 2, 200));
    }

    private void addCirclePath() {
        float cx = getWidth() / 2f, cy = getHeight() / 2f;
        float radius = Math.min(getWidth(), getHeight()) * 0.3f;
        Path path = new Path();
        path.addCircle(cx, cy, radius, Path.Direction.CW);
        shapePaths.add(path);
        shapePoints.add(generateCirclePoints(cx, cy, radius, 3));
    }

    private void addRectanglePath() {
        float left = getWidth() * 0.25f;
        float top = getHeight() * 0.25f;
        float right = getWidth() * 0.75f;
        float bottom = getHeight() * 0.75f;
        Path path = new Path();
        path.moveTo(left, top);
        path.lineTo(right, top);
        path.lineTo(right, bottom);
        path.lineTo(left, bottom);
        path.lineTo(left, top);
        shapePaths.add(path);
        shapePoints.add(generateRectanglePoints(left, top, right, bottom, 3));
    }

    private void addTrianglePath() {
        float cx = getWidth() / 2f;
        float cy = getHeight() / 2f;
        float size = Math.min(getWidth(), getHeight()) * 0.3f;
        float x1 = cx, y1 = cy - size;
        float x2 = cx - size, y2 = cy + size;
        float x3 = cx + size, y3 = cy + size;
        Path path = new Path();
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);
        path.lineTo(x3, y3);
        path.lineTo(x1, y1);
        shapePaths.add(path);
        shapePoints.add(generatePolygonPoints(new float[]{x1, y1, x2, y2, x3, y3}, 3));
    }

    private void addStarPath() {
        float cx = getWidth() / 2f, cy = getHeight() / 2f;
        float outer = 250, inner = 100;
        Path path = new Path();
        float angle = (float) (-Math.PI / 2);
        path.moveTo(cx + outer * (float) Math.cos(angle), cy + outer * (float) Math.sin(angle));
        for (int i = 1; i <= 10; i++) {
            angle += Math.PI / 5;
            float r = (i % 2 == 0) ? outer : inner;
            path.lineTo(cx + r * (float) Math.cos(angle), cy + r * (float) Math.sin(angle));
        }
        path.close();
        shapePaths.add(path);
        shapePoints.add(generatePathPoints(path, 600));
    }

    private void addZigZagPath() {
        float startX = getWidth() * 0.2f;
        float endX = getWidth() * 0.8f;
        float topY = getHeight() * 0.4f;
        float bottomY = getHeight() * 0.6f;
        Path path = new Path();
        path.moveTo(startX, topY);
        for (int i = 0; i < 6; i++) {
            float x = startX + (endX - startX) / 6 * (i + 1);
            float y = (i % 2 == 0) ? bottomY : topY;
            path.lineTo(x, y);
        }
        shapePaths.add(path);
        shapePoints.add(generatePathPoints(path, 300));
    }

    private void addSpiralPath() {
        float cx = getWidth() / 2f;
        float cy = getHeight() / 2f;
        Path path = new Path();
        int steps = 1000;
        float maxRadius = Math.min(getWidth(), getHeight()) * 0.4f;
        for (int i = 0; i < steps; i++) {
            float t = (float) i / steps;
            float angle = (float) (6 * Math.PI * t);
            float radius = maxRadius * t;
            float x = cx + radius * (float) Math.cos(angle);
            float y = cy + radius * (float) Math.sin(angle);
            if (i == 0) path.moveTo(x, y);
            else path.lineTo(x, y);
        }
        shapePaths.add(path);
        shapePoints.add(generatePathPoints(path, 700));
    }

    private void addInfinityPath() {
        float cx = getWidth() / 2f;
        float cy = getHeight() / 2f;
        float a = 200, b = 100;
        Path path = new Path();
        int steps = 600;
        for (int i = 0; i < steps; i++) {
            float t = (float) (2 * Math.PI * i / steps);
            float x = cx + a * (float) Math.sin(t);
            float y = cy + b * (float) Math.sin(t) * (float) Math.cos(t);
            if (i == 0) path.moveTo(x, y);
            else path.lineTo(x, y);
        }
        shapePaths.add(path);
        shapePoints.add(generatePathPoints(path, 600));
    }

    // ✅ New Shapes
    private void addHeartPath() {
        float cx = getWidth() / 2f, cy = getHeight() / 2f;
        Path path = new Path();
        int steps = 600;
        for (int i = 0; i < steps; i++) {
            float t = (float) (2 * Math.PI * i / steps);
            float x = 16 * (float) Math.pow(Math.sin(t), 3);
            float y = 13 * (float) Math.cos(t) - 5 * (float) Math.cos(2 * t) - 2 * (float) Math.cos(3 * t) - (float) Math.cos(4 * t);
            x *= 15; y *= 15;
            x += cx; y = cy - y;
            if (i == 0) path.moveTo(x, y);
            else path.lineTo(x, y);
        }
        shapePaths.add(path);
        shapePoints.add(generatePathPoints(path, 600));
    }

    private void addWavePath() {
        float w = getWidth(), h = getHeight();
        Path path = new Path();
        path.moveTo(0, h / 2f);
        for (int x = 0; x <= w; x += 5) {
            float y = h / 2f + 100 * (float) Math.sin((float) x / w * 4 * Math.PI);
            path.lineTo(x, y);
        }
        shapePaths.add(path);
        shapePoints.add(generatePathPoints(path, 600));
    }

    private void addPentagonPath() {
        float cx = getWidth() / 2f, cy = getHeight() / 2f;
        float radius = 200;
        Path path = new Path();
        for (int i = 0; i < 5; i++) {
            double angle = 2 * Math.PI * i / 5 - Math.PI / 2;
            float x = cx + radius * (float) Math.cos(angle);
            float y = cy + radius * (float) Math.sin(angle);
            if (i == 0) path.moveTo(x, y);
            else path.lineTo(x, y);
        }
        path.close();
        shapePaths.add(path);
        shapePoints.add(generatePathPoints(path, 600));
    }

    private void addLeafPath() {
        float cx = getWidth() / 2f;
        float cy = getHeight() / 2f;
        Path path = new Path();
        path.moveTo(cx, cy);
        for (int i = 0; i <= 180; i++) {
            float t = (float) i / 180f * (float) Math.PI;
            float r = (float) (100 * Math.sin(t) * Math.sqrt(Math.abs(Math.cos(t))));
            float x = cx + r * (float) Math.cos(t);
            float y = cy - r * (float) Math.sin(t);
            path.lineTo(x, y);
        }
        path.lineTo(cx, cy);
        shapePaths.add(path);
        shapePoints.add(generatePathPoints(path, 600));
    }

    private float[] generateLinePoints(float x1, float y1, float x2, float y2, int steps) {
        float[] pts = new float[steps * 2];
        for (int i = 0; i < steps; i++) {
            float t = (float) i / (steps - 1);
            pts[i * 2] = x1 + t * (x2 - x1);
            pts[i * 2 + 1] = y1 + t * (y2 - y1);
        }
        return pts;
    }

    private float[] generateCirclePoints(float cx, float cy, float r, int rounds) {
        int steps = 360 * rounds;
        float[] pts = new float[steps * 2];
        for (int i = 0; i < steps; i++) {
            float angle = (float) (2 * Math.PI * i / 360);
            pts[i * 2] = cx + r * (float) Math.cos(angle);
            pts[i * 2 + 1] = cy + r * (float) Math.sin(angle);
        }
        return pts;
    }

    private float[] generateRectanglePoints(float l, float t, float r, float b, int rounds) {
        List<Float> pts = new ArrayList<>();
        for (int i = 0; i < rounds; i++) {
            for (float x = l; x <= r; x += 10) { pts.add(x); pts.add(t); }
            for (float y = t; y <= b; y += 10) { pts.add(r); pts.add(y); }
            for (float x = r; x >= l; x -= 10) { pts.add(x); pts.add(b); }
            for (float y = b; y >= t; y -= 10) { pts.add(l); pts.add(y); }
        }
        float[] arr = new float[pts.size()];
        for (int i = 0; i < arr.length; i++) arr[i] = pts.get(i);
        return arr;
    }

    private float[] generatePolygonPoints(float[] vertices, int rounds) {
        List<Float> pts = new ArrayList<>();
        for (int r = 0; r < rounds; r++) {
            for (int i = 0; i < vertices.length; i += 2) {
                float x1 = vertices[i], y1 = vertices[i + 1];
                float x2 = vertices[(i + 2) % vertices.length], y2 = vertices[(i + 3) % vertices.length];
                for (float t = 0; t <= 1; t += 0.05f) {
                    pts.add(x1 + t * (x2 - x1));
                    pts.add(y1 + t * (y2 - y1));
                }
            }
        }
        float[] arr = new float[pts.size()];
        for (int i = 0; i < arr.length; i++) arr[i] = pts.get(i);
        return arr;
    }

    private float[] generatePathPoints(Path path, int steps) {
        List<Float> pointsList = new ArrayList<>();
        android.graphics.PathMeasure pathMeasure = new android.graphics.PathMeasure(path, false);
        float pathLength = pathMeasure.getLength();

        for (int i = 0; i < steps; i++) {
            float distance = (i / (float) (steps - 1)) * pathLength;
            float[] pos = new float[2];
            pathMeasure.getPosTan(distance, pos, null);
            pointsList.add(pos[0]);
            pointsList.add(pos[1]);
        }

        float[] points = new float[pointsList.size()];
        for (int i = 0; i < points.length; i++) {
            points[i] = pointsList.get(i);
        }
        return points;
    }
}
