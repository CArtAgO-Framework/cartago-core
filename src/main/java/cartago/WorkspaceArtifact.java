/**
 * CArtAgO - DISI, University of Bologna
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package cartago;

import cartago.security.SecurityException;
import cartago.tools.inspector.Inspector;
import cartago.security.*;

import java.net.URI;
import java.util.*;

/**
 * Artifact providing basic functionalities to manage the workspace, including
 * creating new artifacts, lookup artifacts, setting RBAC security policies, and
 * so on.
 * 
 * @author aricci
 *
 */
public class WorkspaceArtifact extends Artifact {

	private Workspace wsp;
	private Inspector debug;

	@OPERATION
	void init(Workspace env) {
		this.wsp = env;
	}

	/* Artifacts management */

	/**
	 * Lookup an artifact, to get its unique identifier
	 * 
	 * @param artifactName artifact name
	 * @param aid artifact identifier
	 */
	@OPERATION @LINK void lookupArtifact(String artifactName, OpFeedbackParam<ArtifactId> aid) {
		try {
			ArtifactId id = wsp.lookupArtifact(this.getCurrentOpAgentId(), artifactName);
			aid.set(id);
		} catch (Exception ex) {
			failed(ex.toString());
		}
	}

	/**
	 * 
	 * Discover an artifact by type
 	 *
	 * @param artifactType
	 * @param aid
	 */
	@OPERATION
	@LINK
	void lookupArtifactByType(String artifactType, OpFeedbackParam<ArtifactId> aid) {
		try {
			ArtifactId id = wsp.lookupArtifactByType(this.getCurrentOpAgentId(), artifactType);
			aid.set(id);
		} catch (Exception ex) {
			failed(ex.toString());
		}
	}

	/**
	 * 
	 * Discover an artifact by type
	 *
	 * @param list
	 */
	@OPERATION
	@LINK
	void getCurrentArtifacts(OpFeedbackParam<String[]> list) {
		try {
			String[] names = wsp.getArtifactList();
			list.set(names);
		} catch (Exception ex) {
			failed(ex.toString());
		}
	}

	/**
	 * 
	 * Create a new artifact
	 * 
	 * 
	 * @param artifactName name of the artifact 
	 * @param templateName template (Java full class name)
	 */
	@OPERATION
	@LINK
	void makeArtifact(String artifactName, String templateName) {
		try {
			ArtifactId id = wsp.makeArtifact(this.getCurrentOpAgentId(), artifactName, templateName,
					ArtifactConfig.DEFAULT_CONFIG);
			// signal("new_artifact_created",artifactName,templateName,id);
			this.defineObsProperty("artifact", id, artifactName, templateName);
		} catch (UnknownArtifactTemplateException ex) {
			failed("artifact " + artifactName + " creation failed: unknown template " + templateName,
					"makeArtifactFailure", "unknown_artifact_template", templateName);
		} catch (ArtifactAlreadyPresentException ex) {
			failed("artifact " + artifactName + " creation failed: " + artifactName + "already present",
					"makeArtifactFailure", "artifact_already_present", artifactName);
		} catch (ArtifactConfigurationFailedException ex) {
			failed("artifact " + artifactName + " creation failed: an error occurred in artifact initialisation",
					"makeArtifactFailure", "init_failed", artifactName);
		}
	}

	/**
	 * 
	 * Create a new artifact
	 * 
	 * 
	 * @param artifactName name of the artifact 
	 * @param templateName template (Java full class name)
	 * @param param parameters
	 */
	@OPERATION
	@LINK
	void makeArtifact(String artifactName, String templateName, Object[] param) {
		try {
			ArtifactId id = wsp.makeArtifact(this.getCurrentOpAgentId(), artifactName, templateName,
					new ArtifactConfig(param));
			this.defineObsProperty("artifact", id, artifactName, templateName);
		} catch (UnknownArtifactTemplateException ex) {
			failed("artifact " + artifactName + " creation failed: unknown template " + templateName,
					"makeArtifactFailure", "unknown_artifact_template", templateName);
		} catch (ArtifactAlreadyPresentException ex) {
			failed("artifact " + artifactName + " creation failed: " + artifactName + "already present",
					"makeArtifactFailure", "artifact_already_present", artifactName);
		} catch (ArtifactConfigurationFailedException ex) {
			failed("artifact " + artifactName + " creation failed: an error occurred in artifact initialisation",
					"makeArtifactFailure", "init_failed", artifactName);
		}
	}
	
