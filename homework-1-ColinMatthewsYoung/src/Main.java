import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import edu.uwm.cs351.Particle;
import edu.uwm.cs351.Point;
import edu.uwm.cs351.ParticleSimulation;
import edu.uwm.cs351.Vector;

public class Main {
	public static void main(String[] args) {
		  Particle p1 = new Particle(new Point(100.0,100.0), new Vector(0.0,0.1), 20.0, Color.YELLOW);
		  Particle p2 = new Particle(new Point(100.0,200.0),new Vector(0.5,0.0), 5.0, Color.BLACK);
		  Particle p3 = new Particle(new Point(150.0,100.0),new Vector(0.0,-0.5),1, Color.BLUE);
		  
		  final ParticleSimulation animation = new ParticleSimulation(p1,p2,p3);
		  
		  SwingUtilities.invokeLater(new Runnable() {
			  public void run() {
				  JFrame j = new JFrame();
				  j.setTitle("Particle Simulation");
				  j.setContentPane(animation);
				  j.setSize(300,300);
				  j.setVisible(true);
				  j.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			  }
		  });
		  
		  while (args.length == 0) {
			  try {
				  Thread.sleep(10);
				  animation.move();
			  } catch (InterruptedException e) {
				  return;
			  }
		  }
	  }
}
