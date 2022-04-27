package Main;

import javax.swing.*;

public class MyJButton extends JButton
{
  private static final long serialVersionUID = 2L;
  public final int ROW;
  public final int COL;
  
  public MyJButton(String text, int row, int col)
  {
    super(text);
    this.ROW = row;
    this.COL = col;
  } 
}
