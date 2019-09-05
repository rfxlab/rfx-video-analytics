package rfxlab.com.demo.analytics;

import java.util.Date;
import java.util.List;
import java.util.Map;

import rfx.core.stream.functor.StreamProcessor;
import rfx.core.stream.message.Fields;
import rfx.core.stream.message.Tuple;
import rfx.core.stream.message.Values;
import rfx.core.stream.model.DataFlowInfo;
import rfx.core.stream.processor.HttpEventProcessor;
import rfx.core.stream.topology.BaseTopology;
import rfx.core.util.useragent.Client;
import rfx.core.util.useragent.Parser;
import rfx.core.util.DateTimeUtil;
import rfx.core.util.LogUtil;
import rfx.core.util.StringUtil;
import rfxlab.com.demo.utils.BeaconUtil;
import rfxlab.com.demo.utils.DeviceParserUtil;

public class ParsingPlayViewLog extends StreamProcessor {

	public static final String LOG_DATA = "LogData";
	public static final String CONTEXT_KEYWORD = "cxkw";
	static Fields outFields = new Fields(LOG_DATA);

	protected ParsingPlayViewLog(DataFlowInfo dataFlowInfo, BaseTopology topology) {
		super(dataFlowInfo, topology);
	}

	@Override
	public void onReceive(Tuple inputTuple) throws Exception {
		try {
			String query = inputTuple.getStringByField(HttpEventProcessor.QUERY);
			int loggedTime = inputTuple.getIntegerByField(HttpEventProcessor.LOGGEDTIME);
			String userAgent = inputTuple.getStringByField(HttpEventProcessor.USERAGENT);
			String ip = inputTuple.getStringByField(HttpEventProcessor.IP);
			String cookie = inputTuple.getStringByField(HttpEventProcessor.COOKIE);
			int partitionId = StringUtil.safeParseInt(inputTuple.getStringByField(HttpEventProcessor.PARTITION_ID));
			if (StringUtil.isEmpty(query)) {
				return;
			}
			System.out.println("loggedTime " + DateTimeUtil.formatDateHourMinute(new Date(loggedTime * 1000L)));

			Map<String, List<String>> params = BeaconUtil.getQueryMap(query);
			String uuid = BeaconUtil.getParam(params, "uuid", "");
			String referrer = BeaconUtil.getParam(params, "referrer");
			String url = BeaconUtil.getParam(params, "url", BeaconUtil.extractRefererURL(cookie));
			int placementId = StringUtil.safeParseInt(BeaconUtil.getParam(params, "placement"));

			// here to get contentID from url
			String contentID = BeaconUtil.getParam(params, "ctid", "-");

			String contextKeyword = BeaconUtil.getParam(params, "cxkw");
			LogData o = new LogData(loggedTime, placementId, uuid, ip, partitionId, contentID);

			o.setUrl(url);
			o.setRefererUrl(referrer);

			if (userAgent.contains("okhttp")) {
				o.setDeviceType(DeviceParserUtil.NATIVE_APP);
				o.setDeviceName(DeviceParserUtil.DEVICE_ANDROID);
				o.setDeviceOs(DeviceParserUtil.DEVICE_ANDROID);
			} else {
				Client uaClient = Parser.load().parse(userAgent);
				int deviceType = DeviceParserUtil.getDeviceType(uaClient);
				o.setDeviceType(deviceType);
				o.setDeviceName(uaClient.device.family);
				o.setDeviceOs(uaClient.os.family);
			}

			o.setUserAgent(userAgent);
			o.setContextKeyword(contextKeyword);
			this.emit(new Tuple(outFields, new Values(o)));
		} catch (IllegalArgumentException e) {
			LogUtil.e("ParsingPlayViewLog occurs", e.getMessage());
		} catch (Exception e) {
			LogUtil.e("ParsingPlayViewLog occurs", e.getClass().getName() + " - " + e.getMessage());
		} finally {
			inputTuple.clear();
		}
		this.doPostProcessing();
	}

}
