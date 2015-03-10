
package bin.classes;

import java.awt.Color;
import java.awt.Graphics;


public class Tile 
{
    public static final int NEVER_TOUCHED = 0;
    public static final int IN_OPEN=1;
    public static final int IN_CLOSE=2;
    public static final int IN_PATH=3;
    
    public static final int NO_PARENT=-1;
    public static final int PARENT_LEFT=0;
    public static final int PARENT_LEFT_UP=1;
    public static final int PARENT_UP=2;
    public static final int PARENT_RIGHT_UP=3;
    public static final int PARENT_RIGHT=4;
    public static final int PARENT_RIGHT_DOWN=5;
    public static final int PARENT_DOWN=6;
    public static final int PARENT_LEFT_DOWN=7;
    
    
    
    public boolean pasable=true;
    public int state=NEVER_TOUCHED;
    public int parent=NO_PARENT;
    
    
    public void click()
    {
        
        pasable=!pasable;
        
    }
    
    public void draw(Graphics g, int x, int y)
    {
        Color oldColor = g.getColor();
        
        
        
        if(pasable == false)
        {
            g.fillRect(x, y, 20, 20);
            return;
        }
        
        
        if(state==NEVER_TOUCHED)
        {
            return;
        }
        
        switch(state)
        {
            case IN_OPEN:
                g.setColor(Color.GREEN);
                break;
            case IN_CLOSE:
                g.setColor(Color.BLUE);
                break;
            case IN_PATH:
                g.setColor(Color.PINK);
        }
        
        g.fillRect(x, y, 20, 20);
        g.setColor(oldColor);
        
        switch(parent)
        {
            case PARENT_LEFT:
                g.drawLine(x+2, y+10, x+10, y+10);
                break;
            case PARENT_LEFT_DOWN:
                g.drawLine(x+2, y+18, x+10, y+10);
                break;
            case PARENT_LEFT_UP:
                g.drawLine(x+2, y+2, x+10, y+10);
                break;
            case PARENT_UP:
                g.drawLine(x+10, y+2, x+10, y+10);
                break;
            case PARENT_RIGHT_UP:
                g.drawLine(x+18, y+2, x+10, y+10);
                break;
            case PARENT_RIGHT:
                g.drawLine(x+18, y+10, x+10, y+10);
                break;
            case PARENT_RIGHT_DOWN:
                g.drawLine(x+18, y+18, x+10, y+10);
                break;
            case PARENT_DOWN:
                g.drawLine(x+10, y+18, x+10, y+10);
                break;
        }
        
        g.drawOval(x+8, y+8, 4, 4);
    }
    
}
