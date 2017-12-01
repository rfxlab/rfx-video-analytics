package rfxlab.com.demo.starter;

import rfx.core.util.StringUtil;
import rfxlab.com.demo.video.tracking.VideoHttpLogTrackingHandler;
import server.http.RfxEventTrackingWorker;

public class TrackingHttpServer {
	public static void main(String[] args) throws Exception {
		if(args.length == 0 ){
			args = new String[]{"127.0.0.1","8080"};
		}		
		String host = args[0];
		int port = StringUtil.safeParseInt(args[1]);			
		RfxEventTrackingWorker.createHttpLogCollector(host, port, new VideoHttpLogTrackingHandler());		 
	}
}
