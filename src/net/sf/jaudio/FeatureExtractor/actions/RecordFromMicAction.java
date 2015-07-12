package net.sf.jaudio.FeatureExtractor.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import net.sf.jaudio.FeatureExtractor.Controller;
import net.sf.jaudio.FeatureExtractor.RecordingFrame;

/**
 * Action that creates and displays the RecordingFrame
 * 
 * @author Daniel McEnnis
 */
public class RecordFromMicAction extends AbstractAction {

	static final long serialVersionUID = 1;

	Controller parent;

	RecordingFrame rec_ = null;

	/**
	 * Constructor that sets th4e menu text and stores a reference to the
	 * controller.
	 * 
	 * @param c
	 *            near global controller.
	 */
	public RecordFromMicAction(Controller c) {
		super("Record From Mic...");
		parent = c;
	}

	/**
	 * Creates and displays the RecordingFrame frame.
	 */
	public void actionPerformed(ActionEvent e) {
		if (rec_ == null) {
			rec_ = new RecordingFrame(parent);
		}
		rec_.setVisible(true);
	}

}