	/**
	 * 
	 * Create a new artifact
	 * 
	 * @param artifactName name of the artifact 
	 * @param templateName template (Java full class name)
	 * @param param parameters
	 * @param aid artifact identifier
	 */
	@OPERATION
	@LINK
	void makeArtifact(String artifactName, String templateName, Object[] params, OpFeedbackParam<ArtifactId> aid) {
		try {
			ArtifactId id = wsp.makeArtifact(this.getCurrentOpAgentId(), artifactName, templateName,
					new ArtifactConfig(params));
			aid.set(id);
			this.defineObsProperty("artifact", id, artifactName, templateName);
		} catch (UnknownArtifactTemplateException ex) {
			failed("artifact " + artifactName + " creation failed: unknown template " + templateName,
					"makeArtifactFailure", "unknown_artifact_template", templateName);
		} catch (ArtifactAlreadyPresentException ex) {
			failed("artifact " + artifactName + " creation failed: " + artifactName + "already present",
					"makeArtifactFailure", "artifact_already_present", artifactName);
		} catch (ArtifactConfigurationFailedException ex) {
			failed("artifact " + artifactName + " creation failed: an error occurred in artifact initialisation",
					"makeArtifactFailure", "init_failed", artifactName);
		}
	}

	/**
	 * 
	 * Dispose an existing artifact.
	 * 
	 * @param id
	 */
	@OPERATION
	@LINK
	void disposeArtifact(ArtifactId id) {
		try {
			wsp.disposeArtifact(this.getCurrentOpAgentId(), id);
		} catch (Exception ex) {
			failed(ex.toString());
			return;
		}
		try {
			// some artifact (session_XXX for instance) are not registered
			// here and the removeObsProo could fail
			this.removeObsPropertyByTemplate("artifact", id, null, null);
		} catch (Exception ex) {
		}
	}

	@GUARD
	boolean artifactAvailable(String artName) {
		return wsp.getArtifact(artName) != null;
	}

	/**
	 * Link two artifacts.
	 * 
	 * @param artifactOutId
	 * @param artifactOutPort
	 * @param artifactInId
	 */
	@OPERATION
	void linkArtifacts(ArtifactId artifactOutId, String artifactOutPort, ArtifactId artifactInId) {
		AgentId userId = this.getCurrentOpAgentId();
		try {
			wsp.linkArtifacts(userId, artifactOutId, artifactOutPort, artifactInId);
		} catch (Exception ex) {
			failed("Artifact Not Available.");
		}
	}

	/* Workspaces */

	/* Artifact factories management */

	/**
	 * Add an artifact factory
	 * 
	 * @param factory artifact factory
	 */
	@OPERATION
	@LINK
	void addArtifactFactory(ArtifactFactory factory) {
		wsp.addArtifactFactory(factory);
	}

	/**
	 * Remove an existing artifact factory
	 * 
	 * @param name
	 */
	@OPERATION
	@LINK
	void removeArtifactFactory(String name) {
		wsp.removeArtifactFactory(name);
	}

	/* WSP Rule management */

	@OPERATION
	void setWSPRuleEngine(AbstractWSPRuleEngine man) {
		try {
			man.setKernel(wsp);
			wsp.setWSPRuleEngine(man);
		} catch (Exception ex) {
			failed(ex.getMessage());
		}
	}

	/* Topology management */

