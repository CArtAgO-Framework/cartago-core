package cartago.cs;

public class CSAlignRequestProcessor extends Thread {

	private CSAlignRequestSource alignRequests;
	
	public CSAlignRequestProcessor(CSAlignRequestSource src) {
		this.alignRequests = src;
	}
	
	public void run() {
		while (true) {
			try {
				var req = alignRequests.fetchNewRequest();
				req.task().process(req.wsp(), req.ev());				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	
}
