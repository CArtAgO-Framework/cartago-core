package cartago.utils.agent;

public class ObsProperties {

	private cartago.utils.agent.ArtifactObsProperty[] props;
	
	ObsProperties(){
	}
	
	void setValue(cartago.utils.agent.ArtifactObsProperty[] props){
		this.props = props;
	}
	
	public cartago.utils.agent.ArtifactObsProperty getProperty(String name){
		for (ArtifactObsProperty obs: props){
			if (obs.getName().equals(name)){
				return obs;
			}
		}
		return null;
	}
	
}
