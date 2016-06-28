package love.image.timer;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UpdateTimer {

	private final Log log = LogFactory.getLog(UpdateTimer.class);

	public void execute() {

		log.info("\n\n定时任务：To Do Something.");
		// synchronized (this) {
		// log.info("bgn -- " + System.currentTimeMillis());
		// orderService.someBodyToBuy();
		// log.info("end -- " + System.currentTimeMillis());
		// }
	}
}
