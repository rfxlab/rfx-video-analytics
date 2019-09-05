package rfxlab.com.demo.starter;

import rfx.core.stream.model.KafkaTaskDef;
import rfx.core.stream.model.TaskDef;
import rfx.core.stream.worker.StreamProcessingWorker;
import rfx.core.util.LogUtil;
import rfx.core.util.StringUtil;
import rfx.core.util.Utils;
import rfxlab.com.demo.analytics.LogTokenizing;

public class DataReactorWorker extends StreamProcessingWorker {

    private static final String SIMPLE_NAME = DataReactorWorker.class.getSimpleName();

    public DataReactorWorker(String name) {
	super(name);
	System.out.println(SIMPLE_NAME + ":" + name);
    }

    @Override
    protected void onBeforeBeStopped() {
	System.out.println(" =====>>> worker.restart <<<====");
	LogTokenizing.stopProcessing();
    }

    public static void main(String[] args) {
	if (args.length != 5) {
	    System.err.println("need 5 params [topic] [host] [port] [beginPartitionId] [endPartitionId]");
	    return;
	}
	LogUtil.setDebug(false);

	String topic = args[0];
	String host = args[1];
	int port = StringUtil.safeParseInt(args[2]);
	int beginPartition = StringUtil.safeParseInt(args[3]);
	int endPartition = StringUtil.safeParseInt(args[4]);

	TaskDef taskDef = new KafkaTaskDef(topic, beginPartition, endPartition);
	new DataReactorWorker(SIMPLE_NAME + ":" + topic).setTaskDef(taskDef).start(host, port);
	Utils.sleep(2000);
    }
}
