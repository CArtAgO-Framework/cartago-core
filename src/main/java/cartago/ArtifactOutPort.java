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

import java.util.*;

/**
 * Class representing artifact out port.
 *  
 * @author aricci
 *
 */
public class ArtifactOutPort {

	private String portName;
	private ArrayList<ArtifactId> artifactList;

	ArtifactOutPort(String name){
		this.portName = name;
		artifactList = new ArrayList<ArtifactId>();
	}
	
	public ArrayList<ArtifactId> getArtifactList(){
		return artifactList;
	}
	
	public void addArtifact(ArtifactId id){
		artifactList.add(id);
	}
	
	public String getPortName(){
		return portName;
	}
	
	public void removeArtifact(ArtifactId id){
		artifactList.remove(id);
	}
}
