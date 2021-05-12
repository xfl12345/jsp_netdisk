package com.github.xfl12345.jsp_netdisk.model.service;

import com.github.xfl12345.jsp_netdisk.model.dao.DirFileDao;
import com.github.xfl12345.jsp_netdisk.model.dao.TbDirectoryDao;
import com.github.xfl12345.jsp_netdisk.model.pojo.database.DirFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("dirFileService")
public class DirFileService {

    private final Logger logger = LoggerFactory.getLogger(DirFileService.class);

    @Autowired
    private DirFileDao dirFileDao;









    /**
     * 通过实体作为筛选条件统计
     *
     * @param dirFile 实例对象
     * @return long型的总数
     */
    public long countAll(DirFile dirFile) {
        return dirFileDao.countAll(dirFile);
    }

    /**
     * 通过实体作为筛选条件查询
     *
     * @param dirFile 实例对象
     * @return 对象列表
     */
    public List<DirFile> queryAll(DirFile dirFile) {
        return dirFileDao.queryAll(dirFile);
    }

    /**
     * 查询指定行数据
     *
     * @param dirFile 实例对象
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    public List<DirFile> queryAllByLimit(DirFile dirFile, int offset, int limit) {
        return dirFileDao.queryAllByLimit(dirFile, offset, limit);
    }

    /**
     * 新增数据
     *
     * @param dirFile 实例对象
     * @return 影响行数
     */
    public int insert(DirFile dirFile) {
        return dirFileDao.insert(dirFile);
    }

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<DirFile> 实例对象列表
     * @return 影响行数
     */
    public int insertBatch(List<DirFile> entities) {
        return dirFileDao.insertBatch(entities);
    }

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<DirFile> 实例对象列表
     * @return 影响行数
     */
    public int insertOrUpdateBatch(List<DirFile> entities) {
        return dirFileDao.insertOrUpdateBatch(entities);
    }

    /**
     * 通过实体作为筛选条件删除
     *
     * @param dirFile 实例对象
     * @return 影响行数
     */
    public int deleteAll(DirFile dirFile) {
        return dirFileDao.deleteAll(dirFile);
    }
}
