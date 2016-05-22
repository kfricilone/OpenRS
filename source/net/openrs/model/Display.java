/*
 * Copyright (c) 2015, Oskar Veerhoek
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those
 * of the authors and should not be interpreted as representing official policies,
 * either expressed or implied, of the FreeBSD Project.
 */

package net.openrs.model;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;

// Easy access to all the GLFW and OpenGL methods and constants (otherwise you would have to write GLFW.glfw.. every time)

/**
 * Code is based on the following sample code: http://www.lwjgl.org/guide
 */
public class Display {

    // The GLFW error callback: this tells GLFW what to do if things go wrong
    private static GLFWErrorCallback errorCallback;
    // The handle of the GLFW window
    private static long windowID;

    public static void main(String[] args) {
        // Set the error handling code: all GLFW errors will be printed to the system error stream (just like println)
        errorCallback = GLFWErrorCallback.createPrint(System.err);
        glfwSetErrorCallback(errorCallback);

        // Initialize GLFW:
        int glfwInitializationResult = glfwInit(); // initialize GLFW and store the result (pass or fail)
        if (glfwInitializationResult == GL_FALSE)
            throw new IllegalStateException("GLFW initialization failed");

        // Configure the GLFW window
        windowID = glfwCreateWindow(
                640, 480,   // Width and height of the drawing canvas in pixels
                "Display",     // Title of the window
                MemoryUtil.NULL, // Monitor ID to use for fullscreen mode, or NULL to use windowed mode (LWJGL JavaDoc)
                MemoryUtil.NULL); // Window to share resources with, or NULL to not share resources (LWJGL JavaDoc)

        if (windowID == MemoryUtil.NULL)
            throw new IllegalStateException("GLFW window creation failed");
        
        // User parameters for
//        glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // should be window be visible while it's initializing? (use for position)
//        glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);
//        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE); // Mac Modern OpenGL
//        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE); // Mac Modern OpenGL
//        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3); // Modern OpenGL
//        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2); // Mac Modern OpenGL

        glfwMakeContextCurrent(windowID); // Links the OpenGL context of the window to the current thread (GLFW_NO_CURRENT_CONTEXT error)
        glfwSwapInterval(1); // Enable VSync, which effective caps the frame-rate of the application to 60 frames-per-second
        glfwShowWindow(windowID);

        // If you don't add this line, you'll get the following exception:
        //  java.lang.IllegalStateException: There is no OpenGL context current in the current thread.
        GL.createCapabilities(); // Links LWJGL to the OpenGL context
        
        glMatrixMode(GL_PROJECTION);
        glOrtho(0, 640, 0, 480, 1, -1);
        glMatrixMode(GL_MODELVIEW);
        
        // Enter the update loop: keep refreshing the window as long as the window isn't closed
        while (glfwWindowShouldClose(windowID) == GL_FALSE) {
            // Clear the contents of the window (try disabling this and resizing the window – fun guaranteed)
            glClear(GL_COLOR_BUFFER_BIT);
            // ">>" denotes a possibly modified piece of OpenGL documentation (http://www.opengl.org/sdk/docs/man/)
            // >> glBegin and glEnd delimit the vertices that define a primitive or a group of like primitives.
            // >> glBegin accepts a single argument that specifies how the vertices are interpreted.
            // All upcoming vertex calls will be taken as points of a quadrilateral until glEnd is called. Since
            // this primitive requires four vertices, we will have to call glVertex four times.
            glBegin(GL_QUADS);
            // >> glVertex commands are used within glBegin/glEnd pairs to specify point, line, and polygon vertices.
            // >> glColor sets the current colour. (All subsequent calls to glVertex will be assigned this colour)
            // >> The number after 'glVertex'/'glColor' indicates the amount of components. (xyzw/rgba)
            // >> The character after the number indicates the type of arguments.
            // >>      (for 'glVertex' = d: Double, f: Float, i: Integer)
            // >>      (for 'glColor'  = d: Double, f: Float, b: Signed Byte, ub: Unsigned Byte)
            glColor3f(1.0f, 0.0f, 0.0f);                    // Pure Green
            glVertex2i(0, 0);                               // Upper-left
            glColor3b((byte) 0, (byte) 127, (byte) 0);      // Pure Red
            glVertex2d(640.0, 0.0);                         // Upper-right
            glColor3ub((byte) 255, (byte) 255, (byte) 255); // White
            glVertex2f(640.0f, 480.0f);                     // Bottom-right
            glColor3d(0.0d, 0.0d, 1.0d);                    // Pure Blue
            glVertex2i(0, 480);                             // Bottom-left
            // If we put another four calls to glVertex2i here, a second quadrilateral will be drawn.
            glEnd();
            
            drawString("FPS: ", 300, 300);
            
            // Swaps the front and back framebuffers, this is a very technical process which you don't necessarily
            // need to understand. You can simply see this method as updating the window contents.
            glfwSwapBuffers(windowID);
            // Polls the user input. This is very important, because it prevents your application from becoming unresponsive
            glfwPollEvents();
        }

        // It's important to release the resources when the program has finished to prevent dreadful memory leaks
        glfwDestroyWindow(windowID);
        // Destroys all remaining windows and cursors (LWJGL JavaDoc)
        glfwTerminate();
    }
    
