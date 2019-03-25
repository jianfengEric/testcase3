/*
package com.tng.portal.common.soa;

import com.google.gson.*;
import com.rabbitmq.client.*;
import com.tng.portal.common.util.MethodParameters;
import com.tng.portal.common.vo.rest.RestfulResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;


public class NioSocketClient {
    private static transient Logger log = LoggerFactory.getLogger(NioSocketClient.class);
    private static Properties pros = new Properties();
    private static Connection conn = null;
    private static Channel channel = null;
    private static String exchange = null;
    static {
        try {
            pros.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("rabbitMQ.properties"));
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(pros.getProperty("rabbitmq.host"));
            factory.setPort(Integer.valueOf(pros.getProperty("rabbitmq.port")));
            factory.setUsername(pros.getProperty("rabbitmq.username"));
            factory.setPassword(pros.getProperty("rabbitmq.password"));
            factory.setVirtualHost(pros.getProperty("rabbitmq.vhost"));
            factory.setAutomaticRecoveryEnabled(Boolean.valueOf(pros.getProperty("rabbitmq.automaticrecoveryenabled")));
            factory.setNetworkRecoveryInterval(Long.valueOf(pros.getProperty("rabbitmq.networkrecoveryinterval")));//ms
            factory.setTopologyRecoveryEnabled(Boolean.valueOf(pros.getProperty("rabbitmq.topologyrecoveryenabled")));
            factory.setRequestedHeartbeat(Integer.valueOf(pros.getProperty("rabbitmq.requestedheartbeat")));//s
            factory.setConnectionTimeout(Integer.valueOf(pros.getProperty("rabbitmq.connectiontimeout")));//ms
            exchange = pros.getProperty("rabbitmq.exchange");
            conn = factory.newConnection();
            channel = conn.createChannel();

        } catch (Exception e) {
            log.error("Can't Connection Rabbitmq ...", e);
        }
    }

    private NioSocketClient(){}

    public static void write(String msg,Thread callBack){
        try {
            if(channel==null||!channel.isOpen()){
                channel = conn.createChannel();
            }
            channel.exchangeDeclare(exchange, "fanout");
            channel.basicPublish(exchange, "", null, msg.getBytes("UTF-8"));
        }catch (Exception e){
            log.error("NioSocket Write Error",e);
        }finally {
            if(null != callBack){
                callBack.start();
                try {
                    callBack.join(200);
                } catch (Exception e) {
                    log.debug("callBack.join Error",e);
                }
            }
        }
    }

}
*/
