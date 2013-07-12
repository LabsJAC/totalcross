package tc.samples.ui.controls;

import totalcross.io.*;
import totalcross.sys.*;
import totalcross.ui.*;
import totalcross.ui.dialog.*;
import totalcross.ui.event.*;
import totalcross.ui.gfx.*;
import totalcross.ui.image.*;
import totalcross.unit.*;

public class OtherControlsSample extends BaseContainer
{
   Label lStatus;
   ScrollContainer sc;
   
   public void initUI()
   {
      try
      {
         super.initUI();
         setTitle("Other controls");
         sc = new ScrollContainer(false, true);
         sc.setInsets(gap,gap,gap,gap);
         add(sc,LEFT,TOP,FILL,FILL);

         sc.add(lStatus = new Label("",CENTER), LEFT,AFTER);
         lStatus.setHighlighted(true);

         sc.add(new Label("TimeBox"),LEFT,AFTER+gap);
         addClock();

         final Button btnInput;
         sc.add(new Label("InputBox"),LEFT,AFTER+gap);
         sc.add(btnInput = new Button("Click to input your name"),LEFT,AFTER);
         btnInput.addPressListener(new PressListener()
         {
            public void controlPressed(ControlEvent e)
            {
               InputBox ib = new InputBox("InputBox","Please enter your name:","");
               ib.popup();
               String s = ib.getValue();
               if (s != null) lStatus.setText(s);
            }
         });

         final Button btnChooseColor;
         sc.add(new Label("ColorChooserBox"),LEFT,AFTER+gap);
         sc.add(btnChooseColor = new Button("Choose new background color"),LEFT,AFTER);
         btnChooseColor.addPressListener(new PressListener()
         {
            public void controlPressed(ControlEvent e)
            {
               ColorChooserBox ccb = new ColorChooserBox(getBackColor());
               ccb.popup();
               if (ccb.choosenColor != -1)
               {
                  sc.setBackColor(ccb.choosenColor);
                  Control[] c = sc.getBagChildren();
                  for (int i =0; i < c.length; i++)
                     c[i].setBackColor(ccb.choosenColor);
                  repaint();
               }
            }
         });

         sc.add(new Label("SpinList"),LEFT,AFTER+gap);
         SpinList sl;
         sc.add(sl = new SpinList(new String[]{"Today","Day [1,31]"}, !Settings.fingerTouch),LEFT,AFTER,Settings.fingerTouch?FILL:PREFERRED,PREFERRED);
         sl.hAlign = CENTER;
  
         sc.add(new Label("Slider"),LEFT,AFTER+gap);
         sc.add(sb1 = new Slider(ScrollBar.HORIZONTAL),LEFT,AFTER, SCREENSIZE+50, PREFERRED);
         sb1.drawTicks = true;
         sb1.setLiveScrolling(true);
         sb1.setValues(1,1,1,6);
   
         sc.add(new Label("Horizontal ScrollBar"),LEFT,AFTER+gap);
         sc.add(sb2 = new ScrollBar(ScrollBar.HORIZONTAL), LEFT,AFTER, SCREENSIZE+50, PREFERRED);
         sb2.setVisibleItems(10);
         sb2.setValues(1,1,1,6);

         sc.add(new Label("Ruler"),LEFT,AFTER+gap);
         sc.add(new Ruler(),LEFT,AFTER+gap);

         sc.add(new Label("FileChooser"),LEFT,AFTER+gap);
         addFileChooser();
      }
      catch (Exception ee)
      {
         MessageBox.showException(ee,true);
      }
   }

   Button btnClock;
   Slider sb1;
   ScrollBar sb2;
   
   private void addClock() throws ImageException
   {
      Image clock = new Image(fmH,fmH);
      int xx = clock.getWidth()-1;
      int yy = clock.getHeight()-1;
      Graphics g = clock.getGraphics();
      g.foreColor = Color.BLUE;
      g.drawCircle(xx/2,yy/2,xx/2);
      g.drawLine(xx/2,yy/2,xx,yy/2);
      g.drawLine(xx/2,yy/2,xx/2,yy/3);
      btnClock = new Button(clock);
      btnClock.setBorder(Button.BORDER_NONE);
      sc.add(btnClock, LEFT, AFTER);
   }
   
   public void addFileChooser()
   {
      final Button btn,btn2;
      final Check ch;
      sc.add(ch = new Check("Multiple selection"),LEFT,AFTER);
      sc.add(btn = new Button("Choose file"),LEFT,AFTER+gap);
      sc.add(btn2 = new Button("Delete file"),AFTER+gap,SAME);
      btn2.setEnabled(false);
      btn.addPressListener(
         new PressListener()
         {
            public void controlPressed(ControlEvent e)
            {
               try
               {
                  FileChooserBox fcb = new FileChooserBox(null);
                  fcb.multipleSelection = ch.isChecked(); // guich@tc115_4
                  fcb.mountTree("device/");
                  fcb.popup();
                  String s = fcb.getAnswer();
                  if (s == null)
                     lStatus.setText("Cancelled");
                  else
                  if (fm.stringWidth(s) > getWidth())
                     lStatus.setMarqueeText(s, 100, 1, -8);
                  else
                     lStatus.setText(s);
                  btn2.setEnabled(s != null);
               }
               catch (Exception ee)
               {
                  MessageBox.showException(ee,true);
               }
            }
         });
      btn2.addPressListener(new PressListener()
      {
         public void controlPressed(ControlEvent e)
         {
            try
            {
               String s = lStatus.getText();
               new File(s,File.DONT_OPEN).delete();
            }
            catch (Exception ee)
            {
               MessageBox.showException(ee,false);
            }
         }
      });
   }

   public void onEvent(Event event)
   {
      if (event instanceof UIRobotEvent)
         lStatus.setMarqueeText(event.type == UIRobotEvent.ROBOT_SUCCEED ? "Robot succeed" : "Robot failed: "+((UIRobotEvent)event).failureReason, 100,1,-5);
      else
      if (event.type == ControlEvent.PRESSED)
      {
         if (event.target == btnClock)
         {
            TimeBox tb = new TimeBox();
            tb.popup();
            lStatus.setText(tb.getTime().toString());
         }
         else
         if (event.target == sb1 || event.target == sb2)
         {
            int value = ((ScrollBar)event.target).getValue();
            sb1.setValue(value);
            sb2.setValue(value);
         }
         else
         if (event.target instanceof MultiListBox)
            lStatus.setText("Last selected: "+((MultiListBox)event.target).getLastSelectedItem());
      }
   }
}
