package cn.com.yeexun.datamap.util;
 
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
 







import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cn.com.yeexun.collectTask.service.ICollectTaskService;
import cn.com.yeexun.dataSource.entity.DataSource;
//import cn.com.yeexun.dataSource.service.impl.IDataSourceService;
import cn.com.yeexun.datamap.service.IDataMapService;
 
 
/**
 * Description: 
 *<p>
 *定时连接数据库查询数据。防止数据库连接池连接超时
 *</p>
 *
 * @author yinpf
 * @version 1.0   
 *
 */
public class CasePlanSyncTimer {
 
	/**
	 * LOG
	 */
	private Logger log = LoggerFactory.getLogger(CasePlanSyncTimer.class);
	/**
	 * 类名
	 */
	private String className = getClass().getName();
	/**
	 * 类名
	 */
	@Autowired
	private IDataMapService dataMapService;
	
//	@Autowired
//	private IDataSourceService dataSourceService;
	
	@Autowired
	private ICollectTaskService collectTaskService;
	
	/**
	 * 时间间隔(默认一天)
	 */
    private static final long PERIOD_DAY = 24 * 60 * 60 * 1000;
    /**
     * 查询时间(默认凌晨0点)
     */
    private static final int REGULAR_CHECK_TIME = 0;
	/**
	 * 启动定时任务
	 */
	public void startRegularCheckDataConnect() {
		//定时同步所有未同步排期并同步
		/* Calendar calendar = Calendar.getInstance();  
	        calendar.set(Calendar.HOUR_OF_DAY, REGULAR_CHECK_TIME); //具体执行时间点  
	        calendar.set(Calendar.MINUTE,0);  
	        calendar.set(Calendar.SECOND,0);  
	        Date date=calendar.getTime(); //第一次执行定时任务的时间  
	        //如果第一次执行定时任务的时间 小于当前的时间  
	        //此时要在 第一次执行定时任务的时间加一天，以便此任务在下个时间点执行。如果不加一天，任务会立即执行。  
	        if (date.before(new Date())) {  
	            date = this.addDay(date, 1);  
	        }  
	        Timer timer = new Timer();  
	        TimerTask task =new TimerTask() {
				public void run() {
					try {
						System.out.println("--------开始执行定时任务---------"+ new Date());
						List<DataSource> sources = dataSourceService.getDataSourceByLayer("汇总层");
						if (sources != null && sources.size() > 0) {
							for (DataSource dataSource : sources) {
								if (collectTaskService.isRDBMS(dataSource)) {
									Float size = dataMapService.getDataSize(dataSource);
									dataSource.setDataSize(size);
									dataSourceService.update(dataSource);
								}
							}
						}
					} catch (Exception e) {
						log.debug("获取数据库的数据大小出错！");
						e.printStackTrace();
					}
				}
			};  
	        //安排指定的任务在指定的时间开始进行重复的固定延迟执行。  
	        timer.schedule(task,date,PERIOD_DAY);*/
	}
	/**
	 * 增加或减少天数 
	 */
    private Date addDay(Date date, int num) {  
        Calendar startDT = Calendar.getInstance();  
        startDT.setTime(date);  
        startDT.add(Calendar.DAY_OF_MONTH, num);  
        return startDT.getTime();  
    }
    
    
}

