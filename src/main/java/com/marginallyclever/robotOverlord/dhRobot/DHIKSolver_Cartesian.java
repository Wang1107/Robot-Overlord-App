package com.marginallyclever.robotOverlord.dhRobot;

import javax.vecmath.Matrix4d;

import com.marginallyclever.convenience.StringHelper;

/**
 * Solves Inverse Kinematics for cartesian robots like 3D printers and milling machines.
 * Effectively three prismatic (sliding) joints.
 * @author Dan Royer
 */
public class DHIKSolver_Cartesian extends DHIKSolver {
	//public double d0,d1,d2;

	/**
	 * @return the number of double values needed to store a valid solution from this DHIKSolver.
	 */
	public int getSolutionSize() {
		return 3;
	}

	/**
	 * Starting from a known local origin and a known local hand position, calculate the travel for the given pose.
	 * @param robot The DHRobot description. 
	 * @param targetPose the pose that robot is attempting to reach in this solution.
	 * @param keyframe store the computed solution in keyframe.
	 */
	@Override
	public void solve(DHRobot robot,Matrix4d targetPose,DHKeyframe keyframe) {
		Matrix4d m4 = new Matrix4d(targetPose);

		keyframe.fkValues[0] = m4.m23;
		keyframe.fkValues[1] = m4.m03;
		keyframe.fkValues[2] = m4.m13;
		
		this.solutionFlag = DHIKSolver.ONE_SOLUTION;
		
		if(true) {
			System.out.println("solution={"+StringHelper.formatDouble(keyframe.fkValues[0])+","+
					StringHelper.formatDouble(keyframe.fkValues[1])+","+
					StringHelper.formatDouble(keyframe.fkValues[2])+"}");
		}
	}
}
