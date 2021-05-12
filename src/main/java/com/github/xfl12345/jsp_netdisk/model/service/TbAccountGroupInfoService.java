package com.github.xfl12345.jsp_netdisk.model.service;

import com.github.xfl12345.jsp_netdisk.model.dao.TbAccountGroupInfoDao;
import com.github.xfl12345.jsp_netdisk.model.pojo.database.TbAccountGroupInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("tbAccountGroupInfoService")
public class TbAccountGroupInfoService {

    private final Logger logger = LoggerFactory.getLogger(TbAccountGroupInfoService.class);

    @Autowired
    private TbAccountGroupInfoDao tbAccountGroupInfoDao;












    /**
     * 通过实体作为筛选条件统计
     *
     * @param tbAccountGroupInfo 实例对象
     * @return long型的总数
     */
    public long countAll(TbAccountGroupInfo tbAccountGroupInfo) {
        return tbAccountGroupInfoDao.countAll(tbAccountGroupInfo);
    }

    /**
     * 查询指定行数据
     *
     * @param tbAccountGroupInfo 实例对象
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    public List<TbAccountGroupInfo> queryAllByLimit(TbAccountGroupInfo tbAccountGroupInfo, int offset, int limit) {
        return tbAccountGroupInfoDao.queryAllByLimit(tbAccountGroupInfo, offset, limit);
    }

    /**
     * 通过实体作为筛选条件查询
     *
     * @param tbAccountGroupInfo 实例对象
     * @return 对象列表
     */
    public List<TbAccountGroupInfo> queryAll(TbAccountGroupInfo tbAccountGroupInfo) {
        return tbAccountGroupInfoDao.queryAll(tbAccountGroupInfo);
    }

    /**
     * 新增数据
     *
     * @param tbAccountGroupInfo 实例对象
     * @return 影响行数
     */
    public int insert(TbAccountGroupInfo tbAccountGroupInfo) {
        return tbAccountGroupInfoDao.insert(tbAccountGroupInfo);
    }

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<TbAccountGroupInfo> 实例对象列表
     * @return 影响行数
     */
    public int insertBatch(List<TbAccountGroupInfo> entities) {
        return tbAccountGroupInfoDao.insertBatch(entities);
    }

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<TbAccountGroupInfo> 实例对象列表
     * @return 影响行数
     */
    public int insertOrUpdateBatch(List<TbAccountGroupInfo> entities) {
        return tbAccountGroupInfoDao.insertOrUpdateBatch(entities);
    }

    /**
     * 通过实体作为筛选条件删除
     *
     * @param tbAccountGroupInfo 实例对象
     * @return 影响行数
     */
    public int deleteAll(TbAccountGroupInfo tbAccountGroupInfo) {
        return tbAccountGroupInfoDao.deleteAll(tbAccountGroupInfo);
    }

}
