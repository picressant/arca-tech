package models;

public class BatchThread {
	private static BatchThread instance;
	
	private Thread thread;
	
	private BatchWorker worker;
	
	private int lastLine = 0;
	
	
	public static BatchThread getInstance() {
		if (instance == null)
			instance = new BatchThread();
		
		return instance;
	}
	
	public int initialize() {
		if (this.worker == null) {
			this.worker = new BatchWorker(this.lastLine, "D:\\Work\\exoTechnique\\Data\\data.txt");
		}
		return this.worker.getTotalLines();
	}

	
	public void start() {
		this.thread = new Thread(this.worker);
		this.thread.start();
	}
	
	public void suspend() {
		this.lastLine = this.worker.stop();
	}
	
	public int getCurrentReadLines() {		
		return this.worker.getCurrentLine();
	}
}
