package services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import database.DataDAO;
import models.BatchThread;
import models.ExportedChartData;
import models.ExportedTableValue;
import models.FilterAttribute;

/**
 * Hello world!
 *
 */
@Path("batch")
public class BatchService 
{
   
	@GET
	@Path("init")
    public int init() {
		return BatchThread.getInstance().initialize();
    }
	
	@GET
	@Path("start")
    public void start() {
		BatchThread.getInstance().start();
    }
	
	@GET
	@Path("stop")
    public void stop() {
		BatchThread.getInstance().suspend();
    }
	
	@GET
	@Path("state")
	public int getPercentDone() {
		return BatchThread.getInstance().getCurrentReadLines();
	}
	
	@POST
	@Path("values/table")
	@Produces("application/json")
	@Consumes("text/plain")
	public String getValuesByOrigins(String iFilter) {
		List<ExportedTableValue> lExport = new ArrayList<ExportedTableValue>();
		Map<String, Integer> lMap = new DataDAO().getValueByOrigin(this._deserializeFilter(iFilter));
		
		for(Map.Entry<String, Integer> entry : lMap.entrySet()) {
			ExportedTableValue lData = new ExportedTableValue();
			
		    lData.Origin = entry.getKey();
		    lData.Value = entry.getValue();
		    
		    lExport.add(lData);
		}
		
		return this._serialize(lExport);	
	}

	
	@POST
	@Path("values/chart")
	@Produces("application/json")
	@Consumes("text/plain")
	public String getValuesForChat(String iFilter) {


		List<ExportedChartData> lExport = new ArrayList<ExportedChartData>();
		Map<String, Integer> lMap = new DataDAO().getSumBy(this._deserializeFilter(iFilter));
		
		for(Map.Entry<String, Integer> entry : lMap.entrySet()) {
			ExportedChartData lData = new ExportedChartData();
			
		    lData.TimeStp = entry.getKey();
		    lData.Value = entry.getValue();
		    
		    lExport.add(lData);
		}
		
		return this._serialize(lExport);		
	}
	
	private FilterAttribute _deserializeFilter(String iFilter) {
		Gson ljson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		return ljson.fromJson(iFilter, FilterAttribute.class);
	}
	
	private String _serialize(Object iData) {
		Gson ljson = new Gson();
		return ljson.toJson(iData);	
	}
	
	
}
