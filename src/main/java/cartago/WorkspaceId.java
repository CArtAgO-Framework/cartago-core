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

import java.io.Serializable;
import java.net.URI;

/**
 * Identifier of a workspace
 *  
 * @author aricci
 */
public class WorkspaceId implements Serializable {
	
	private String name;

	/**
	 * 
	 * Workspace identifier contructor
	 * 
	 * @param name Unique name of thw workspace
	 */
	public WorkspaceId(String name) {
		this.name = name;
	}
	
	/**
	 * Get the local name of the workspace
	 * 
	 * @return
	 */
	public String getName(){
		return name;
	}
			
	public boolean equals(Object wspId) {
		return this.getName().equals(((WorkspaceId)wspId).getName());
	}
	
	@Override
	public int hashCode() {
		return getName().hashCode();
	}
}
