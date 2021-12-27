/*G
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FinalProject;

import com.sun.opengl.util.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.BitSet;
import javax.media.opengl.*;
import javax.swing.*;
//import Texture.AnimListener;
   
public class Air_hockey extends JFrame {
    static GLCanvas glcanvas = null;
    public static Animator animator;
   
    public static void main(String[] args) {
        new Air_hockey();
    }


    public Air_hockey() {
        GLCanvas glcanvas;
       
        
        AirHochGLEventListener listener = new AirHochGLEventListener();
        glcanvas = new GLCanvas();
        glcanvas.addGLEventListener(listener);
        
        glcanvas.addMouseListener(listener);
        glcanvas.addKeyListener(listener);
        
        getContentPane().add(glcanvas, BorderLayout.CENTER);
        animator = new FPSAnimator(15);
        animator.add(glcanvas);
        animator.start();

        setTitle("Air hockey game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 700);
        setLocationRelativeTo(null);
        setVisible(true);
        setFocusable(true);
        glcanvas.requestFocus();
    }
}

