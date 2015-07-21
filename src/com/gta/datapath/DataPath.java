package com.gta.datapath;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;
import net.sf.json.JSONObject;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.gta.simhash.SimHash;
import com.gta.affective.Segment;

public class DataPath {
	private int threadId;
    
	public DataPath(SimHash hash, Segment segment)
	{
		FDSAPI fdsAPI = new FDSAPI();
		File file = new File("runtime.config");
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(file));
			StringBuilder sb = new StringBuilder();
			String s = null;
			while ((s = br.readLine()) != null) 
			{
				sb.append(s);
			}
			br.close();
			
			String sets = sb.toString();
			JSONObject obj = JSONObject.fromObject(sets);
			ConnectionFactory factory = new ConnectionFactory();
			factory.setUsername(obj.getString("broker_username"));
			factory.setPassword(obj.getString("broker_password"));
			factory.setHost(obj.getString("broker_host"));
			factory.setVirtualHost(obj.getString("broker_vhost"));
			Connection conn = factory.newConnection();
			Channel channel = conn.createChannel();
		
			String exchangeName = obj.getString("broker_exchange");
			channel.exchangeDeclare(exchangeName, "topic", true);
			channel.queueDeclare("ambiguity", true, false, false, null);
			channel.queueBind("ambiguity", exchangeName, obj.getString("subscript_topic"));
			
			QueueingConsumer consumer = new QueueingConsumer(channel);
			channel.basicConsume("ambiguity", true, consumer);
			while (true) 
			{
				try 
				{
					Delivery delivery = consumer.nextDelivery();
					String message = new String(delivery.getBody());
					JSONObject element = JSONObject.fromObject(message);
					threadId = new Integer(element.getString("id"));
					String text = fdsAPI.getWebContent(threadId);
					System.out.println(text);
					SimHash hashData = new SimHash(text, 64, 8);
					System.out.println(segment.analysis(text));
                    if (!text.equals("") && text != null)
					{
//					   corpus.getResult(title);
                       hash.getResult(hashData);
                       System.out.println();
					   System.out.println("***************************************************************************************");
					   System.out.println("***************************************************************************************");
				    }
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (TimeoutException e)
		{
			e.printStackTrace();
		}
	}

}
