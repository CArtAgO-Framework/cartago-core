package cartago;

import cartago.events.*;

/**
 * 
 * Interface for components implementing a mapping 
 * from a workspace to a Conceptual Space
 *
 * A mapping component defines how a specific conceptual space
 * should be updated/aligned, given a relevant observable event/change
 * occurred inside a workspace
 * 
 */
public interface ConceptualSpaceMapping {

	void alignCS(Workspace wsp, ArtifactObsEvent ev);

	void alignCS(Workspace wsp, ArtifactCreatedEvent ev);

	void alignCS(Workspace wsp, ArtifactRemovedEvent ev);

	void alignCS(Workspace wsp, WorkspaceCreatedEvent ev);

	void alignCS(Workspace wsp, WorkspaceRemovedEvent ev);
	
	String getId();

}