    public static void drawString(String s, int x, int y) {
        int startX = x;
        glBegin(GL_POINTS);
        for (char c : s.toLowerCase().toCharArray()) {
           if (c == 'a') {
              for (int i = 0; i < 8; i++) {
                 glVertex2f(x + 1, y + i);
                 glVertex2f(x + 7, y + i);
              }
              for (int i = 2; i <= 6; i++) {
                 glVertex2f(x + i, y + 8);
                 glVertex2f(x + i, y + 4);
              }
              x += 8;
           } else if (c == 'b') {
              for (int i = 0; i < 8; i++) {
                 glVertex2f(x + 1, y + i);
              }
              for (int i = 1; i <= 6; i++) {
                 glVertex2f(x + i, y);
                 glVertex2f(x + i, y + 4);
                 glVertex2f(x + i, y + 8);
              }
              glVertex2f(x + 7, y + 5);
              glVertex2f(x + 7, y + 7);
              glVertex2f(x + 7, y + 6);
              glVertex2f(x + 7, y + 1);
              glVertex2f(x + 7, y + 2);
              glVertex2f(x + 7, y + 3);
              x += 8;
           } else if (c == 'c') {
              for (int i = 1; i <= 7; i++) {
                 glVertex2f(x + 1, y + i);
              }
              for (int i = 2; i <= 5; i++) {
                 glVertex2f(x + i, y);
                 glVertex2f(x + i, y + 8);
              }
              glVertex2f(x + 6, y + 1);
              glVertex2f(x + 6, y + 2);

              glVertex2f(x + 6, y + 6);
              glVertex2f(x + 6, y + 7);

              x += 8;
           } else if (c == 'd') {
              for (int i = 0; i <= 8; i++) {
                 glVertex2f(x + 1, y + i);
              }
              for (int i = 2; i <= 5; i++) {
                 glVertex2f(x + i, y);
                 glVertex2f(x + i, y + 8);
              }
              glVertex2f(x + 6, y + 1);
              glVertex2f(x + 6, y + 2);
              glVertex2f(x + 6, y + 3);
              glVertex2f(x + 6, y + 4);
              glVertex2f(x + 6, y + 5);
              glVertex2f(x + 6, y + 6);
              glVertex2f(x + 6, y + 7);

              x += 8;
           } else if (c == 'e') {
              for (int i = 0; i <= 8; i++) {
                 glVertex2f(x + 1, y + i);
              }
              for (int i = 1; i <= 6; i++) {
                 glVertex2f(x + i, y + 0);
                 glVertex2f(x + i, y + 8);
              }
              for (int i = 2; i <= 5; i++) {
                 glVertex2f(x + i, y + 4);
              }
              x += 8;
           } else if (c == 'f') {
              for (int i = 0; i <= 8; i++) {
                 glVertex2f(x + 1, y + i);
              }
              for (int i = 1; i <= 6; i++) {
                 glVertex2f(x + i, y + 8);
              }
              for (int i = 2; i <= 5; i++) {
                 glVertex2f(x + i, y + 4);
              }
              x += 8;
           } else if (c == 'g') {
              for (int i = 1; i <= 7; i++) {
                 glVertex2f(x + 1, y + i);
              }
              for (int i = 2; i <= 5; i++) {
                 glVertex2f(x + i, y);
                 glVertex2f(x + i, y + 8);
              }
              glVertex2f(x + 6, y + 1);
              glVertex2f(x + 6, y + 2);
              glVertex2f(x + 6, y + 3);
              glVertex2f(x + 5, y + 3);
              glVertex2f(x + 7, y + 3);

              glVertex2f(x + 6, y + 6);
              glVertex2f(x + 6, y + 7);

              x += 8;
           } else if (c == 'h') {
              for (int i = 0; i <= 8; i++) {
                 glVertex2f(x + 1, y + i);
                 glVertex2f(x + 7, y + i);
              }
              for (int i = 2; i <= 6; i++) {
                 glVertex2f(x + i, y + 4);
              }
              x += 8;
           } else if (c == 'i') {
              for (int i = 0; i <= 8; i++) {
                 glVertex2f(x + 3, y + i);
              }
              for (int i = 1; i <= 5; i++) {
                 glVertex2f(x + i, y + 0);
                 glVertex2f(x + i, y + 8);
              }
              x += 7;
           } else if (c == 'j') {
              for (int i = 1; i <= 8; i++) {
                 glVertex2f(x + 6, y + i);
              }
              for (int i = 2; i <= 5; i++) {
                 glVertex2f(x + i, y + 0);
              }
              glVertex2f(x + 1, y + 3);
              glVertex2f(x + 1, y + 2);
              glVertex2f(x + 1, y + 1);
              x += 8;
           } else if (c == 'k') {
              for (int i = 0; i <= 8; i++) {
                 glVertex2f(x + 1, y + i);
              }
              glVertex2f(x + 6, y + 8);
              glVertex2f(x + 5, y + 7);
              glVertex2f(x + 4, y + 6);
              glVertex2f(x + 3, y + 5);
              glVertex2f(x + 2, y + 4);
              glVertex2f(x + 2, y + 3);
              glVertex2f(x + 3, y + 4);
              glVertex2f(x + 4, y + 3);
              glVertex2f(x + 5, y + 2);
              glVertex2f(x + 6, y + 1);
              glVertex2f(x + 7, y);
              x += 8;
           } else if (c == 'l') {
              for (int i = 0; i <= 8; i++) {
                 glVertex2f(x + 1, y + i);
              }
              for (int i = 1; i <= 6; i++) {
                 glVertex2f(x + i, y);
              }
              x += 7;
           } else if (c == 'm') {
              for (int i = 0; i <= 8; i++) {
                 glVertex2f(x + 1, y + i);
                 glVertex2f(x + 7, y + i);
              }
              glVertex2f(x + 3, y + 6);
              glVertex2f(x + 2, y + 7);
              glVertex2f(x + 4, y + 5);

              glVertex2f(x + 5, y + 6);
              glVertex2f(x + 6, y + 7);
              glVertex2f(x + 4, y + 5);
              x += 8;
           } else if (c == 'n') {
              for (int i = 0; i <= 8; i++) {
                 glVertex2f(x + 1, y + i);
                 glVertex2f(x + 7, y + i);
              }
              glVertex2f(x + 2, y + 7);
              glVertex2f(x + 2, y + 6);
              glVertex2f(x + 3, y + 5);
              glVertex2f(x + 4, y + 4);
              glVertex2f(x + 5, y + 3);
              glVertex2f(x + 6, y + 2);
              glVertex2f(x + 6, y + 1);
              x += 8;
           } else if (c == 'o' || c == '0') {
              for (int i = 1; i <= 7; i++) {
                 glVertex2f(x + 1, y + i);
                 glVertex2f(x + 7, y + i);
              }
              for (int i = 2; i <= 6; i++) {
                 glVertex2f(x + i, y + 8);
                 glVertex2f(x + i, y + 0);
              }
              x += 8;
           } else if (c == 'p') {
              for (int i = 0; i <= 8; i++) {
                 glVertex2f(x + 1, y + i);
              }
              for (int i = 2; i <= 5; i++) {
                 glVertex2f(x + i, y + 8);
                 glVertex2f(x + i, y + 4);
              }
              glVertex2f(x + 6, y + 7);
              glVertex2f(x + 6, y + 5);
              glVertex2f(x + 6, y + 6);
              x += 8;
           } else if (c == 'q') {
              for (int i = 1; i <= 7; i++) {
                 glVertex2f(x + 1, y + i);
                 if (i != 1)
                    glVertex2f(x + 7, y + i);
              }
              for (int i = 2; i <= 6; i++) {
                 glVertex2f(x + i, y + 8);
                 if (i != 6)
                    glVertex2f(x + i, y + 0);
              }
              glVertex2f(x + 4, y + 3);
              glVertex2f(x + 5, y + 2);
              glVertex2f(x + 6, y + 1);
              glVertex2f(x + 7, y);
              x += 8;
           } else if (c == 'r') {
              for (int i = 0; i <= 8; i++) {
                 glVertex2f(x + 1, y + i);
              }
              for (int i = 2; i <= 5; i++) {
                 glVertex2f(x + i, y + 8);
                 glVertex2f(x + i, y + 4);
              }
              glVertex2f(x + 6, y + 7);
              glVertex2f(x + 6, y + 5);
              glVertex2f(x + 6, y + 6);

              glVertex2f(x + 4, y + 3);
              glVertex2f(x + 5, y + 2);
              glVertex2f(x + 6, y + 1);
              glVertex2f(x + 7, y);
              x += 8;
           } else if (c == 's') {
              for (int i = 2; i <= 7; i++) {
                 glVertex2f(x + i, y + 8);
              }
              glVertex2f(x + 1, y + 7);
              glVertex2f(x + 1, y + 6);
              glVertex2f(x + 1, y + 5);
              for (int i = 2; i <= 6; i++) {
                 glVertex2f(x + i, y + 4);
                 glVertex2f(x + i, y);
              }
              glVertex2f(x + 7, y + 3);
              glVertex2f(x + 7, y + 2);
              glVertex2f(x + 7, y + 1);
              glVertex2f(x + 1, y + 1);
              glVertex2f(x + 1, y + 2);
              x += 8;
           } else if (c == 't') {
              for (int i = 0; i <= 8; i++) {
                 glVertex2f(x + 4, y + i);
              }
              for (int i = 1; i <= 7; i++) {
                 glVertex2f(x + i, y + 8);
              }
              x += 7;
           } else if (c == 'u') {
              for (int i = 1; i <= 8; i++) {
                 glVertex2f(x + 1, y + i);
                 glVertex2f(x + 7, y + i);
              }
              for (int i = 2; i <= 6; i++) {
                 glVertex2f(x + i, y + 0);
              }
              x += 8;
           } else if (c == 'v') {
              for (int i = 2; i <= 8; i++) {
                 glVertex2f(x + 1, y + i);
                 glVertex2f(x + 6, y + i);
              }
              glVertex2f(x + 2, y + 1);
              glVertex2f(x + 5, y + 1);
              glVertex2f(x + 3, y);
              glVertex2f(x + 4, y);
              x += 7;
           } else if (c == 'w') {
              for (int i = 1; i <= 8; i++) {
                 glVertex2f(x + 1, y + i);
                 glVertex2f(x + 7, y + i);
              }
              glVertex2f(x + 2, y);
              glVertex2f(x + 3, y);
              glVertex2f(x + 5, y);
              glVertex2f(x + 6, y);
              for (int i = 1; i <= 6; i++) {
                 glVertex2f(x + 4, y + i);
              }
              x += 8;
           } else if (c == 'x') {
              for (int i = 1; i <= 7; i++)
                 glVertex2f(x + i, y + i);
              for (int i = 7; i >= 1; i--)
                 glVertex2f(x + i, y + 8 - i);
              x += 8;
           } else if (c == 'y') {
              glVertex2f(x + 4, y);
              glVertex2f(x + 4, y + 1);
              glVertex2f(x + 4, y + 2);
              glVertex2f(x + 4, y + 3);
              glVertex2f(x + 4, y + 4);

              glVertex2f(x + 3, y + 5);
              glVertex2f(x + 2, y + 6);
              glVertex2f(x + 1, y + 7);
              glVertex2f(x + 1, y + 8);

              glVertex2f(x + 5, y + 5);
              glVertex2f(x + 6, y + 6);
              glVertex2f(x + 7, y + 7);
              glVertex2f(x + 7, y + 8);
              x += 8;
           } else if (c == 'z') {
              for (int i = 1; i <= 6; i++) {
                 glVertex2f(x + i, y);
                 glVertex2f(x + i, y + 8);
                 glVertex2f(x + i, y + i);
              }
              glVertex2f(x + 6, y + 7);
              x += 8;
           } else if (c == '1') {
              for (int i = 2; i <= 6; i++) {
                 glVertex2f(x + i, y);
              }
              for (int i = 1; i <= 8; i++) {
                 glVertex2f(x + 4, y + i);
              }
              glVertex2f(x + 3, y + 7);
              x += 8;
           } else if (c == '2') {
              for (int i = 1; i <= 6; i++) {
                 glVertex2f(x + i, y);
              }
              for (int i = 2; i <= 5; i++) {
                 glVertex2f(x + i, y + 8);
              }
              glVertex2f(x + 1, y + 7);
              glVertex2f(x + 1, y + 6);

              glVertex2f(x + 6, y + 7);
              glVertex2f(x + 6, y + 6);
              glVertex2f(x + 6, y + 5);
              glVertex2f(x + 5, y + 4);
              glVertex2f(x + 4, y + 3);
              glVertex2f(x + 3, y + 2);
              glVertex2f(x + 2, y + 1);
              x += 8;
           } else if (c == '3') {
              for (int i = 1; i <= 5; i++) {
                 glVertex2f(x + i, y + 8);
                 glVertex2f(x + i, y);
              }
              for (int i = 1; i <= 7; i++) {
                 glVertex2f(x + 6, y + i);
              }
              for (int i = 2; i <= 5; i++) {
                 glVertex2f(x + i, y + 4);
              }
              x += 8;
           } else if (c == '4') {
              for (int i = 2; i <= 8; i++) {
                 glVertex2f(x + 1, y + i);
              }
              for (int i = 2; i <= 7; i++) {
                 glVertex2f(x + i, y + 1);
              }
              for (int i = 0; i <= 4; i++) {
                 glVertex2f(x + 4, y + i);
              }
              x += 8;
           } else if (c == '5') {
              for (int i = 1; i <= 7; i++) {
                 glVertex2f(x + i, y + 8);
              }
              for (int i = 4; i <= 7; i++) {
                 glVertex2f(x + 1, y + i);
              }
              glVertex2f(x + 1, y + 1);
              glVertex2f(x + 2, y);
              glVertex2f(x + 3, y);
              glVertex2f(x + 4, y);
              glVertex2f(x + 5, y);
              glVertex2f(x + 6, y);

              glVertex2f(x + 7, y + 1);
              glVertex2f(x + 7, y + 2);
              glVertex2f(x + 7, y + 3);

              glVertex2f(x + 6, y + 4);
              glVertex2f(x + 5, y + 4);
              glVertex2f(x + 4, y + 4);
              glVertex2f(x + 3, y + 4);
              glVertex2f(x + 2, y + 4);
              x += 8;
           } else if (c == '6') {
              for (int i = 1; i <= 7; i++) {
                 glVertex2f(x + 1, y + i);
              }
              for (int i = 2; i <= 6; i++) {
                 glVertex2f(x + i, y);
              }
              for (int i = 2; i <= 5; i++) {
                 glVertex2f(x + i, y + 4);
                 glVertex2f(x + i, y + 8);
              }
              glVertex2f(x + 7, y + 1);
              glVertex2f(x + 7, y + 2);
              glVertex2f(x + 7, y + 3);
              glVertex2f(x + 6, y + 4);
              x += 8;
           } else if (c == '7') {
              for (int i = 0; i <= 7; i++)
                 glVertex2f(x + i, y + 8);
              glVertex2f(x + 7, y + 7);
              glVertex2f(x + 7, y + 6);

              glVertex2f(x + 6, y + 5);
              glVertex2f(x + 5, y + 4);
              glVertex2f(x + 4, y + 3);
              glVertex2f(x + 3, y + 2);
              glVertex2f(x + 2, y + 1);
              glVertex2f(x + 1, y);
              x += 8;
           } else if (c == '8') {
              for (int i = 1; i <= 7; i++) {
                 glVertex2f(x + 1, y + i);
                 glVertex2f(x + 7, y + i);
              }
              for (int i = 2; i <= 6; i++) {
                 glVertex2f(x + i, y + 8);
                 glVertex2f(x + i, y + 0);
              }
              for (int i = 2; i <= 6; i++) {
                 glVertex2f(x + i, y + 4);
              }
              x += 8;
           } else if (c == '9') {
              for (int i = 1; i <= 7; i++) {
                 glVertex2f(x + 7, y + i);
              }
              for (int i = 5; i <= 7; i++) {
                 glVertex2f(x + 1, y + i);
              }
              for (int i = 2; i <= 6; i++) {
                 glVertex2f(x + i, y + 8);
                 glVertex2f(x + i, y + 0);
              }
              for (int i = 2; i <= 6; i++) {
                 glVertex2f(x + i, y + 4);
              }
              glVertex2f(x + 1, y + 0);
              x += 8;
           } else if (c == '.') {
              glVertex2f(x + 1, y);
              x += 2;
           } else if (c == ',') {
              glVertex2f(x + 1, y);
              glVertex2f(x + 1, y + 1);
              x += 2;
           } else if (c == '\n') {
              y -= 10;
              x = startX;
           } else if (c == ' ') {
              x += 8;
           }
        }
        glEnd();
     }
}