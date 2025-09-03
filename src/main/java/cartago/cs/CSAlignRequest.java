package cartago.cs;

import cartago.CartagoEvent;
import cartago.Workspace;

public record CSAlignRequest(AlignCSTask task, Workspace wsp, CartagoEvent ev) {}
