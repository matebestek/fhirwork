package fhirconverter.configuration;

/**
 * All the different types of configuration files will be loaded into system as
 * instance of objects in this class
 *
 * @author Abdul-qadir Ali
 * @author Chenghui Fan
 */

//	This is the class for all kinds of configs (bind to different files)
public abstract class Config {
	private String configType;
	private String filePath;
//	private boolean isReadable;
//	private boolean isWritable;
	
//	public Config(String configType, String filePath, boolean isReadable, boolean isWritable) {
	public Config(String configType, String filePath) {
		this.configType = configType;
		this.filePath = filePath;
//		this.isReadable = isReadable;
//		this.isWritable = isWritable;
	}
	
	public String getFilePath(){
		return this.filePath;
	}
	
	public void setFilePath(String newPath){
		this.filePath = newPath;
	}
	
	public String getConfigType(){
		return this.configType;
	}
	
	public abstract void addConfig(String key, String value);
	
	public abstract void removeConfig(String key);
	
	public abstract void changeConfig(String key, String value);
	
/*	public boolean isReadable(){
		return this.isReadable;
	}
	
	public boolean isWritable(){
		return this.isWritable;
	}
	
	public void setReadPermission(boolean readPermission){
		this.isReadable = readPermission;
	}
	
	public void setWritePermission(boolean writePermission){
		this.isWritable = writePermission;
	}
*/
}