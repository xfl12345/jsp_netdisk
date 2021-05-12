package com.github.xfl12345.jsp_netdisk.model.service;

import com.github.xfl12345.jsp_netdisk.appconst.field.MySessionAttributes;
import com.github.xfl12345.jsp_netdisk.model.dao.TbDirectoryDao;
import com.github.xfl12345.jsp_netdisk.model.pojo.database.TbAccount;
import com.github.xfl12345.jsp_netdisk.model.pojo.database.TbDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("tbDirectoryService")
public class TbDirectoryService {

    private final Logger logger = LoggerFactory.getLogger(TbDirectoryService.class);

    @Autowired
    private TbDirectoryDao tbDirectoryDao;

    /**
     * 通过实体作为筛选条件统计
     *
     * @param tbDirectory 实例对象
     * @return long型的总数
     */
    public long countAll(TbDirectory tbDirectory) {
        return tbDirectoryDao.countAll(tbDirectory);
    }

    /**
     * 通过ID查询单条数据
     *
     * @param directoryId 主键
     * @return 实例对象
     */
    public TbDirectory queryById(Long directoryId) {
        return tbDirectoryDao.queryById(directoryId);
    }

    /**
     * 查询指定行数据
     *
     *
     * @param tbDirectory
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    public List<TbDirectory> queryAllByLimit(TbDirectory tbDirectory, int offset, int limit) {
        return tbDirectoryDao.queryAllByLimit(tbDirectory, offset, limit);
    }

    /**
     * 通过实体作为筛选条件查询
     *
     * @param tbDirectory 实例对象
     * @return 对象列表
     */
    public List<TbDirectory> queryAll(TbDirectory tbDirectory) {
        return tbDirectoryDao.queryAll(tbDirectory);
    }

    /**
     * 新增数据
     *
     * @param tbDirectory 实例对象
     * @return 影响行数
     */
    public int insert(TbDirectory tbDirectory) {
        return tbDirectoryDao.insert(tbDirectory);
    }

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<TbDirectory> 实例对象列表
     * @return 影响行数
     */
    public int insertBatch(List<TbDirectory> entities) {
        return tbDirectoryDao.insertBatch(entities);
    }

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<TbDirectory> 实例对象列表
     * @return 影响行数
     */
    public int insertOrUpdateBatch(List<TbDirectory> entities) {
        return tbDirectoryDao.insertOrUpdateBatch(entities);
    }

    /**
     * 修改数据
     *
     * @param tbDirectory 实例对象
     * @return 影响行数
     */
    public int update(TbDirectory tbDirectory) {
        return tbDirectoryDao.update(tbDirectory);
    }

    /**
     * 通过主键删除数据
     *
     * @param directoryId 主键
     * @return 影响行数
     */
    public int deleteById(Long directoryId) {
        return tbDirectoryDao.deleteById(directoryId);
    }
}
