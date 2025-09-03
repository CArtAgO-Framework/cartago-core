package cartago.cs;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import cartago.ConceptualSpaceMapping;
import cartago.Workspace;
import cartago.events.ArtifactCreatedEvent;
import cartago.events.ArtifactObsEvent;
import cartago.events.ArtifactRemovedEvent;
import cartago.events.WorkspaceCreatedEvent;
import cartago.events.WorkspaceRemovedEvent;

/**
 * 
 * A simple general implementation of mapping engine component,
 * using a producer-consumer architecture to apply the mappings.
 * 
 * In particular aligning requests are buffered and processed 
 * sequentially by a separate align request processor thread, 
 * applying some specific alignment strategy that depends
 * on the specific concrete mapping.
 * 
 */
public class CSMappingEngine implements ConceptualSpaceMapping, CSAlignRequestSource {

	private String id;
	private BlockingQueue<CSAlignRequest> requestsQueue; 
	private static final int REQ_QUEUE_SIZE = 100;
	private CSAlignTaskStrategy alignStrategy;
	
	public CSMappingEngine(String id, CSAlignTaskStrategy alignStrategy) {
		this.id = id;
		this.alignStrategy = alignStrategy;
		requestsQueue = new LinkedBlockingQueue<CSAlignRequest>(REQ_QUEUE_SIZE);
		new CSAlignRequestProcessor(this).start();
	}
	
	@Override
	public void alignCS(Workspace wsp, ArtifactObsEvent ev) {
		requestsQueue.add(new CSAlignRequest(alignStrategy::alignCSObsEvent, wsp, ev));
	}

	@Override
	public void alignCS(Workspace wsp, ArtifactCreatedEvent ev) {
		requestsQueue.add(new CSAlignRequest(alignStrategy::alignCSArtCreatedEvent, wsp, ev));
	}

	@Override
	public void alignCS(Workspace wsp, ArtifactRemovedEvent ev) {
		requestsQueue.add(new CSAlignRequest(alignStrategy::alignCSArtRemovedEvent, wsp, ev));
	}
	
	@Override
	public void alignCS(Workspace wsp, WorkspaceCreatedEvent ev) {
		requestsQueue.add(new CSAlignRequest(alignStrategy::alignCSWspCreatedEvent, wsp, ev));
	}

	@Override
	public void alignCS(Workspace wsp, WorkspaceRemovedEvent ev) {
		requestsQueue.add(new CSAlignRequest(alignStrategy::alignCSWspRemovedEvent, wsp, ev));
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public CSAlignRequest fetchNewRequest() throws InterruptedException  {
		return requestsQueue.take();
	}


}
