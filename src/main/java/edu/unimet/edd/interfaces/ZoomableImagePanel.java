/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.unimet.edd.interfaces;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;



/**
 * * A custom panel that displays an image with zoom functionality.
 * 
 * <p>Users can zoom in and out using the mouse scroll wheel.</p>
 *
 * @author carlos
 */
public class ZoomableImagePanel extends JPanel {
    private BufferedImage image;
    private double scale = 1.0; // Initial zoom level

    
   /**
    * Constructs a ZoomableImagePanel with the specified image file.
    * 
    * @param imagePath the path to the image file to be displayed.
    */
    public ZoomableImagePanel(String imagePath) throws Exception {
        this.image = ImageIO.read(new File(imagePath));
        
        // Add mouse wheel listener for zoom
        addMouseWheelListener(e -> {
            if (e.getPreciseWheelRotation() < 0) {
                scale *= 1.1; // Zoom in
            } else {
                scale *= 0.9; // Zoom out
            }
            revalidate();
            repaint();
        });
    }
    
    
   /**
    * Paints the image onto the panel, applying the current zoom factor.
    * 
    * @param g the Graphics object used for drawing.
    */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            Graphics2D g2d = (Graphics2D) g;
            int width = (int) (image.getWidth() * scale);
            int height = (int) (image.getHeight() * scale);
            g2d.drawImage(image, 0, 0, width, height, this);
        }
    }

    
   /**
    * Returns the preferred size of the panel, adjusted based on the current zoom scale.
    * 
    * @return the preferred size as a Dimension object.
    */
    @Override
    public Dimension getPreferredSize() {
        if (image != null) {
            return new Dimension((int) (image.getWidth() * scale), (int) (image.getHeight() * scale));
        }
        return super.getPreferredSize();
    }
}