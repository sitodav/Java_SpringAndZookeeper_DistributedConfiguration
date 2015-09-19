package zookeeper.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component(value="zkconnector")
@Scope("prototype")
public class ZkConnector implements Watcher{
	
	private ZooKeeper zk;
	private String serverPath;
	
	@Value("${zookeeper.connectionstring}")
	public void setServerPath(String serverPath) throws IOException
	{
		this.serverPath = serverPath;
		zk = new ZooKeeper(serverPath,10000,this);
	}
	
	public String getServerPath()
	{
		return this.serverPath;
	}

	@Override
	public void process(WatchedEvent event) {
		System.out.println("Evento su connector (client) :"+event);
	}
	
	public void disconnect() throws InterruptedException
	{
		zk.close();
	}
	
	public void risolviGerarchiaZooKeeperComeFormaDocumentoProperties(String actualPathInZk, HashMap<String,String> toFill) throws Exception
	{
		
		if(zk == null) throw new Exception("connettore (client) non connesso al server");
		
		String t = (actualPathInZk.length() > 1) ? actualPathInZk.substring(0, actualPathInZk.length()-1) : actualPathInZk ;
		List<String> children =zk.getChildren(t, this);
		String prefixPropertiesStyle = (actualPathInZk.length() > 1 ) ? actualPathInZk.substring(1).replace("/", ".") : "";
		for(String child : children)
		{
			String dataInChild = new String(zk.getData(actualPathInZk+child, this, new Stat()));
			toFill.put(prefixPropertiesStyle+child, dataInChild);
			risolviGerarchiaZooKeeperComeFormaDocumentoProperties(actualPathInZk+child+"/",toFill);
		}
		
	}
}
