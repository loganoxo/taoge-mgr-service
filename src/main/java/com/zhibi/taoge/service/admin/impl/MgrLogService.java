package com.zhibi.taoge.service.admin.impl;

import com.zhibi.taoge.dao.MgrLogDao;
import com.zhibi.taoge.entity.console.MgrLog;
import com.zhibi.taoge.service.admin.IMgrLogService;
import org.springframework.stereotype.Service;

/**
 * @author QinHe at 2018-10-26
 */

@Service
public class MgrLogService implements IMgrLogService {
    private final MgrLogDao mgrLogDao;

    public MgrLogService(MgrLogDao mgrLogDao) {
        this.mgrLogDao = mgrLogDao;
    }

    @Override
    public void save(MgrLog log) {
        mgrLogDao.save(log);
    }
}
