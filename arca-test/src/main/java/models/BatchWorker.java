package models;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Date;
import java.util.Scanner;

import database.DatabaseConnection;

public class BatchWorker implements Runnable {
	
	private volatile boolean running = true;
	private int currentLine;
	private int totalLines;
	private String filePath;
	
	public BatchWorker(int iLastLine, String iFilePath) {
		this.setCurrentLine(iLastLine);
		this.setFilePath(iFilePath);
		this.setTotalLines();
	}
	
	@Override
	public void run() {
		running = true;
		while(running) {
			FileInputStream inputStream = null;
			Scanner sc = null;
			try {
				DatabaseConnection.getInstance().getCurrentConnection().setAutoCommit(false);
		    
				inputStream = new FileInputStream(this.getFilePath());
			    sc = new Scanner(inputStream, "UTF-8");
			    for (int i=0; i < this.getCurrentLine(); i++)
			    	sc.nextLine();
			    while (sc.hasNextLine()) {
			        String lLine = sc.nextLine();
			        
			        String[] lInfo = lLine.split(",");
			        
			        Date lDate = new Date(Long.parseLong(lInfo[0]));
			        int lValue = Integer.parseInt(lInfo[1]);
			        String lOrigin = lInfo[2];
			        
			        new Data(lDate, lValue, lOrigin).save();			       			       
			        
			        this.currentLine++;
			        if (this.currentLine % 1000 == 0)
			        	DatabaseConnection.getInstance().getCurrentConnection().commit();
			        
			        if (!running)
			        	break;
			    }
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
			    if (inputStream != null) {
			        try {
						inputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			    }
			    if (sc != null)
			        sc.close();			    
			    
			    try {
			    	DatabaseConnection.getInstance().getCurrentConnection().commit();
					DatabaseConnection.getInstance().getCurrentConnection().setAutoCommit(true);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public int stop() {
		running = false;
		return this.getCurrentLine();
	}

	public int getCurrentLine() {
		return currentLine;
	}

	private void setCurrentLine(int currentLine) {
		this.currentLine = currentLine;
	}

	private String getFilePath() {
		return filePath;
	}

	private void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public int getTotalLines() {
		return totalLines;
	}

	private void setTotalLines() {			    
		InputStream is;
		try {
			is = new BufferedInputStream(new FileInputStream(this.getFilePath()));
			 try {
			    	
			        byte[] c = new byte[1024];
			        int count = 0;
			        int readChars = 0;
			        boolean empty = true;
			        while ((readChars = is.read(c)) != -1) {
			            empty = false;
			            for (int i = 0; i < readChars; ++i) {
			                if (c[i] == '\n') {
			                    ++count;
			                }
			            }
			        }
			        this.totalLines = (count == 0 && !empty) ? 1 : count;
			    } 
			    catch (Exception e) {
			    	e.printStackTrace();
			    	this.totalLines = 0;
			    }
			    finally {
			        try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			    }
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			this.totalLines = 0;
		}
	   
	}
}
