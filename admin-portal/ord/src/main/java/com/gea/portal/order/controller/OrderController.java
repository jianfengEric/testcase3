package com.gea.portal.order.controller;

import com.gea.portal.order.dto.BaseServiceDto;
import com.gea.portal.order.dto.GeaTxQueueDto;
import com.gea.portal.order.dto.GeaTxQueueInDto;
import com.gea.portal.order.dto.OrderDetailDto;
import com.gea.portal.order.service.GlobalService;
import com.gea.portal.order.service.OrderService;
import com.gea.portal.order.service.impl.PreOrderServiceImpl;
import com.gea.portal.order.service.impl.ProOrderServiceImpl;
import com.gea.portal.order.util.OrderStatusEnum;
import com.tng.portal.ana.util.AnaBeanUtils;
import com.tng.portal.common.constant.PermissionId;
import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.service.AnaApplicationService;
import com.tng.portal.common.vo.PageDatas;
import com.tng.portal.common.vo.rest.RestfulResponse;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("order-enquriy/v1")
public class OrderController extends BaseController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private PreOrderServiceImpl preOrderService;

    @Autowired
    private ProOrderServiceImpl proOrderService;

    @Autowired
    private GlobalService globalService;
    
    private OrderService getOrderServiceByInstance(String env) {
        OrderService orderService = null;
        if (StringUtils.equals(env, Instance.PRE_PROD.getValue())) {
            orderService = preOrderService;
        } else if (StringUtils.equals(env, Instance.PROD.getValue())) {
            orderService = proOrderService;
        }

        return orderService;
    }

    /***
     * need to config permission to orddev
     *db
     * @param instance
     *            PRE_PROD/ PROD
     * @param transferId
     * @param serviceType
     * @param pageNo
     * @param pageSize
     * @param sortBy
     * @param isAscending
     * @return
     */
    @PreAuthorize("hasPermission('ENQUIRY_ORDER_LISTING',"+ PermissionId.VIEW +")")
    @GetMapping("/get-order-list")
    @ResponseBody
    public RestfulResponse<PageDatas<GeaTxQueueDto>> getOrderList(
            @ApiParam(value = "instance") @RequestParam(required = true)Instance instance,
            @ApiParam(value = "transferId") @RequestParam(required = false) String transferId,
            @ApiParam(value = "the condition takes effect when the administrator queries") @RequestParam(required = false) String participantId,
            @ApiParam(value = "participantName") @RequestParam(required = false) String participantName,
            @ApiParam(value = "yyyy-MM-dd or yyyy-MM-dd HH:mm:ss") @RequestParam(required = false) String beginSubmissionDateTime,
            @ApiParam(value = "yyyy-MM-dd or yyyy-MM-dd HH:mm:ss") @RequestParam(required = false) String endSubmissionDateTime,
            @ApiParam(value = "yyyy-MM-dd or yyyy-MM-dd HH:mm:ss") @RequestParam(required = false) String beginUpdateTime,
            @ApiParam(value = "yyyy-MM-dd or yyyy-MM-dd HH:mm:ss") @RequestParam(required = false) String endUpdateTime,
            @ApiParam(value = "yyyy-MM-dd") @RequestParam(required = false) String submissionDateTime,
            @ApiParam(value = "yyyy-MM-dd") @RequestParam(required = false) String updateTime,
            @ApiParam(value = "serviceType Id") @RequestParam(required = false) String serviceType,
            @ApiParam(value = "status") @RequestParam(required = false) OrderStatusEnum status,
            @ApiParam(value = "gea_tx_status code") @RequestParam(required = false) String geaStatus,
            @ApiParam(value = "remark") @RequestParam(required = false) String remark,
            @ApiParam(value = "current page number") @RequestParam(required = false, defaultValue = "1") Integer pageNo,
            @ApiParam(value = "page size") @RequestParam(required = false, defaultValue = "50") Integer pageSize,
            @ApiParam(value = "sort by") @RequestParam(required = false) String sortBy,
            @ApiParam(value = "true--ascend or false--descend") @RequestParam(required = false, defaultValue = "false") Boolean isAscending) {
        RestfulResponse<PageDatas<GeaTxQueueDto>> restResponse = new RestfulResponse<>();
        GeaTxQueueInDto geaTxQueueInDto = new GeaTxQueueInDto();
        //request params populate the inParamDto
        AnaBeanUtils.requestParamsCopyToBean(geaTxQueueInDto, request);
        /**
         * participantId condition takes effect when the administrator queries
         */
        geaTxQueueInDto.setParticipantIdByLogin(getOwnParticipantId());
        OrderService orderService = getOrderServiceByInstance(instance.getValue());
        if(null == orderService){
            restResponse.setFailStatus();
            restResponse.setMessageEN("instance's value error");
            return restResponse;
        }
        PageDatas<GeaTxQueueDto> pageData = orderService.getOrders(geaTxQueueInDto, pageNo, pageSize,
                sortBy, isAscending);

        restResponse.setData(pageData);
        restResponse.setSuccessStatus();
        return restResponse;
    }

    @PreAuthorize("hasPermission('ENQUIRY_ORDER_DETAIL',"+ PermissionId.VIEW +")")
    @GetMapping("/get-order-detail")
    @ResponseBody
    public RestfulResponse<OrderDetailDto> getOrderDetail(
            @ApiParam(value = "transferId") @RequestParam(required = true) String transferId,
            @ApiParam(value = "instance") @RequestParam(required = true) Instance instance) {
        RestfulResponse<OrderDetailDto> restResponse = new RestfulResponse<>();
        String participantId = getOwnParticipantId();// get login userId
        OrderService orderService = getOrderServiceByInstance(instance.getValue());
        if(null == orderService){
            restResponse.setFailStatus();
            restResponse.setMessageEN("instance's value error");
            return restResponse;
        }
        OrderDetailDto orderDetail = orderService.getOrderDetail(transferId, participantId);
        restResponse.setSuccessStatus();
        restResponse.setData(orderDetail);
        return restResponse;
    }
    
    @GetMapping("get-order-status-list")
    @ResponseBody
    public RestfulResponse<List<BaseServiceDto>> getOrderStatusList() {
    	return globalService.getOrderStatusList();
    }
    /***
     * get baseService list
     * fixed DEV-3997 
     * @return
     */
    @GetMapping("get-order-service-type-list")
    @ResponseBody
    public RestfulResponse<List<com.tng.portal.common.dto.srv.BaseServiceDto>> getServiceList() {
        return globalService.getServiceList();
    }

    @GetMapping("get-gea-status-list")
    @ResponseBody
    public RestfulResponse<List<BaseServiceDto>> getGeaTXStatusList() {
        return globalService.getGeaTXStatusList();
    }

}
