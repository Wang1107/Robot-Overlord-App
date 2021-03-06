package com.marginallyclever.robotOverlord.makelangeloRobot.generators;

import java.io.IOException;
import java.io.Writer;

import com.marginallyclever.robotOverlord.RobotOverlord;
import com.marginallyclever.robotOverlord.Translator;

/**
 * Generates a Sierpinsky Triangle fractal, also known as an arrowhead curve.
 * @author Dan Royer 2016-12-12
 * @see <a href='https://en.wikipedia.org/wiki/Sierpi%C5%84ski_arrowhead_curve'>https://en.wikipedia.org/wiki/Sierpi%C5%84ski_arrowhead_curve</a>
 */
public class Generator_SierpinskiTriangle extends ImageGenerator {
	private Turtle turtle;
	private float xMax, xMin, yMax, yMin;
	private float maxSize;
	private static int order = 4; // controls complexity of curve
	
	@Override
	public String getName() {
		return Translator.get("SierpinskiTriangleName");
	}


	static public int getOrder() {
		return order;
	}
	static public void setOrder(int order) {
		if(order<1) order=1;
		Generator_SierpinskiTriangle.order = order;
	}
	
	
	@Override
	public boolean generate(RobotOverlord gui,Writer out) throws IOException {
		imageStart(out);
		tool = machine.getCurrentTool();
		liftPen(out);
		tool.writeChangeTo(out);

		float v = Math.min((float)(machine.getPaperWidth() * machine.getPaperMargin()),
				(float)(machine.getPaperHeight() * machine.getPaperMargin())) * 10.0f/2.0f;
		xMax = v;
		yMax = v;
		xMin = -v;
		yMin = -v;

		turtle = new Turtle();
		
		float xx = xMax - xMin;
		float yy = yMax - yMin;
		maxSize = xx > yy ? xx : yy;

		boolean drawBoundingBox=false;
		if(drawBoundingBox) {
			liftPen(out);
			moveTo(out, xMax, yMax, false);
			moveTo(out, xMax, yMin, false);
			moveTo(out, xMin, yMin, false);
			moveTo(out, xMin, yMax, false);
			moveTo(out, xMax, yMax, false);
			liftPen(out);
		}
		
		liftPen(out);
		// move to starting position
		turtle.setX(xMax);
		turtle.setY(-xMax/2);
		moveTo(out, turtle.getX(), turtle.getY(), true);
		lowerPen(out);
		// do the curve
		turtle.turn(90);
		if( (order&1) == 0 ) {
			drawCurve(out, order, maxSize,-60);
		} else {
			turtle.turn(60);
			drawCurve(out, order, maxSize,-60);
		}
		liftPen(out);
	    moveTo(out, (float)machine.getHomeX(), (float)machine.getHomeY(),true);
		return true;
	}


	private void drawCurve(Writer output, int n, float distance,float angle) throws IOException {
		if (n == 0) {
			turtleMove(output,distance);
			return;
		}
		
		drawCurve(output,n-1,distance/2.0f,-angle);
		turtle.turn(angle);
		drawCurve(output,n-1,distance/2.0f,angle);
		turtle.turn(angle);
		drawCurve(output,n-1,distance/2.0f,-angle);
	}


	public void turtleMove(Writer output,float distance) throws IOException {
		//turtle_x += turtle_dx * distance;
		//turtle_y += turtle_dy * distance;
		//output.write(new String("G0 X"+(turtle_x)+" Y"+(turtle_y)+"\n").getBytes());
		turtle.move(distance);
		moveTo(output, turtle.getX(), turtle.getY(), false);
	}
}
