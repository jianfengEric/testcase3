package com.gea.portal.srv.controller;


import com.gea.portal.srv.entity.ServiceBatchChgReq;
import com.gea.portal.srv.service.SrvBaseService;
import com.tng.portal.common.dto.srv.ServiceBatchChgReqDto;
import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.vo.PageDatas;
import com.tng.portal.common.vo.rest.RestfulResponse;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("base_service/v1")
public class BaseServiceController {
    @Autowired
    private SrvBaseService baseService;

    @GetMapping("get-service-base-markup")
    @ResponseBody
    public RestfulResponse<PageDatas> getServiceBaseMarkups(@ApiParam(value="current page number")@RequestParam(value = "pageNo", required = false) Integer pageNo,
                                                            @ApiParam(value="page size")@RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                            @ApiParam(value="sort by")@RequestParam(value = "sortBy", required = false) String sortBy,
                                                            @ApiParam(value="true--ascend or false--descend")@RequestParam(value = "isAscending", required = false) Boolean isAscending,
                                                            @RequestParam Instance instance){
        return baseService.getServiceBaseMarkups(pageNo, pageSize,sortBy,isAscending,instance);
    }

    @PostMapping("submit-service-base-markup-data")
    @ResponseBody
    public RestfulResponse<String> submitServiceBaseMarkupData(@RequestBody(required = false)ServiceBatchChgReqDto serviceBatchChgReqDto){
        ServiceBatchChgReq serviceBatchChgReq=baseService.submitServiceBaseMarkupData(serviceBatchChgReqDto);
        return RestfulResponse.ofData(String.valueOf(serviceBatchChgReq.getId()));
    }

    @GetMapping("check-edit")
    @ResponseBody
    public RestfulResponse<Boolean> hasPending(@RequestParam Instance instance) {
        Boolean hasPendingStatus = baseService.checkEdit(instance);
        return RestfulResponse.ofData(hasPendingStatus);
    }
}
