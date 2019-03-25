/*
package com.tng.portal.ana.soa;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tng.portal.ana.service.TokenService;
import com.tng.portal.ana.vo.AnaAccountAccessTokenDto;
import com.tng.portal.common.soa.IoHandlerAdapter;
import com.tng.portal.common.soa.NioSocketClient;
import com.tng.portal.common.soa.Packet;
import com.tng.portal.common.util.ApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.lang.reflect.Type;
import java.util.*;

public class AnaIoHandlerAdapter extends IoHandlerAdapter {

    private transient Logger log = LoggerFactory.getLogger(AnaIoHandlerAdapter.class);
    private static TokenService tokenService;
    private Random random = new Random();
    @Override
    public void messageReceived(String message) {
        super.messageReceived(message);
        System.out.println(message);
        try {
            ObjectMapper mapper = new ObjectMapper();
            Packet packet = mapper.readValue(message,Packet.class);
            String localServerName = ApplicationContext.get(ApplicationContext.Key.serviceName);
            Thread.sleep(random.nextInt(100));
            if (packet.getApplications().contains(localServerName) && Method.syncToken.equals(packet.getMethod())) {
                JavaType javaType = mapper.getTypeFactory().constructParametricType(HashMap.class, String.class, AnaAccountAccessTokenDto.class);
                HashMap<String, AnaAccountAccessTokenDto> data = mapper.readValue(mapper.writeValueAsString(packet.getData()), javaType);
                getTokenService().syncToken(data.get(localServerName));
                packet.setApplications(new ArrayList(Arrays.asList("ANA")));
                packet.setMethod(Method.callBACK);
                packet.setData(null);
                NioSocketClient.write(mapper.writeValueAsString(packet), null);
            } else if (packet.getApplications().contains(localServerName) && Method.callBACK.equals(packet.getMethod())) {
                AnaIoHandlerCallback.push(packet.getKey());
            } else if (packet.getApplications().contains(localServerName) && Method.clearToken.equals(packet.getMethod())) {
                JavaType javaType = mapper.getTypeFactory().constructParametricType(HashMap.class,String.class, AnaAccountAccessTokenDto.class);
                HashMap<String,AnaAccountAccessTokenDto> data = mapper.readValue(mapper.writeValueAsString(packet.getData()),javaType);
                getTokenService().clearToken(data.get(localServerName));
            } else {
                log.debug("Can not find service");
            }
        }catch (DataIntegrityViolationException dive){
            log.debug("data integrity violation error",dive);
        }catch (ObjectOptimisticLockingFailureException oolfe){
            log.debug("object optimistic locking failure error",oolfe);
        }catch (Exception e){
            log.error("messageReceived error",e);
        }
    }

    public static class Method{
        public final static String syncToken = "SYNCTOKEN";
        public final static String clearToken = "CLEARBACK";
        private final static String callBACK = "CALLBACK";

    }

    public static class Service{
        public final static String tokenServiceImpl = "tokenServiceImpl";
    }

    private static TokenService getTokenService() {
        return tokenService;
    }

    public static void setTokenService(TokenService tokenService) {
        AnaIoHandlerAdapter.tokenService = tokenService;
    }
}
*/
