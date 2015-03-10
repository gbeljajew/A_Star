
package bin.classes;


public class PathTile implements Comparable<PathTile>
{
    public int x,y,px,py,f,g,h;

    public PathTile(int x, int y, PathTile parent, int tx, int ty) 
    {
        this.x = x;
        this.y = y;
        
        setParent(parent, tx, ty);
    }
    
    public final void setParent(PathTile parent, int tx, int ty)
    {
        if(parent == null)
        {
            px=x;
            py=y;
            h=(Math.abs(x-tx)+Math.abs(y-ty))*10;
            g=0;
            f=h;
            return;
        }
        px=parent.x;
        py=parent.y;
        
        h=(Math.abs(x-tx)+Math.abs(y-ty))*10;
        
        if(x == px || y == py)
        {
            g = parent.g+10;
        }
        else
        {
            g = parent.g+14;
        }
        
        f = g+h;
    }

    @Override
    public int compareTo(PathTile o) 
    {
        return this.f-o.f;
    }
}
