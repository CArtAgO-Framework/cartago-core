package cartago.cs;

import cartago.CartagoEvent;
import cartago.Workspace;

/**
 * 
 * This interface represent the strategy that the 
 * processor must use to align a conceptual space
 * given an event occurred in the workspace.
 * 
 * Implementations of this interface represent
 * a concrete mapping to CS.
 * 
 */
public interface CSAlignTaskStrategy {

	void alignCSObsEvent(Workspace wsp, CartagoEvent ev);
	void alignCSArtCreatedEvent(Workspace wsp, CartagoEvent ev);
	void alignCSArtRemovedEvent(Workspace wsp, CartagoEvent ev);
	void alignCSWspCreatedEvent(Workspace wsp, CartagoEvent ev);
	void alignCSWspRemovedEvent(Workspace wsp, CartagoEvent ev);
}
