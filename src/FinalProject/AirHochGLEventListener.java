/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FinalProject;

import static FinalProject.AirHochGLEventListener.ispause;
import java.awt.event.MouseListener;
import com.sun.opengl.util.j2d.TextRenderer;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import static java.lang.System.exit;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.media.opengl.*;

import java.util.BitSet;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.glu.GLU;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
//import Texture.AnimListener;
//import Texture.TextureReader;
import java.io.FileNotFoundException;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class AirHochGLEventListener extends AnimListener {

    private final int[] oop_right = {3, 5, 7};  // left, up left, down left
    private final int[] oop_left = {2, 4, 6}; // right, up right, down right
    private final int[] oop_down = {0, 4, 5}; //up, up right, up left
    private final int[] oop_up = {1, 7, 6}; //down, down left, down right
    private final int[] oop_up_right = {7, 1, 3}; // down left, down, left
    private final int[] oop_up_left = {1, 2, 6}; //down, down right, right
    private final int[] oop_down_righ = {0, 3, 5}; // up, left, up left
    private final int[] oop_down_left = {0, 4, 2}; // up, up right, right

    int timer = 0;
    String time = java.time.LocalTime.now() + "";
    GLCanvas glc;

    boolean exit = false;

    public static boolean ispause = false;
    GLAutoDrawable gldddd;
    TextRenderer renderer = new TextRenderer(new Font("SanasSerif", Font.BOLD, 20));
    boolean DrawBall = true;
    int xPosition = 90;
    int yPosition = 90;
    int animationIndex = 0;
    int maxWidth = 100;
    int maxHeight = 100;
    int xPlayer1 = 42, yPlayer1 = 20;
    int xPlayer2 = 42, yPlayer2 = 90;
    int xBall = 54, yBall = 45;
    boolean touchBall = false;
    boolean touchBall2 = false;
    int ballDirection = 0;
    int scor;
    //0=up 1 = down  2=right  3=left  4=up_right   5=up_left  6 =down_right  7=down_left
    private static final int[] DI = new int[]{0, 0, 1, -1, 1, -1, 1, -1};
    private static final int[] DJ = new int[]{1, -1, 0, 0, 1, 1, -1, -1};
    private static final int TOUCH_MARGEN_X = 16;
    private static final int TOUCH_MARGEN_Y = 1;
    private static final int TOUCH_MARGEN_X2 = 16;
    private static final int TOUCH_MARGEN_Y2 = 1;

    boolean how = false;
    boolean player1 = false, Easy = false, Normal = false, Hard = false, sound = false,
            player2 = false, home = true;
    FileInputStream music;
    AudioStream audios;

    // Download enemy textures from https://craftpix.net/freebies/free-monster-2d-game-items/
    String textureNames[] = {"GUI.png", "oneplayer.png", "Pball.png", "blackball.png", "easylevel1.png",
        "twoplayers.png", "Gball.png", "easymenue.png", "howtoplay.png", "easylevel2.png", "easylevel3.png",
        "hardlevel1.png", "hardlevel2.png", "hardlevel3.png", "hardmenue.png", "mediumlevel1.png", "mediumlevel2.png",
        "mediumlevel3.png", "mediummenue.png"};
    TextureReader.Texture texture[] = new TextureReader.Texture[textureNames.length];
    int textures[] = new int[textureNames.length];

    public void setGLCanvas(GLCanvas glc) {
        this.glc = glc;
    }

    @Override
    public void init(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);    //This Will Clear The Background Color To Black
        try {
            music = new FileInputStream(new File("Back_1.wav"));
            audios = new AudioStream(music);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        AudioPlayer.player.start(audios);
        gl.glEnable(GL.GL_TEXTURE_2D);  // Enable Texture Mapping
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glGenTextures(textureNames.length, textures, 0);

        for (int i = 0; i < textureNames.length; i++) {
            try {
                texture[i] = TextureReader.readTexture(assetsFolderName + "//" + textureNames[i], true);
                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);

//                mipmapsFromPNG(gl, new GLU(), texture[i]);
                new GLU().gluBuild2DMipmaps(
                        GL.GL_TEXTURE_2D,
                        GL.GL_RGBA, // Internal Texel Format,
                        texture[i].getWidth(), texture[i].getHeight(),
                        GL.GL_RGBA, // External format from image,
                        GL.GL_UNSIGNED_BYTE,
                        texture[i].getPixels() // Imagedata
                );
            } catch (IOException e) {
                System.out.println(e);
                e.printStackTrace();
            }
        }
    }

    @Override
    public void display(GLAutoDrawable gld) {
        timer++;
        gldddd = gld;
        GL gl = gld.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);       //Clear The Screen And The Depth Buffer
        gl.glLoadIdentity();

        renderer.beginRendering(gld.getWidth(), gld.getHeight());

        renderer.endRendering();

        if (home) {
            DrawBackgroundhom(gl);
            try {
                DrawTime();
            } catch (ParseException ex) {
//                Logger.getLogger(Air_hockey.AirHochGLEventListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (player1) {
            DrawEHM(gl);

        }
        if (Easy) {
            DrawBackgrPlayer1(gl);
            DrawSprite(gl, xPlayer1, yPlayer1, 2, 6f);
            DrawSprite(gl, xPlayer2, yPlayer2, 2, 6f);
            DrawBall(gl, xBall, yBall, 3, 6f);
            moveBall();
            handleBallCollsion();
            handleBallCollsion2();
            handleKeyPress();
            handleKeyPress1();
            scorAirHockey(xBall, yBall);
            if (yBall == 80 || yBall == 15) {
                DrawBall = true;
                yBall = 45;
                xBall = 54;
            }
        }
        if (Normal) {
            DrawBackgrPlayer1(gl);
            DrawSprite(gl, 42, 20, 2, 6f);
            DrawSprite(gl, 34, 73, 6, 6f);
            DrawSprite(gl, 53, 45, 3, 6f);
            handleKeyPress();
        }
        if (Hard) {
            DrawBackgrPlayer1(gl);
            DrawSprite(gl, 42, 20, 2, 6f);
            DrawSprite(gl, 34, 73, 6, 6f);
            DrawSprite(gl, 53, 45, 3, 6f);
            handleKeyPress();
        }

        if (player2) {
            DrawBackgrPlayer1(gl);
            DrawSprite(gl, xPlayer1, yPlayer1, 2, 6f);
            DrawSprite(gl, xPlayer2, yPlayer2, 2, 6f);
            DrawBall(gl, xBall, yBall, 3, 6f);
            moveBall();
            handleBallCollsion();
            handleBallCollsion2();
            handleKeyPress();
            handleKeyPress1();
            scorAirHockey(xBall, yBall);
            if (yBall == 80 || yBall == 15) {
                DrawBall = true;
                yBall = 45;
                xBall = 54;
            }
            if (ispause) {
                DrawPause(gl);
            }
        }
        if (how) {
            DrawHow(gl);
        }
        if (exit) {
            System.exit(0);
        }
    }

    @Override
    public void reshape(GLAutoDrawable glad, int i, int i1, int i2, int i3) {
    }

    @Override
    public void displayChanged(GLAutoDrawable glad, boolean bln, boolean bln1) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(final KeyEvent event) {
        int keyCode = event.getKeyCode();
        keyBits.set(keyCode);

        if (isKeyPressed(KeyEvent.VK_P)) {
            if (!ispause) {
                ispause = !ispause;
                gldddd.repaint();
                Air_hockey.animator.stop();
                gldddd.repaint();
            } else {
                ispause = !ispause;
                gldddd.repaint();
                Air_hockey.animator.start();

            }

        }
    }

    @Override
    public void keyReleased(final KeyEvent event) {
        int keyCode = event.getKeyCode();
        keyBits.clear(keyCode);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        double x4 = e.getX();
        double y4 = e.getY();

//        System.out.println(x + " " + y);
        Component c = e.getComponent();
        double width = c.getWidth();
        double height = c.getHeight();
//        System.out.println(width + " " + height);
//get percent of GLCanvas instead of
//points and then converting it to our
//'100' based coordinate system.
        xPosition = (int) ((x4 / width) * 100);
        yPosition = ((int) ((y4 / height) * 100));
//reversing direction of y axis
        yPosition = 100 - yPosition;
        if (home) {
            if (xPosition <= 45 && xPosition >= 5 && yPosition <= 80 && yPosition >= 68) {
                player1 = true;
                home = false;
                //player1
            }
            if ((xPosition >= 30 && xPosition <= 70) && (yPosition >= 40 && yPosition <= 50)) {
                exit = true;
                //exit
            }
            if ((xPosition >= 53 && xPosition <= 95) && (yPosition >= 55 && yPosition <= 65)) {
                home = false;
                how = true;
                //how to play
            }

            if (xPosition <= 95 && xPosition >= 54 && yPosition <= 80 && yPosition >= 70) {
                player2 = true;
                home = false;
                //player 2
            }
            if (xPosition <= 45 && xPosition >= 5 && yPosition <= 65 && yPosition >= 55) {
                player2 = true;
                home = false;
                //high scor
            }
            if (xPosition <= 91 && xPosition >= 76 && yPosition <= 14 && yPosition >= 8) {
                sound = !sound;
                onOrOffSound();
                home = true;
                //sound
            }
        }

        if (player1) {
            if (xPosition <= 25 && xPosition >= 7 && yPosition <= 30 && yPosition >= 19) {
                player1 = false;
                home = true;
                //return to homme
            }
            if (xPosition <= 65 && xPosition >= 15 && yPosition <= 80 && yPosition >= 74) {
                Easy = true;
                player1 = false;
                //easy
            }
            if (xPosition <= 85 && xPosition >= 15 && yPosition <= 67 && yPosition >= 60) {
                Normal = true;
                player1 = false;
                //mid
            }
            if (xPosition <= 67 && xPosition >= 15 && yPosition <= 55 && yPosition >= 45) {
                Hard = true;
                player1 = false;
                //hard
            }
        }

        if (how) {

            if (xPosition <= 23 && xPosition >= 9 && yPosition <= 95 && yPosition >= 85) {
                how = false;
                home = true;
                //return of how
            }
        }
        System.out.println(xPosition + " " + yPosition);
        //  glc.repaint();

    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    public boolean isKeyPressed(final int keyCode) {
        return keyBits.get(keyCode);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    // This method draws GUI, Instructions,Oneplayer, TwoPlayers
    public void DrawHome(GL gl, int index) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);	// Turn Blending On
        gl.glPushMatrix();
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();
        gl.glDisable(GL.GL_BLEND);
    }

    public void DrawSprite(GL gl, int x, int y, int index, float scale) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);	// Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - 0.9, y / (maxHeight / 2.0) - 0.9, 0);
        gl.glScaled(0.1 * scale, 0.1 * scale, 1);
        //System.out.println(x +" " + y);
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void DrawBall(GL gl, int x, int y, int index, float scale) {

        if (!DrawBall) {
            return;
        }
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);	// Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - 0.9, y / (maxHeight / 2.0) - 0.9, 0);
        gl.glScaled(0.1 * scale, 0.1 * scale, 1);
        //System.out.println(x +" " + y);
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void DrawPause(GL gl) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[7]);	// Turn Blending On

        gl.glPushMatrix();
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void DrawEHM(GL gl) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[1]);	// Turn Blending On

        gl.glPushMatrix();
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void DrawBackgroundhom(GL gl) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[0]);	// Turn Blending On

        gl.glPushMatrix();
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void DrawHow(GL gl) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[8]);	// Turn Blending On

        gl.glPushMatrix();
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void DrawBackgrPlayer1(GL gl) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[4]);	// Turn Blending On

        gl.glPushMatrix();
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void DrawBackgrScore(GL gl, int index) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);	// Turn Blending On

        gl.glPushMatrix();
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void DrawTime() throws ParseException {
        int sec1 = Integer.parseInt((java.time.LocalTime.now()).toString().substring(6, 8)),
                min1 = Integer.parseInt((java.time.LocalTime.now()).toString().substring(3, 5));
        String time1 = time;
        String time2 = java.time.LocalTime.now() + "";

        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date date1 = format.parse(time1);
        Date date2 = format.parse(time2);
        long difference = date2.getTime() - date1.getTime();

        String fi = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(difference),
                TimeUnit.MILLISECONDS.toSeconds(difference)
                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(difference))
        );

        renderer.beginRendering(gldddd.getWidth(), gldddd.getHeight());
        renderer.draw(fi, 600, 620);
        renderer.endRendering();
    }

    public void handleKeyPress() {

        if (isKeyPressed(KeyEvent.VK_LEFT)) {
            if (xPlayer1 > 12) {
                //0=up 1 = down  2=right  3=left  4=up_right   5=up_left  6 =down_right  7=down_left
                xPlayer1--;

            }
        }
        if (isKeyPressed(KeyEvent.VK_RIGHT)) {
            if (xPlayer1 < 72) {
                xPlayer1++;
            }
        }
        if (isKeyPressed(KeyEvent.VK_UP)) {
            if (yPlayer1 < 48) {
                yPlayer1++;
            }
        }
        if (isKeyPressed(KeyEvent.VK_DOWN)) {
            if (yPlayer1 > 20) {
                yPlayer1--;

            }
        }

    }

    public void handleKeyPress1() {

        if (isKeyPressed(KeyEvent.VK_W)) {
            if (yPlayer2 < 90) {
                yPlayer2++;
                //bake
            }
        }
        if (isKeyPressed(KeyEvent.VK_S)) {
            if (yPlayer2 > 62) {
                yPlayer2--;
                //up
            }
        }

        if (isKeyPressed(KeyEvent.VK_A)) {
            if (xPlayer2 > 12) {
                xPlayer2--;
                //left
            }
        }
        if (isKeyPressed(KeyEvent.VK_D)) {
            if (xPlayer2 < 73) {
                xPlayer2++;
                //righ
            }
        }

    }

    public BitSet keyBits = new BitSet(256);

    private void moveBall() {
        if (!touchBall) {
            return;
        }
        xBall += DI[ballDirection];
        yBall += DJ[ballDirection];
        //UP
        if (ballDirection == 0) {
            if (yBall > 85) {
                ballDirection = oop_up[random(0, 2)];
            }
        }
        //DOWN
        if (ballDirection == 1) {
            if (yBall < 12) {
                ballDirection = oop_down[random(0, 2)];
            }
        }
        //right
        if (ballDirection == 2) {
            if (xBall > 85) {
                ballDirection = oop_right[random(0, 2)];
            }
        }
        //left
        if (ballDirection == 3) {
            if (xBall < 12) {
                ballDirection = oop_left[random(0, 2)];
            }
        }
        //up_right
        if (ballDirection == 4) {
            if (xBall > 85 || yBall > 85) {
                ballDirection = oop_up_right[random(0, 2)];
            }
        }
        //up_left
        if (ballDirection == 5) {
            if (xBall < 12 || yBall > 85) {
                ballDirection = oop_up_left[random(0, 2)];
            }
        }
        //down_right
        if (ballDirection == 6) {
            if (xBall > 85 || yBall < 12) {
                ballDirection = oop_down_righ[random(0, 2)];
            }
        }
        //down_left
        if (ballDirection == 7) {
            if (xBall < 12 || yBall < 12) {
                ballDirection = oop_down_left[random(0, 2)];
            }
        }
    }

    //0=up 1 = down  2=right  3=left  4=up_right   5=up_left  6 =down_right  7=down_left
