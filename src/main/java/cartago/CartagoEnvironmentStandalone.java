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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import cartago.standalone.AgentSession;
import cartago.tools.inspector.Inspector;


/**
 * 
 * Entry point for working with CArtAgO Environment
 *  
 * @author aricci
 *
 */
public class CartagoEnvironmentStandalone {

	/* singleton design */
	private static CartagoEnvironmentStandalone instance;
			
	private Inspector inspector;
	
	private Workspace wsp;
	
	public static String WSP_DEFAULT_NAME = "main";
	
	private List<ConceptualSpaceMapping> csMappings ;
	
	public static CartagoEnvironmentStandalone getInstance() {
		synchronized (CartagoEnvironmentStandalone.class) {
			if (instance == null) {
				instance = new CartagoEnvironmentStandalone();
			}
			return instance;
		}
	}
	
	private CartagoEnvironmentStandalone() {
		csMappings = new ArrayList<ConceptualSpaceMapping>();
	}
	
	/*
	 * 
	 *  Init methods -- Standalone or Infrastructure workspace 
	 *  
	 */
	
	/**
	 * 
	 * Initialise the workspace, as standalone
	 * 
	 * @param wspName workspace name
	 * 
	 * @param logger logger to be used
	 */
	public void initWsp(String wspName) {
		WorkspaceId id = new WorkspaceId(wspName);
		wsp = new Workspace(id);
		for (var m: csMappings) {
			try {
				wsp.registerMapping(m);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		wsp.init();
	}

	/**
	 * 
	 * Initialise the workspace as standalone, using the default name
	 * 
	 */
	public void initWsp() {
		this.initWsp(WSP_DEFAULT_NAME);
	}
		
	/**
	 * 
	 * Get the workspace.
	 * 
	 * @return
	 */
	public Workspace getWorkspace() {
		return wsp;
	}
	
	/**
	 * 
	 * Get version.
	 * 
	 * @return
	 */
	public String getVersion() {
		return CARTAGO_VERSION.getID();
	}
	
	/**
	 * 
	 * Shutdown the CArtAgO environmennt.
	 * 
	 * @throws CartagoException
	 */
	public synchronized void shutdown() throws CartagoException {
		wsp.shutdown();
	}
	
	// Agent sessions

	/**
	 * 
	 * Start a CArtAgO session in the local workspace.
	 * 
	 * @param cred Agent credentials
	 * @param eventListener listener to receive CArtAgO events
	 * @return Session object
	 * @throws CartagoException
	 */
	public IAgentSession startSession(AgentCredential cred, ICartagoListener eventListener) throws CartagoException {
		AgentSession session = new AgentSession(cred,null,eventListener);
		ICartagoContext ctx = wsp.joinWorkspace(cred, session);
		session.init(CartagoEnvironmentStandalone.getInstance().getWorkspace().getId(), ctx);
		return session;
	}
	
	/* CS mapping */
	
	public void registerCSMapping(ConceptualSpaceMapping cs) {
		this.csMappings.add(cs);
	}
	
	/* factory management */
	
	/**
	 * Add an artifact factory for artifact templates
	 * 
	 * @param factory artifact factory
	 * @throws CartagoException
	 */
	public void addArtifactFactory(ArtifactFactory factory) throws CartagoException {
		wsp.addArtifactFactory(factory);
	}
	
	/**
	 * Remove an existing class loader for artifacts
	 * @param name id of the artifact factory
	 * @throws CartagoException
	 */
	public void removeArtifactFactory(String name) throws CartagoException {
		wsp.removeArtifactFactory(name);
	}

	/**
	 * Register a new logger for CArtAgO Workspace Kernel events
	 * 
	 * @param wspName
	 * @param logger
	 * @throws CartagoException
	 */
	public void registerLogger(ICartagoLogger logger) throws CartagoException {
		wsp.registerLogger(logger);
	}

	/* inspector */
	
	/**
	 * Enable inpector 
	 * 
	 * @param wspName
	 * @throws CartagoException
	 */
	public void enableInspector() throws CartagoException {
		 if (inspector == null){
			 inspector = new Inspector();
			 inspector.start();
			registerLogger(inspector.getLogger());
		}
	}
	
	/**
	 * Disable inspector 
	 * 
	 * @throws CartagoException
	 */
	public void disableDebug() throws CartagoException {
		inspector = null;
	}
	
	
	/* loggers */
	
	/**
	 * 
	 * Unregister a logger 
	 * 
	 * @param logger
	 * @throws CartagoException
	 */
	public void unregisterLogger(ICartagoLogger logger) throws CartagoException {
		wsp.unregisterLogger(logger);
	}	
	
	/**
	 * Getting a controller.
	 * 
	 * @return
	 * @throws CartagoException
	 */
	public ICartagoController  getController() throws CartagoException {
		return wsp.getController();
	}

}
