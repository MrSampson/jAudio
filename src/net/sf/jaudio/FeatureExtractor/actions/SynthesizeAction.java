package net.sf.jaudio.FeatureExtractor.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import net.sf.jaudio.FeatureExtractor.Controller;
import net.sf.jaudio.FeatureExtractor.RecordingSelectorPanel;
import net.sf.jaudio.FeatureExtractor.SynthesisFrame;

/**
 * Action that shows the SynthesizeFrame
 * 
 * @author Daniel McEnnis
 *
 */
public class SynthesizeAction extends AbstractAction {
	
	static final long serialVersionUID = 1;

	private SynthesisFrame synthesis_frame=null;
	
	private Controller controller;
	
	/**
	 * Constructor that sets the menu text and stores a reference to the controller.
	 * @param c near global controller.
	 */
	public SynthesizeAction(Controller c){
		super("Synthesize Audio...");
		controller = c;
	}
	
	/**
	 * Creates and shows the SynthesizeFrame.
	 */
	public void actionPerformed(ActionEvent e) {
		if (synthesis_frame == null)
			synthesis_frame = new SynthesisFrame(controller);
		else
			synthesis_frame.setVisible(true);
	}

}
