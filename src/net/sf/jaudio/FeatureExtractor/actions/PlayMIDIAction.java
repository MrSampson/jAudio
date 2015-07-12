package net.sf.jaudio.FeatureExtractor.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import net.sf.jaudio.FeatureExtractor.MIDIFrame;
import net.sf.jaudio.FeatureExtractor.RecordingSelectorPanel;

/**
 * Loads the MIDIFrame for playing midi files
 * 
 * @author Daniel McEnnis
 */
public class PlayMIDIAction extends AbstractAction {

	static final long serialVersionUID = 1;

	private MIDIFrame midi_frame = null;

	/**
	 * Constructor that sets the menu text.
	 */
	public PlayMIDIAction() {
		super("Play MIDI File...");
	}

	/**
	 * Create and show the MIDIFrame frame.
	 */
	public void actionPerformed(ActionEvent e) {
		if (midi_frame == null)
			midi_frame = new MIDIFrame();
		else
			midi_frame.setVisible(true);
	}

}
