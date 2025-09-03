package cartago;

import cartago.events.*;

public interface ConceptualSpaceMapping {
	
	void alignCS(Workspace wsp, ArtifactObsEvent ev);

	void alignCS(Workspace wsp, ArtifactCreatedEvent ev);

	void alignCS(Workspace wsp, ArtifactRemovedEvent ev);

	void alignCS(Workspace wsp, WorkspaceCreatedEvent ev);

	void alignCS(Workspace wsp, WorkspaceRemovedEvent ev);
	
	String getId();

}
