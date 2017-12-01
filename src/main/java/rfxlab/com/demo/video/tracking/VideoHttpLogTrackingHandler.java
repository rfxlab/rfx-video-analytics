package rfxlab.com.demo.video.tracking;

import io.vertx.core.http.HttpServerRequest;
import rfx.core.util.StringPool;
import server.http.handler.BaseHttpHandler;
import server.http.util.HttpTrackingUtil;
import server.http.util.KafkaLogHandlerUtil;

public class VideoHttpLogTrackingHandler implements BaseHttpHandler {
	
	private static final String PONG = "PONG";
	private static final String FAVICON_ICO = "favicon.ico";
	private static final String PING = "ping";
	private static final String VIDEO_TRACKING = "playview";


	@Override
	public void handle(HttpServerRequest req) {
		String uri;
		if(req.uri().startsWith("/")){
			uri = req.uri().substring(1);	
		} else {
			uri = req.uri();
		}
		
		System.out.println("URI: " + uri);
		
		//common
		if (uri.equalsIgnoreCase(FAVICON_ICO)) {
			HttpTrackingUtil.trackingResponse(req);
		}
		else if (uri.equalsIgnoreCase(PING)) {
			req.response().end(PONG);
		}	
		
		else if(uri.startsWith(VIDEO_TRACKING)){
			//handle log request				
			KafkaLogHandlerUtil.logAndResponseImage1px(req, VIDEO_TRACKING);
		}
		
		else {
			req.response().end("Not handler found for uri:"+uri);
		}
	}

	@Override
	public String getPathKey() {
		return StringPool.STAR;
	}

}
