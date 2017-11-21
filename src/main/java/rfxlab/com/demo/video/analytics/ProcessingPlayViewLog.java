package rfxlab.com.demo.video.analytics;


import rfx.core.stream.functor.StreamProcessor;
import rfx.core.stream.message.Tuple;
import rfx.core.stream.model.DataFlowInfo;
import rfx.core.stream.topology.BaseTopology;
import rfxlab.com.demo.utils.BatchLogWriter;

public class ProcessingPlayViewLog extends StreamProcessor {

	public static final String PLV = "plv";
	static final BatchLogWriter rawLogWriter = new BatchLogWriter("playview");
	

	protected ProcessingPlayViewLog(DataFlowInfo dataFlowInfo, BaseTopology topology) {
		super(dataFlowInfo, topology);
	}

	@Override
	public void onReceive(Tuple inTuple) throws Exception {
		LogData ld = (LogData) inTuple.getValueByField(ParsingPlayViewLog.LOG_DATA);

		rawLogWriter.writeString(ld.getLoggedTime(), ld.getPartitionId(), ld.toRawLogRecord());
		processPlayViewData(ld);		
	}

	public static void processPlayViewData(LogData ld) {
		String url = ld.getUrl();
		int placementId = ld.getPlacement();
		// TODO

	}

}
