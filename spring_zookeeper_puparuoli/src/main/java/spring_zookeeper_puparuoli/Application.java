package spring_zookeeper_puparuoli;

import java.util.HashMap;
import java.util.Properties;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import zookeeper.utils.ZkConnector;

public class Application {

	public static void main(String[] args) {

		ClassPathXmlApplicationContext aC = new ClassPathXmlApplicationContext("springconfig.xml");
		Properties propertiesDaAggiungereAlContestoSpring = new Properties();
		HashMap<String,String> proprietaDaZk = new HashMap<String,String>();
		
		ZkConnector zkConn = aC.getBean("zkconnector",ZkConnector.class);
		
		
		try 
		{
			zkConn.risolviGerarchiaZooKeeperComeFormaDocumentoProperties("/", proprietaDaZk);
			for(String nomeProprieta : proprietaDaZk.keySet())
			{
//				System.out.println(t+" : "+toFill.get(t));
				propertiesDaAggiungereAlContestoSpring.setProperty(nomeProprieta , proprietaDaZk.get(nomeProprieta));
			}
			
			PropertySourcesPlaceholderConfigurer configurer =
	                new PropertySourcesPlaceholderConfigurer();

			configurer.setProperties(propertiesDaAggiungereAlContestoSpring);
			configurer.setIgnoreUnresolvablePlaceholders(true);

			aC.addBeanFactoryPostProcessor(configurer);   
			aC.refresh();
	
			//test se funziona (chiedo allocazione di un bean con scope prototype usando una proprità caricata usando la struttura del filesystem di zookeeper)
			Studente st = aC.getBean("studente",Studente.class);
			System.out.println(st.getNome());

		} 
		catch (Exception e) 
		{
			// TODO GESTIRE
			e.printStackTrace();
		}
		
		
		
		
		
		
		 
		
		
		
	}

}
