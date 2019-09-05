package rfxlab.com.demo.analytics;


import rfx.core.stream.topology.BaseTopology;
import rfx.core.stream.topology.Pipeline;
import rfx.core.stream.topology.PipelineTopology;
import rfx.core.util.LogUtil;
import rfx.core.util.Utils;

public class VideoPlayViewTopology extends PipelineTopology {        
    @Override
    public BaseTopology build() {   
    	System.out.println("... buildTopology " + this.topologyName);    	
        return Pipeline.create(this)
        		.apply(LogTokenizing.class)				
                .apply(ParsingPlayViewLog.class) 
                .apply(ProcessingPlayViewLog.class)
                .done();
    }
    
    private static final String TOPIC = "playview";		
    public static void main(String[] args) {
    	LogUtil.setPrefixFileName(TOPIC);
		int begin  = 0;
		int end  = 0;		
		PipelineTopology topo = new VideoPlayViewTopology();
		topo.initKafkaDataSeeders(TOPIC, begin, end).buildTopology().start();
		Utils.sleep(2000);
	}
}