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

/**
 * Artifact providing functionalities 
 * to manage/join workspaces and the node.
 *  
 * @author aricci
 *
 */
public class SystemArtifact extends Artifact {

	// private WorkspaceKernel thisWsp;
	
	void init(){
	}

	/**
	 * Shutdown gracefully the node.
	 * 
	 * The dispose routine of all artifacts is called.
	 * 
	 */
	@OPERATION void shutdown(){
		try {
			CartagoEnvironmentStandalone.getInstance().shutdown();
		} catch (Exception ex){
			ex.printStackTrace();
			failed("Shutdown failed.");
		}
	}

	/**
	 * Shutdown the node and after one second shutdown the VM.
	 */
	@OPERATION void crash(){
		try {
			CartagoEnvironmentStandalone.getInstance().shutdown();
			Thread.sleep(1000);
			System.exit(0);
		} catch (Exception ex){
			failed("Shutdown failed.");
		}
	}
	
}
