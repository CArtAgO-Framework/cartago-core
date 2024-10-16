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

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

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
			
	private HashMap<String,Inspector> debuggers;
	
	private Workspace wsp;
	
	public static String WSP_DEFAULT_NAME = "main";
	
	public static CartagoEnvironmentStandalone getInstance() {
		synchronized (CartagoEnvironmentStandalone.class) {
			if (instance == null) {
				instance = new CartagoEnvironmentStandalone();
			}
			return instance;
		}
	}
	
	private CartagoEnvironmentStandalone() {
		debuggers = new HashMap<String,Inspector>();
	}
	
	/* Init methods -- Standalone or Infrastructure workspace */
	
	/**
	 * 
	 * Initialise the workspace, as standalone
	 * 
	 * @param wspName workspace name
	 * 
	 * @param logger logger to be used
	 */
	public void initWsp(String wspName, Optional<ICartagoLogger> logger) {
		WorkspaceId id = new WorkspaceId(wspName);
		wsp = new Workspace(id, logger);
	}

	/**
	 * 
	 * Initialise the workspace as standalone, using the default name
	 * 
	 * @param logger logger to be used
	 */
	public void initWsp(Optional<ICartagoLogger> logger) {
		this.initWsp(WSP_DEFAULT_NAME, logger);
	}

	/**
	 * 
	 * Initialise the workspace as standalone, using the default name
	 * 
	 */
	public void initWsp() {
		this.initWsp(WSP_DEFAULT_NAME, Optional.empty());
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
	public synchronized IAgentSession startSession(AgentCredential cred, ICartagoListener eventListener) throws CartagoException {
			AgentSession session = new AgentSession(cred,null,eventListener);
			ICartagoContext ctx = wsp.joinWorkspace(cred, session);
			session.init(CartagoEnvironmentStandalone.getInstance().getWorkspace().getId(), ctx);
			return session;
	}
	
		
	/* factory management */
	
	/**
	 * Add an artifact factory for artifact templates
	 * 
	 * @param wspName workspace name
	 * @param factory artifact factory
	 * @throws CartagoException
	 */
	public synchronized void addArtifactFactory(String wspName, ArtifactFactory factory) throws CartagoException {
		wsp.addArtifactFactory(factory);
	}
	
	/**
	 * Remove an existing class loader for artifacts
	 * @param wspName workspace name
	 * @param name id of the artifact factory
	 * @throws CartagoException
	 */
	public synchronized void removeArtifactFactory(String wspName, String name) throws CartagoException {
		wsp.removeArtifactFactory(name);
	}

	/**
	 * Register a new logger for CArtAgO Workspace Kernel events
	 * 
	 * @param wspName
	 * @param logger
	 * @throws CartagoException
	 */
	public synchronized void registerLogger(String wspName, ICartagoLogger logger) throws CartagoException {
		wsp.registerLogger(logger);
	}

	/* debugging */
	
	/**
	 * Enable debugging of a CArtAgO Workspace 
	 * 
	 * @param wspName
	 * @throws CartagoException
	 */
	public synchronized void enableDebug(String wspName) throws CartagoException {
		Inspector insp = debuggers.get(wspName);
		 if (insp == null){
			insp = new Inspector();
			insp.start();
			registerLogger(wspName, insp.getLogger());
			debuggers.put(wspName, insp);
		}
	}
	
	/**
	 * Disable debugging of a CArtAgO Workspace 
	 * 
	 * @param wspName
	 * @throws CartagoException
	 */
	public synchronized void disableDebug(String wspName) throws CartagoException {
			 Inspector insp = debuggers.remove(wspName);
			 if (insp != null){
				 wsp.unregisterLogger(insp.getLogger());
			 }
	}
	
	
	/* loggers */
	
	/**
	 * 
	 * Unregister a logger 
	 * 
	 * @param wspName
	 * @param logger
	 * @throws CartagoException
	 */
	public synchronized void unregisterLogger(String wspName, ICartagoLogger logger) throws CartagoException {
		wsp.unregisterLogger(logger);
	}	
	
	/**
	 * Getting a controller.
	 * 
	 * @param wspName
	 * @return
	 * @throws CartagoException
	 */
	public synchronized ICartagoController  getController(String wspName) throws CartagoException {
		return wsp.getController();
	}
	

}
