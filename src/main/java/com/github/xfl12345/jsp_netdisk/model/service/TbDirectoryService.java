package com.github.xfl12345.jsp_netdisk.model.service;

import com.github.xfl12345.jsp_netdisk.appconst.field.TbAccountField;
import com.github.xfl12345.jsp_netdisk.appconst.field.TbDirectoryField;
import com.github.xfl12345.jsp_netdisk.model.dao.TbDirectoryDao;
import com.github.xfl12345.jsp_netdisk.model.pojo.database.TbDirectory;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("tbDirectoryService")
public class TbDirectoryService {

    private final Logger logger = LoggerFactory.getLogger(TbDirectoryService.class);

    @Autowired
    private TbDirectoryDao tbDirectoryDao;


    /**
     * 给定一个实体类，返回其目录ID
     *
     * @return 目录ID
     */
    public Long getId(TbDirectory dir) {
        if (dir == null ||
                dir.getDirectoryName() == null ||
                dir.getAccountId() == null ||
                dir.getParentDirectoryId() == null) {
            return null;
        }
        List<TbDirectory> list = queryAll(dir);
        return list.get(0).getDirectoryId();
    }


//    /**
//     * 新增一个目录，并返回其 目录ID
//     *
//     * @param insertDir TbDirectory实体类
//     * @return 目录ID
//     */
//    public Long insertDirectoryAndGetId(TbDirectory insertDir){
//        try {
//            insert(insertDir);
//        }
//        catch (DataAccessException e) {
//            Throwable cause = e.getCause();
//            if (cause instanceof MySQLIntegrityConstraintViolationException) {
//                logger.error("新增目录 发生错误！程序逻辑设计存在问题！！error=" + e);
//            } else {
//                logger.error("新增目录 发生了未知错误！请立即排查原因！！error=" + e);
//            }
//        }
//        List<TbDirectory> list = queryAll(insertDir);
//        return list.get(0).getDirectoryId();
//    }


    /**
     * 通过实体作为筛选条件统计
     *
     * @param tbDirectory 实例对象
     * @return long型的总数
     */
    public long countAll(TbDirectory tbDirectory) {
        return this.tbDirectoryDao.countAll(tbDirectory);
    }

    /**
     * 通过ID查询单条数据
     *
     * @param directoryId 主键
     * @return 实例对象
     */
    public TbDirectory queryById(Long directoryId) {
        TbDirectory tbDirectory;
        //对 根目录 进行特殊处理
        if (directoryId == null || directoryId == 0L) {
            tbDirectory = new TbDirectory();
            tbDirectory.setDirectoryId(0L);
            tbDirectory.setDirectoryName("");//根目录没有名字
            tbDirectory.setParentDirectoryId(null);//没有父节点
            tbDirectory.setAccountId(TbAccountField.ACCOUNT_ID_ROOT);//属于超级用户的
        } else {
            tbDirectory = this.tbDirectoryDao.queryById(directoryId);
        }
        return tbDirectory;
    }

    /**
     * 查询指定行数据
     *
     * @param tbDirectory 实例对象
     * @param offset      查询起始位置
     * @param limit       查询条数
     * @return 对象列表
     */
    public List<TbDirectory> queryAllByLimit(TbDirectory tbDirectory, int offset, int limit) {
        return this.tbDirectoryDao.queryAllByLimit(tbDirectory, offset, limit);
    }

    /**
     * 通过实体作为筛选条件查询
     *
     * @param tbDirectory 实例对象
     * @return 对象列表
     */
    public List<TbDirectory> queryAll(TbDirectory tbDirectory) {
        return this.tbDirectoryDao.queryAll(tbDirectory);
    }

    /**
     * 新增数据
     *
     * @param tbDirectory 实例对象
     * @return 影响行数
     */
    public int insert(TbDirectory tbDirectory) {
        return this.tbDirectoryDao.insert(tbDirectory);
    }

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<TbDirectory> 实例对象列表
     * @return 影响行数
     */
    public int insertBatch(List<TbDirectory> entities) {
        return this.tbDirectoryDao.insertBatch(entities);
    }

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<TbDirectory> 实例对象列表
     * @return 影响行数
     */
    public int insertOrUpdateBatch(List<TbDirectory> entities) {
        return this.tbDirectoryDao.insertOrUpdateBatch(entities);
    }

    /**
     * 修改数据
     *
     * @param tbDirectory 实例对象
     * @return 影响行数
     */
    public int update(TbDirectory tbDirectory) {
        return this.tbDirectoryDao.update(tbDirectory);
    }

    /**
     * 通过主键删除数据
     *
     * @param directoryId 主键
     * @return 影响行数
     */
    public int deleteById(Long directoryId) {
        return this.tbDirectoryDao.deleteById(directoryId);
    }
}
