/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FinalProject;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.media.opengl.GLEventListener;

public abstract class AnimListener implements GLEventListener, KeyListener, MouseListener, MouseMotionListener {
 
    protected String assetsFolderName = "AirHockeyIM";
    
}