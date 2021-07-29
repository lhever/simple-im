package com.lhever.simpleim.router.service;

import com.lhever.simpleim.common.msg.CreateGroupReq;
import com.lhever.simpleim.router.dao.GroupDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>类说明：</p>
 *
 * @author lihong10 2021/7/29 21:38
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2021/7/29 21:38
 * @modify by reason:{方法名}:{原因}
 */
@Service
public class GroupService {

    @Autowired
    private GroupDao groupDao;

    public void createGroup(CreateGroupReq req) {
        groupDao.createGroup(req);
    }


}
