package com.marginallyclever.robotOverlord.modelInWorld;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Iterator;
import java.util.ServiceLoader;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.marginallyclever.robotOverlord.CollapsiblePanel;
import com.marginallyclever.robotOverlord.RobotOverlord;
import com.marginallyclever.robotOverlord.commands.UserCommandSelectFile;
import com.marginallyclever.robotOverlord.commands.UserCommandSelectNumber;
import com.marginallyclever.robotOverlord.commands.UserCommandSelectVector3d;
import com.marginallyclever.robotOverlord.entity.Entity;
import com.marginallyclever.robotOverlord.model.ModelLoadAndSave;

public class ModelInWorldPanel extends JPanel implements ChangeListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ModelInWorld model;
	private UserCommandSelectFile userCommandSelectFile;
	private UserCommandSelectNumber setScale;
	private UserCommandSelectVector3d setOrigin;
	private UserCommandSelectVector3d setRotation;
	
	public ModelInWorldPanel(RobotOverlord gui,ModelInWorld model) {
		super();
		
		this.model = model;

		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx=0;
		c.gridy=0;
		c.weightx=1;
		c.weighty=1;
		c.anchor=GridBagConstraints.NORTHWEST;
		c.fill=GridBagConstraints.HORIZONTAL;

		CollapsiblePanel oiwPanel = new CollapsiblePanel("Model");
		this.add(oiwPanel,c);
		JPanel contents = oiwPanel.getContentPane();
		
		GridBagConstraints con1 = new GridBagConstraints();
		con1.gridx=0;
		con1.gridy=0;
		con1.weighty=1;
		con1.fill=GridBagConstraints.HORIZONTAL;
		con1.anchor=GridBagConstraints.CENTER;

		userCommandSelectFile = new UserCommandSelectFile(gui,"Filename",model.getFilename());
		// Find all the serviceLoaders for loading files.
		ServiceLoader<ModelLoadAndSave> loaders = ServiceLoader.load(ModelLoadAndSave.class);
		Iterator<ModelLoadAndSave> i = loaders.iterator();
		while(i.hasNext()) {
			ModelLoadAndSave loader = i.next();
			FileNameExtensionFilter filter = new FileNameExtensionFilter(loader.getEnglishName(), loader.getValidExtensions());
			userCommandSelectFile.addChoosableFileFilter(filter);
		}
		userCommandSelectFile.addChangeListener(this);
		contents.add(userCommandSelectFile,con1);
		con1.gridy++;
		
		
		setScale = new UserCommandSelectNumber(gui,"Scale",model.getScale());
		setScale.addChangeListener(this);
		contents.add(setScale,con1);
		con1.gridy++;

		setOrigin = new UserCommandSelectVector3d(gui,"Adjust origin",model.getAdjustOrigin());
		setOrigin.addChangeListener(this);
		contents.add(setOrigin,con1);
		con1.gridy++;

		setRotation = new UserCommandSelectVector3d(gui,"Adjust rotation",model.getAdjustOrigin());
		setRotation.addChangeListener(this);
		contents.add(setRotation,con1);
		con1.gridy++;
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if(e.getSource()==userCommandSelectFile) {
			model.setFilename(userCommandSelectFile.getFilename());
		}
		if(e.getSource()==setScale) {
			model.setScale(setScale.getValue());
		}
		if(e.getSource()==setOrigin) {
			model.adjustOrigin(setOrigin.getValue());
		}
		if(e.getSource()==setRotation) {
			model.adjustRotation(setRotation.getValue());
		}
	}
	
	/**
	 * Call by an {@link Entity} when it's details change so that they are reflected on the panel.
	 * This might be better as a listener pattern.
	 */
	public void updateFields() {
		setScale.setValue(model.scale);
		setOrigin.setValue(model.getAdjustOrigin());
		setRotation.setValue(model.getAdjustRotation());
	}
}
