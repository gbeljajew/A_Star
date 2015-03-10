/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bin.pane;

import bin.classes.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import java.util.*;
import javax.swing.JOptionPane;


/**
 *
 * @author gbeljajew
 */
public class MapPane extends JPanel
{
    public static final int WALL=0;
    public static final int START=1;
    public static final int END=2;
    
    public int startX=0, startY=0, endX=0, endY=0;
    
    public Tile[][] map = new Tile[24][24];
    private PathTile walkPath;
    public int clickState=0;
    public boolean runing=false;
    public boolean found = false;
    
    LinkedList<PathTile> openList = new LinkedList<>();
    LinkedList<PathTile> closeList = new LinkedList<>();
    

    public MapPane() 
    {
        for(int ix=0; ix<map.length;ix++)
            for(int iy=0; iy<map[ix].length;iy++)
            {
                map[ix][iy]=new Tile();
            }
        
        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) 
            {
                Point maus = e.getPoint();
                if(maus != null)
                {
                    
                    click(maus.x/20, maus.y/20);
                }
            }
            
            
        });
        
        
    }
    
    
    public void click(int x, int y)
    {
        
        if(x>=0 && x<map.length && y>=0 && y<map[x].length)
        {
            
            switch(clickState)
            {
                case WALL:
                    map[x][y].click();
                    break;
                case START:
                    startX=x;
                    startY=y;
                    break;
                case END:
                    endX=x;
                    endY=y;
                    break;
            }
        }
    }

    @Override
    public void paint(Graphics g) 
    {
        g.clearRect(0, 0, 480, 480);
        
        for(int i=0;i<=480;i+=20)
        {
            g.drawLine(i, 0, i, 480);
            g.drawLine(0, i, 480, i);
        }
        
        for(int ix=0; ix<map.length;ix++)
            for(int iy=0; iy<map[ix].length;iy++)
            {
                map[ix][iy].draw(g, ix*20, iy*20);
            }
        Color oldColor = g.getColor();
        g.setColor(Color.YELLOW);
        g.drawOval(startX*20, startY*20, 20, 20);
        g.setColor(Color.RED);
        g.drawOval(endX*20, endY*20, 20, 20);
        g.setColor(oldColor);
        
    }
    
    public void update()
    {
        if(openList.isEmpty())
        {
            JOptionPane.showMessageDialog(this, "there is no path");
            runing=false;
            return;
        }
        
        PathTile pt = get(openList, endX, endY);
        if(pt!=null)
        {
            walkPath = pt;
            found=true;
            return;
        }
        
        Collections.sort(openList);
        
        pt=openList.remove(0);
        closeList.add(pt);
        
        addToOpen(pt);
        
        updateMap();
        
        
        
    }
    
    public void init()
    {
        openList.clear();
        closeList.clear();
        openList.add(new PathTile(startX, startY, null, endX, endY));
        
        for(int ix=0; ix<map.length;ix++)
            for(int iy=0; iy<map[ix].length;iy++)
            {
                map[ix][iy].parent=Tile.NO_PARENT;
                map[ix][iy].state = Tile.NEVER_TOUCHED;
            }
        
        
        runing=true;
        found = false;
        
    }
    
    private PathTile get(LinkedList<PathTile> list,int x, int y)
    {
        for(PathTile pt :list)
        {
            if(pt.x==x && pt.y==y)
                return pt;
        }
        
        
        return null;
    }
    
    public void getPath()
    {
        if(walkPath.x == startX && walkPath.y == startY)
        {
            found = false;
            runing = false;
            return;
        }
        
        
        map[walkPath.x][walkPath.y].state=Tile.IN_PATH;
        
        walkPath = get(closeList,walkPath.px,walkPath.py);
        
        
        
    }
    
    private void addToOpen(PathTile pt)
    {
        int xMin=pt.x-1;
        if(xMin<0)xMin=0;
        
        int xMax=pt.x+1;
        if(xMax>23)xMax=23;
        
        int yMin=pt.y-1;
        if(yMin<0)yMin=0;
        
        int yMax=pt.y+1;
        if(yMax>23)yMax=23;
        
        for(int ix=xMin;ix<=xMax;ix++)
            for(int iy=yMin;iy<=yMax;iy++)
            {
                if(pt.x==ix && pt.y==iy);
                else
                {
                    if(map[ix][iy].pasable)
                    {
                        PathTile ptn = get(openList, ix, iy);
                        if(ptn==null)
                        {
                            ptn = get(closeList, ix, iy);
                            if(ptn==null)
                                openList.add(new PathTile(ix, iy, pt, endX, endY));
                            
                        }else
                        {
                            int g = pt.g;
                            if(pt.x==ix || pt.y==iy)
                                g+=10;
                            else
                                g+=14;
                            
                            if(ptn.g>g)
                                ptn.setParent(pt, endX, endY);
                            
                        }
                        
                    }
                    
                }
            }
    }
    
    private void updateMap()
    {
        for(PathTile pt : openList)
        {
            Tile t = map[pt.x][pt.y];
            t.state=Tile.IN_OPEN;
            
            if(pt.x==pt.px)
            {
                if(pt.y<pt.py)
                    t.parent=Tile.PARENT_DOWN;
                else
                    t.parent=Tile.PARENT_UP;
            }else if(pt.x<pt.px)
            {
                if(pt.y==pt.py)
                    t.parent=Tile.PARENT_RIGHT;
                else if(pt.y<pt.py)
                    t.parent=Tile.PARENT_RIGHT_DOWN;
                else
                    t.parent=Tile.PARENT_RIGHT_UP;
                
            }else
            {
                if(pt.y==pt.py)
                    t.parent=Tile.PARENT_LEFT;
                else if(pt.y<pt.py)
                    t.parent=Tile.PARENT_LEFT_DOWN;
                else
                    t.parent=Tile.PARENT_LEFT_UP;
            }
        }
        
        for(PathTile pt : closeList)
        {
            Tile t = map[pt.x][pt.y];
            t.state=Tile.IN_CLOSE;
            
            if(pt.x==pt.px)
            {
                if(pt.y<pt.py)
                    t.parent=Tile.PARENT_DOWN;
                else
                    t.parent=Tile.PARENT_UP;
            }else if(pt.x<pt.px)
            {
                if(pt.y==pt.py)
                    t.parent=Tile.PARENT_RIGHT;
                else if(pt.y<pt.py)
                    t.parent=Tile.PARENT_RIGHT_DOWN;
                else
                    t.parent=Tile.PARENT_RIGHT_UP;
                
            }else
            {
                if(pt.y==pt.py)
                    t.parent=Tile.PARENT_LEFT;
                else if(pt.y<pt.py)
                    t.parent=Tile.PARENT_LEFT_DOWN;
                else
                    t.parent=Tile.PARENT_LEFT_UP;
            }
        }
    }
}