//private static final int[] DI = new int[]{0, 0, 1, -1, 1, -1, 1, -1};
//    private static final int[] DJ = new int[]{1, -1, 0, 0, 1, 1, -1, -1};
//    private static final int TOUCH_MARGEN = 5;
    private void handleBallCollsion() {
//        System.out.println("here handleBallCollsion");
        if (xFirstPlayerCollsion() && yFirstPlayerCollsion()) {
            //   System.out.println("*** enter");
            touchBall = true;
            if (xBall == xPlayer1) {
                if (yBall >= yPlayer1) {
                    ballDirection = 0; //up
                } else {
                    ballDirection = 1; //down
                }
            } else if (yBall == yPlayer1) {
                if (xBall >= xPlayer1) {
                    ballDirection = 2; //right
                } else {
                    ballDirection = 3; //left
                }
            } //0=up 1 = down  2=right  3=left  4=up_right   5=up_left  6 =down_right  7=down_left
            else if (xBall > xPlayer1 && yBall > yPlayer1) {

                ballDirection = 4; //up right
                System.out.println("sayed");
            } else if (xBall < xPlayer1 && yBall < yPlayer1) {
                ballDirection = 7; //down left
            } else if (xBall < xPlayer1 && yBall > yPlayer1) {
                ballDirection = 6; //down right
            } else if (xBall > xPlayer1 && yBall < yPlayer1) {
                ballDirection = 5; //up left
            }

        }
    }

    private boolean xFirstPlayerCollsion() {
        return xPlayer1 <= xBall + TOUCH_MARGEN_X && xPlayer1 >= xBall - TOUCH_MARGEN_X;
    }

    private boolean yFirstPlayerCollsion() {
        return yPlayer1 <= yBall + TOUCH_MARGEN_Y && yPlayer1 >= yBall - TOUCH_MARGEN_Y;
    }

    private void handleBallCollsion2() {

        if (xsocoundPlayerCollsion() && ysoucndPlayerCollsion()) {
            //   System.out.println("*** enter");
            touchBall2 = true;
            if (xBall == xPlayer2) {
                if (yBall <= yPlayer2) {
                    ballDirection = 1;
                } else {
                    ballDirection = 0;
                }
            } else if (yBall == yPlayer2) {
                if (xBall >= xPlayer2) {
                    ballDirection = 2;
                } else {
                    ballDirection = 3;
                }
            } //0=up 1 = down  2=right  3=left  4=up_right   5=up_left  6 =down_right  7=down_left
            else if (xBall > xPlayer2 && yBall > yPlayer2) {
                ballDirection = 7;
                System.out.println("sayed");
            } else if (xBall < xPlayer2 && yBall > yPlayer2) {
                ballDirection = 6;
            } else if (xBall < xPlayer2 && yBall < yPlayer2) {
                ballDirection = 4;
            } else if (xBall > xPlayer2 && yBall < yPlayer2) {
                ballDirection = 5;
            }
        }
    }

    private boolean xsocoundPlayerCollsion() {
        return xPlayer2 <= xBall + TOUCH_MARGEN_X2 && xPlayer2 >= xBall - TOUCH_MARGEN_X2;
    }

    private boolean ysoucndPlayerCollsion() {
        return yPlayer2 <= yBall + TOUCH_MARGEN_Y2 && yPlayer2 >= yBall - TOUCH_MARGEN_Y2;
    }

    public int random(double a, double b) {
        return (int) (a + (int) (Math.random() * (b - a)));
    }

    private void onOrOffSound() {
        try {

            System.out.println(sound);
            if (sound) {
                System.out.println("here");
                AudioPlayer.player.stop(audios);

            } else {
                AudioPlayer.player.start(audios);
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void scorAirHockey(int xBall, int yBall) {

        if (xBall >= 33 && xBall <= 66 && yBall == 15 || yBall == 80) {

            scor++;
            System.out.println("collect " + scor);
            DrawBall = false;

        }

    }

}
