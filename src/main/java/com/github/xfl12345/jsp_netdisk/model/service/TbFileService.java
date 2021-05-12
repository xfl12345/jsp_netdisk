package com.github.xfl12345.jsp_netdisk.model.service;

import com.alibaba.fastjson.JSONObject;
import com.github.xfl12345.jsp_netdisk.appconst.api.result.EmailVerificationApiResult;
import com.github.xfl12345.jsp_netdisk.appconst.api.result.FileApiResult;
import com.github.xfl12345.jsp_netdisk.appconst.field.EmailVerificationRequestField;
import com.github.xfl12345.jsp_netdisk.appconst.field.MySessionAttributes;
import com.github.xfl12345.jsp_netdisk.model.dao.TbFileDao;
import com.github.xfl12345.jsp_netdisk.model.pojo.database.TbAccount;
import com.github.xfl12345.jsp_netdisk.model.pojo.database.TbFile;
import com.github.xfl12345.jsp_netdisk.model.utils.JsonRequestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service("tbFileService")
public class TbFileService {

    private final Logger logger = LoggerFactory.getLogger(TbFileService.class);

    @Autowired
    private TbFileDao tbFileDao;

    @Autowired
    private TbDirectoryService tbDirectoryService;

    @Autowired
    private DirFileService dirFileService;

    public boolean isValidFileName(String fileName) {
        if (fileName == null || fileName.length() > 255)
            return false;
        else
            return fileName.matches("[^\\s\\\\/:\\*\\?\\\"<>\\|](\\x20|[^\\s\\\\/:\\*\\?\\\"<>\\|])*[^\\s\\\\/:\\*\\?\\\"<>\\|\\.]$");
    }


    public FileApiResult activeEmail(HttpServletRequest request) {
        TbAccount tbAccount = (TbAccount) request.getSession().getAttribute(MySessionAttributes.TB_ACCOUNT);
        //只有登录了才可以继续
        if (tbAccount == null) {
            return FileApiResult.FAILED_NO_LOGIN;
        }
        //格式化请求数据为JSON对象
        JSONObject requesetJsonObject = null;
        String code;
        try {
            requesetJsonObject = JsonRequestUtils.getJsonObject(request);
        } catch (Exception e) {
            return FileApiResult.FAILED_REQUEST_FORMAT_ERROR;
        }
        FileApiResult apiResult = FileApiResult.OTHER_FAILED;

        //TODO









        return apiResult;
    }



    /**
     * 通过实体作为筛选条件统计
     *
     * @param tbFile 实例对象
     * @return long型的总数
     */
    public long countAll(TbFile tbFile) {
        return tbFileDao.countAll(tbFile);
    }

    /**
     * 通过实体作为筛选条件查询
     *
     * @param tbFile 实例对象
     * @return 对象列表
     */
    public List<TbFile> queryAll(TbFile tbFile) {
        return tbFileDao.queryAll(tbFile);
    }

    /**
     * 通过ID查询单条数据
     *
     * @param fileId 主键
     * @return 实例对象
     */
    public TbFile queryById(Long fileId) {
        return tbFileDao.queryById(fileId);
    }

    /**
     * 查询指定行数据
     *
     * @param tbFile 实例对象
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    public List<TbFile> queryAllByLimit(TbFile tbFile, int offset, int limit) {
        return tbFileDao.queryAllByLimit(tbFile, offset, limit);
    }

    /**
     * 新增数据
     *
     * @param tbFile 实例对象
     * @return 影响行数
     */
    public int insert(TbFile tbFile) {
        return tbFileDao.insert(tbFile);
    }

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<TbFile> 实例对象列表
     * @return 影响行数
     */
    public int insertBatch(List<TbFile> entities) {
        return tbFileDao.insertBatch(entities);
    }

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<TbFile> 实例对象列表
     * @return 影响行数
     */
    public int insertOrUpdateBatch(List<TbFile> entities) {
        return tbFileDao.insertOrUpdateBatch(entities);
    }

    /**
     * 修改数据
     *
     * @param tbFile 实例对象
     * @return 影响行数
     */
    public int update(TbFile tbFile) {
        return tbFileDao.update(tbFile);
    }


    /**
     * 通过主键删除数据
     *
     * @param fileId 主键
     * @return 影响行数
     */
    public int deleteById(Long fileId) {
        return tbFileDao.deleteById(fileId);
    }

}
