package cartago.cs;

/**
 * 
 * Interface representing the source where to fetch
 * the requests about aligning a Conceptual Space,
 * given an event occurred inside a Workspace.
 * 
 */
public interface CSAlignRequestSource {

	CSAlignRequest fetchNewRequest() throws InterruptedException;
}
