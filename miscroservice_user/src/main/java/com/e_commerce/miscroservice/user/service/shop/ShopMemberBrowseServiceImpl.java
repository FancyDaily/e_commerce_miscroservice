package com.e_commerce.miscroservice.user.service.shop;

import com.e_commerce.miscroservice.commons.entity.SimplePage;
import com.e_commerce.miscroservice.commons.exception.ErrorHelper;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.utils.BeanKit;
import com.e_commerce.miscroservice.user.dao.ShopMemberBrowseDao;
import com.e_commerce.miscroservice.user.dao.StoreBusinessDao;
import com.e_commerce.miscroservice.user.entity.ShopMemberBrowse;
import com.e_commerce.miscroservice.user.entity.StoreBusiness;
import com.e_commerce.miscroservice.user.mapper.ShopMemberBrowseMapper;
import com.e_commerce.miscroservice.user.mapper.StoreBusinessMapper;
import com.e_commerce.miscroservice.user.vo.ShopMemberBrowseQuery;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/7 15:45
 * @Copyright 玖远网络
 */
@Service
public class ShopMemberBrowseServiceImpl implements ShopMemberBrowseService{

    private Log logger = Log.getInstance(ShopMemberBrowseServiceImpl.class);

    @Autowired
    private ShopMemberBrowseMapper shopMemberBrowseMapper;
    @Autowired
    private ShopMemberBrowseDao shopMemberBrowseDao;
    @Autowired
    private StoreBusinessMapper storeBusinessMapper;
    @Autowired
    private StoreBusinessDao storeBusinessDao;

    /**
     * 新增用户的浏览记录
     *
     * @param query query
     * @return com.e_commerce.miscroservice.user.entity.ShopMemberBrowseHistory
     * @author Charlie
     * @date 2018/12/7 15:49
     */
    @Override
    public ShopMemberBrowse add(ShopMemberBrowseQuery query) {

        Long inShopMemberId = query.getInShopMemberId ();
        Long targetId = query.getTargetId ();
        Integer type = query.getType ();
        logger.info ("新增小程序用户浏览记录 inShopMemberId={},targetId={},type={}", inShopMemberId, targetId, type);

        if (BeanKit.hasNull (inShopMemberId, targetId, type)) {
            ErrorHelper.declare (false, "请求参数不合法");
        }


        //如果类型多起来了, 建议使用枚举
        ShopMemberBrowse browse;
        switch (type) {
            case 1:
                StoreBusiness store = storeBusinessMapper.selectByPrimaryKey (targetId);
                ErrorHelper.declareNull (store, "没有找到门店信息");
                browse = shopMemberBrowseDao.insertShopInShop (query);
                break;
                //extend....
//            case 2:
//                break;
            default:
                throw ErrorHelper.me ( "未知的参数类型"+type);
        }
        return browse;
    }


    /**
     * 浏览记录
     *
     * @param userId userId
     * @return java.util.Map
     * @author Charlie
     * @date 2018/12/8 11:49
     */
    @Override
    public Map<String, Object> listShopInShop(Long userId, Integer pageSize, Integer pageNumber) {
        logger.info ("用户店中店浏览列表 userId={}", userId);
        Map<String, Object> result = new HashMap<> (2);
        PageHelper.startPage (pageNumber, pageSize);
        List<Map<String, Object>> browseList = shopMemberBrowseMapper.listShopInShop (userId);
        for (Map<String, Object> browse : browseList) {
            Long wxaCloseTime = (Long) browse.get ("wxaCloseTime");
            Long wxaOpenTime = (Long) browse.get ("wxaOpenTime");
            Integer isOpenWxa = (Integer) browse.get ("isOpenWxa");
            Integer businessType = (Integer) browse.get ("wxaBusinessType");
            //没有被关闭,(过期的店中店先不做强制关闭,因为用户没有续费的入口)
            boolean isInShopCanAccess = businessType == 1 && /*BeanKit.gt0 (wxaOpenTime) && wxaCloseTime > System.currentTimeMillis () && */ObjectUtils.nullSafeEquals (1, isOpenWxa);
            browse.put ("isInShopCanAccess", isInShopCanAccess);
        }

        result.put ("dataList", new SimplePage<> (browseList));
        return result;
    }




    /**
     * 删除浏览记录
     *
     * @param userId openId
     * @param browseId browseId
     * @author Charlie
     * @date 2018/12/12 11:53
     */
    @Override
    public void delete(Long userId, Long browseId) {
        logger.info ("删除浏览记录 openId={},browseId={}", userId, browseId);

        ShopMemberBrowse history = shopMemberBrowseDao.findById (userId, browseId);
        ErrorHelper.declareNull (history, "没有找到浏览记录");
        if (history.getDelStatus ().equals (1)) {
            logger.info ("记录已删除");
            return;
        }

        history.setDelStatus (1);
        int rec = shopMemberBrowseDao.updateById (history);
        ErrorHelper.declare (rec == 1, "删除浏览记录失败");
    }
}