	/*
	 * @OPERATION void setWorkspaceTopology(AbstractWorkspaceTopology topology){ try
	 * { wspKernel.setWSPTopology(topology); } catch(Exception ex){
	 * failed(ex.getMessage()); } }
	 */

	// manuals management

	/*
	 * 
	 * @LINK @OPERATION void createManual(String src, OpFeedbackParam<ArtifactId>
	 * aid) throws CartagoException { try { Manual man = env.execCreateManual(src);
	 * ArtifactId id =
	 * env.(getOpUserId(),man.getName(),"alice.cartago.ManualArtifact",new
	 * ArtifactConfig(man),null); aid.set(id); } catch (Exception ex){
	 * failed(ex.toString()); } }
	 * 
	 * @LINK @OPERATION void createManualFromFile(String fname,
	 * OpFeedbackParam<ArtifactId> aid) throws CartagoException { try { String src =
	 * env.loadManualSrc(fname); Manual man = env.createManual(src, getOpUserId());
	 * ArtifactId id = env.execMakeArtifact(getOpUserId(),man.getName(),
	 * "alice.cartago.ManualArtifact",new ArtifactConfig(man),null); aid.set(id); }
	 * catch (Exception ex){ failed(ex.toString()); } }
	 */

	/* RBAC */

	@OPERATION
	void setSecurityManager(IWorkspaceSecurityManager man) {
		wsp.setSecurityManager(man);
	}

	/**
	 * Add a role.
	 */
	@OPERATION
	void addRole(String roleName) {
		try {
			wsp.getSecurityManager().addRole(roleName);
		} catch (SecurityException ex) {
			failed("security_exception");
		}
	}

	/**
	 * Remove a role, if it exists
	 * 
	 * @param roleName
	 */
	@OPERATION
	void removeRole(String roleName) {
		try {
			wsp.getSecurityManager().removeRole(roleName);
		} catch (SecurityException ex) {
			failed("security_exception");
		}
	}

	/**
	 * Get current roles list.
	 * 
	 * @return
	 */
	@OPERATION
	void getRoleList(OpFeedbackParam<String[]> list) {
		try {
			list.set(wsp.getSecurityManager().getRoleList());
		} catch (SecurityException ex) {
			failed("security_exception");
		}
	}

	/**
	 * Add a policy to a role
	 * 
	 */
	@OPERATION
	void addRolePolicy(String roleName, String artifactName, IArtifactUsePolicy policy) {
		try {
			wsp.getSecurityManager().addRolePolicy(roleName, artifactName, policy);
		} catch (SecurityException ex) {
			failed("security_exception");
		}
	}

	/**
	 * Remove a policy
	 * 
	 */
	@OPERATION
	void removeRolePolicy(String roleName, String artifactName) {
		try {
			wsp.getSecurityManager().removeRolePolicy(roleName, artifactName);
		} catch (SecurityException ex) {
			failed("security_exception");
		}
	}

	/**
	 * Set the default use policy
	 * 
	 */
	@OPERATION
	void setDefaultRolePolicy(String roleName, String artName, IArtifactUsePolicy policy) {
		try {
			wsp.getSecurityManager().setDefaultRolePolicy(roleName, policy);
		} catch (SecurityException ex) {
			failed("security_exception");
		}
	}

	/**
	 * Get current artifact list.
	 * 
	 * 
	 */
	@LINK
	void getArtifactList(OpFeedbackParam<ArtifactId[]> artifacts) {
		artifacts.set(wsp.getArtifactIdList());
	}

	/**
	 * Enable debugging
	 * 
	 */
	@OPERATION
	void enableDebug() {
		if (debug == null) {
			Inspector insp = new Inspector();
			insp.start();
			wsp.getLoggerManager().registerLogger(insp.getLogger());
		}
	}

	/**
	 * Disable debugging
	 * 
	 */
	@OPERATION
	void disableDebug() {
		if (debug != null) {
			wsp.getLoggerManager().unregisterLogger(debug.getLogger());
		}
	}	
	
}
