package com.gmail.robmadeyou.Draw;

import static org.lwjgl.opengl.GL11.*;

public class Line {
    public static void drawLine(float x, float y, float x2, float y2) {
        glBegin(GL_LINES);
        glVertex2f(x, y);
        glVertex2f(x2, y2);
        glEnd();
    }
}