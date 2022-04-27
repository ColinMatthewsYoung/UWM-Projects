
import edu.uwm.cs351.Particle;
import edu.uwm.cs351.ParticleCollection;
import edu.uwm.cs351.Point;
import edu.uwm.cs351.Vector;

import java.awt.Color;


public class TestParticleCollection extends TestCollection<Particle> {
	Particle e0 = new Particle(new Point(0,0),new Vector(),0.0,Color.BLACK);
	Particle e1 = new Particle(new Point(1,1),new Vector(),1.0,Color.WHITE); 
	Particle e2 = new Particle(new Point(2,2),new Vector(),2.0,Color.RED);
	Particle e3 = new Particle(new Point(3,3),new Vector(),3.0,Color.BLUE);
	Particle e4 = new Particle(new Point(4,4),new Vector(),4.0,Color.GREEN);
	Particle e5 = new Particle(new Point(5,5),new Vector(),5.0,Color.YELLOW);
	Particle e6 = new Particle(new Point(6,6),new Vector(),6.0,Color.GRAY);
	Particle e7 = new Particle(new Point(7,7),new Vector(),7.0,Color.MAGENTA);
	Particle e8 = new Particle(new Point(8,8),new Vector(),8.0,Color.ORANGE);
	Particle e9 = new Particle(new Point(9,9),new Vector(),9.0,Color.PINK);


	@Override
	protected void initCollections() {
		e = new Particle[] { e0, e1, e2, e3, e4, e5, e6, e7, e8, e9 };
		c = new ParticleCollection();
	}

}
