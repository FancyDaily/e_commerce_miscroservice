package com.e_commerce.miscroservice.message.controller;

import com.e_commerce.miscroservice.commons.entity.application.TEvent;
import com.e_commerce.miscroservice.commons.entity.application.TFormid;
import com.e_commerce.miscroservice.commons.entity.application.TMessageNotice;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.enums.SetTemplateIdEnum;
import com.e_commerce.miscroservice.message.dao.EventDao;
import com.e_commerce.miscroservice.message.dao.FormidDao;
import com.e_commerce.miscroservice.message.dao.MessageNoticeDao;
import com.e_commerce.miscroservice.message.service.EventService;
import com.e_commerce.miscroservice.message.service.PublishService;
import com.e_commerce.miscroservice.message.vo.TemplateData;
import com.e_commerce.miscroservice.message.vo.WxMssVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 消息模块的公共方法
 *
 */
@Component
public class MessageCommonController extends BaseController {


    @Autowired
    private MessageNoticeDao messageNoticeDao;

    @Autowired
    private PublishService publishService;

    @Autowired
    private EventService eventService;

    @Autowired
    private FormidDao formidDao;

    /**
     * 插入系统消息
     *
     * @param relevanceId
     * @param nowUser
     * @param title
     * @param content
     * @param toUserId
     * @param nowTime
     * @return
     */
    public Long messageSave (Long relevanceId , TUser nowUser , String title , String content , Long toUserId , Long nowTime){
        TMessageNotice messageNotice = new TMessageNotice();
        messageNotice.setId(snowflakeIdWorker.nextId());
        messageNotice.setNoticeUserId(toUserId);
        messageNotice.setRelevanceId(relevanceId);
        messageNotice.setType(1);// 1 代表系统通知
        messageNotice.setNoticeTitle(title);
        messageNotice.setNoticeMessage(content);
        messageNotice.setCreateTime(nowTime);
        messageNotice.setCreateUser(nowUser.getId());
        messageNotice.setCreateUserName(nowUser.getName());
        messageNotice.setUpdateTime(nowTime);
        messageNotice.setUpdateUser(nowUser.getId());
        messageNotice.setUpdateUserName(nowUser.getName());
        messageNotice.setIsValid("1");
        long save = messageNoticeDao.insert(messageNotice);
        return save;
    }

    /**
     * 发送服务通知
     *
     * @param openid
     * @param formid
     * @param msg
     * @param setTemplateIdEnum
     * @param parameter
     * @return
     */
    public String pushOneUserMsg(String openid, String formid, List<String> msg, SetTemplateIdEnum setTemplateIdEnum , String parameter) {
        RestTemplate restTemplate = new RestTemplate();
        //String token = getToken();
        //String url = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send" + "?access_token=" + token; TODO 等待获取token
        String url = "";
        // 拼接推送的模版
        WxMssVo wxMssVo = new WxMssVo();
        wxMssVo.setTouser(openid);// 用户openid
        wxMssVo.setTemplate_id(setTemplateIdEnum.getSetTemplateId());
        if (!setTemplateIdEnum.getUrl().isEmpty()) {
            wxMssVo.setPage(setTemplateIdEnum.getUrl()+parameter);
        }
        wxMssVo.setForm_id(formid);// formid

        Map<String, TemplateData> m = new HashMap<>(msg.size());

        // 循环放入数据
        for (int i = 0; i < msg.size(); i++) {
            TemplateData keyword = new TemplateData();
            keyword.setValue(msg.get(i));
            m.put("keyword" + (i + 1), keyword);
            wxMssVo.setData(m);
        }
//		if (setTemplateIdEnum.getEnlarge() != 0) {
//			wxMssVo.setEmphasis_keyword("keyword" + setTemplateIdEnum.getEnlarge()+".DATA");
//		}
        logger.info("小程序推送结果={}", url);
        logger.info("小程序推送结果={}", wxMssVo.toString());
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, wxMssVo, String.class);
        logger.info("小程序推送结果={}", responseEntity.getBody());
        return responseEntity.getBody();
    }

    /**
     * 通过labelsId和key获取key
     * @param labelsId
     * @param key
     * @return
     */
    public String getValue(long labelsId , String key){
        return publishService.getValue(labelsId , key);
    }

    /**
     * 插入事件
     * @param event
     * @return
     */
    public long insertTevent(TEvent event){
        return eventService.insertTevent(event);
    }

    /**
     * 更新事件
     * @param event
     * @return
     */
    public long updateTevent(TEvent event){
        return eventService.updateTevent(event);
    }

    /**
     * 查找事件
     * @param id
     * @return
     */
    public TEvent selectTeventById(Long id){
        return eventService.selectTeventById(id);
    }

    /**
     * 更新formId
     * @param formid
     * @return
     */
    public long updateFormId(TFormid formid){return formidDao.updateFormId(formid); }

    /**
     * 找到一条可用的formId
     * @param findTime
     * @param userId
     * @return
     */
    public TFormid selectCanUseFormId(Long findTime , Long userId){return formidDao.selectAllFormIdCanUse(findTime , userId);}
}
