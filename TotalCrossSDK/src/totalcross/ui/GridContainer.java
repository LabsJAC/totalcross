package totalcross.ui;

import totalcross.ui.event.*;
import totalcross.ui.font.*;
import totalcross.ui.gfx.*;

public class GridContainer extends Container
{
   public static final int HORIZONTAL_ORIENTATION = 0;
   public static final int VERTICAL_ORIENTATION = 1;
   
   private int orientation, cols, rows, rpp, pageCount;
   private ScrollContainer sc;
   private Cell[] cells;
   private ArrowButton btFirst, btLast;
      
   /** The container that has the page number and first/last arrows.
    * Only works when orientation is horizontal, and is null otherwise. */
   public NumericPagePosition pagepos;
   
   public GridContainer(int orientation)
   {
      this.orientation = orientation;
      sc = new ScrollContainer(orientation == HORIZONTAL_ORIENTATION, orientation == VERTICAL_ORIENTATION)
      {
         public int getScrollDistance()
         {
            return cells.length >= cols ? cells[cols-1].getX2()+1 : 0;
         }
      };
      pagepos=new NumericPagePosition();
      if (orientation == HORIZONTAL_ORIENTATION)
         sc.flick.setPagePosition(pagepos);
   }
   
   public Flick getFlick()
   {
      return sc.flick;
   }
   
   public static class Cell extends Container
   {
      int inX,inY;
      
      public Cell()
      {
         focusTraversable = true;
      }
      public void onEvent(Event e)
      {
         switch (e.type)
         {
            case PenEvent.PEN_DOWN:
            {
               PenEvent pe = (PenEvent)e;
               inX = pe.x;
               inY = pe.y;
               break;
            }
            case PenEvent.PEN_UP:
            {
               PenEvent pe = (PenEvent)e;
               int threeshold = width/4;
               int dx = inX-pe.x; if (dx < 0) dx = -dx;
               int dy = inY-pe.y; if (dy < 0) dy = -dy;
               if (dx < threeshold && dy < threeshold && !hadParentScrolled())
                  postPressedEvent();
               break;
            }
         }
      }
   }

   public void onFontChanged()
   {
      sc.setFont(font);
   }
   
   public void initUI()
   {
      boolean isHoriz = orientation == HORIZONTAL_ORIENTATION;
      if (isHoriz)
      {
         pagepos.setBackColor(Color.darker(backColor,16));
         add(pagepos,CENTER,BOTTOM,PARENTSIZE+30,fmH);
         pagepos.setPosition(1);      
         sc.flick.setScrollDistance(width);
      }
      btFirst = new ArrowButton(isHoriz ? Graphics.ARROW_LEFT : Graphics.ARROW_UP,fmH/2,foreColor);
      btFirst.setBorder(Button.BORDER_NONE);
      add(btFirst,LEFT,BOTTOM,PARENTSIZE+(isHoriz?35:50),fmH);
      if (!isHoriz) btFirst.setArrowSize(fmH/2);
      
      add(sc,LEFT,TOP,FILL,FIT);
      
      btLast = new ArrowButton(isHoriz ? Graphics.ARROW_RIGHT : Graphics.ARROW_DOWN,fmH/2,foreColor);
      btLast.setBorder(Button.BORDER_NONE);
      add(btLast,RIGHT,BOTTOM,PARENTSIZE+(isHoriz?35:50),fmH);
      if (!isHoriz) btLast.setArrowSize(fmH/2);
      sc.flick.forcedFlickDirection = orientation == HORIZONTAL_ORIENTATION ? Flick.HORIZONTAL_DIRECTION_ONLY : Flick.VERTICAL_DIRECTION_ONLY;
   }
   
   public void setRowsPerPage(int rpp)
   {
      this.rpp = rpp;
   }
   
   public void setPageSize(int cols, int rows)
   {
      this.cols = cols;
      this.rows = rows;
   }

   public void setCells(Cell[] cells)
   {
      sc.removeAll();
      if (rpp != 0)
         setFont(Font.getFont(font.isBold(),Math.min(height,width)/rows/rpp));
      this.cells = cells;
      int percX = PARENTSIZE - cols;
      int percY = PARENTSIZE - rows;
      int px = LEFT, py = TOP;
      int cr = cols*rows;
      pageCount = cells.length / cr;
      if ((cells.length % cr) != 0) pageCount++;
      if (orientation == VERTICAL_ORIENTATION)
         for (int z = 1; z <= cells.length; z++)
         {
            sc.add(cells[z-1],px,py,percX,percY);
            if ((z%cols) != 0) 
               {px = AFTER; py = SAME;}
            else 
               {px = LEFT; py = AFTER;}
         }
      else
      {
         pagepos.setCount(pageCount);
         pagepos.setPosition(1);
         Control last = null;
         for (int z = 1, idx = cols-1; z <= cells.length; z++)
         {
            sc.add(cells[z-1],px,py,percX,percY,last);
            last = null;
            if ((z%cols) != 0) // same row
               {px = AFTER; py = SAME;}
            else // change row
            if (z < cr)
               {px = LEFT; py = AFTER;}
            else
            {
               last = cells[idx];
               idx += cols;
            }
         }
         // fill with spacers the rest of the columns if the last page has less than one column filled
         int remains = cols - cells.length % cr;
         if (remains > 0 && remains < cols)
            for (int i = 0; i < remains; i++)
               sc.add(new Spacer(0,0),AFTER,SAME,percX,1);
         if (cells.length >= cols)
            sc.flick.setScrollDistance(cells[cols-1].getX2()+1);
      }
   }
   
   public void onEvent(Event e)
   {
      if (e.type == ControlEvent.PRESSED)
      {
         if (e.target == btFirst)
            sc.scrollToPage(1);
         else
         if (e.target == btLast)
            sc.scrollToPage(pageCount);
      }
   }
}
