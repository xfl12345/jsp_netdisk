package com.github.xfl12345.jsp_netdisk.model.service;

import com.alibaba.fastjson.JSONObject;
import com.github.xfl12345.jsp_netdisk.StaticSpringApp;
import com.github.xfl12345.jsp_netdisk.appconst.MyConst;
import com.github.xfl12345.jsp_netdisk.appconst.api.result.FileApiResult;
import com.github.xfl12345.jsp_netdisk.appconst.api.result.JsonApiResult;
import com.github.xfl12345.jsp_netdisk.appconst.field.*;
import com.github.xfl12345.jsp_netdisk.appconst.field.TbPermissionField.FileOperationPermission;
import com.github.xfl12345.jsp_netdisk.model.pojo.DownloadDetail;
import com.github.xfl12345.jsp_netdisk.model.pojo.api.request.BaseRequestObject;
import com.github.xfl12345.jsp_netdisk.model.pojo.api.request.payload.DirectoryOperationRequestData;
import com.github.xfl12345.jsp_netdisk.model.pojo.api.response.JsonCommonApiResponseObject;
import com.github.xfl12345.jsp_netdisk.model.pojo.database.*;
import com.github.xfl12345.jsp_netdisk.model.pojo.result.*;
import com.github.xfl12345.jsp_netdisk.model.service.api.JsonCommonApiCall;
import com.github.xfl12345.jsp_netdisk.model.utility.MyJsonUtils;
import com.github.xfl12345.jsp_netdisk.model.utility.MyStrIsOK;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.github.xfl12345.jsp_netdisk.StaticSpringApp.myConst;

@Service("FileService")
public class FileService implements JsonCommonApiCall {

    private final Logger logger = LoggerFactory.getLogger(FileService.class);

    @Autowired
    public TbFileService tbFileService;

    @Autowired
    private TbDirectoryService tbDirectoryService;

    @Autowired
    public DirFileService dirFileService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TbPermissionService tbPermissionService;

    public static final String jsonApiVersion = "1";

    /**
     * 文件操作权限字典，方便快速判断某个权限是否允许某个操作
     */
    public static final TreeMap<FileOperationPermission, TreeSet<String>> FileOperationPermissionDict = new TreeMap<>();

    /**
     * 属于文件操作的集合
     */
    public static final TreeSet<String> fileOperation = new TreeSet<>();
    /**
     * 属于目录操作的集合
     */
    public static final TreeSet<String> directoryOperation = new TreeSet<>();

    /**
     * 这是一个下载链接缓存Map，将 AccountId 作为键，目标文件 TbFile 作为值
     * 为了方便查询，同时为了支持高并发，将 目录 ID + '_' + 用户自定义文件名 作为 查找 TbFile 的键
     */
    public static final ConcurrentHashMap<String, ConcurrentHashMap<String, DownloadDetail>> downloadLinkMap = new ConcurrentHashMap<>();


//    private TreeSet<String> simpleStringTreeSetDeepCopy(TreeSet<String> treeSet){
//        TreeSet<String> copySet = new TreeSet<>();
//        copySet.addAll(treeSet);
//        return copySet;
//    }

    public FileService() {
        //初始化一个字典，方便快速查询
        //无任何权限，直接空集
        FileOperationPermissionDict.put(FileOperationPermission.NO_PERMISSION,new TreeSet<>());
        TreeSet<String> operationSet = null;
        //仅可见
        operationSet = new TreeSet<>();
        operationSet.add(FileOperationField.find);
        operationSet.add(FileOperationField.listFiles);
        FileOperationPermissionDict.put(FileOperationPermission.VIEW_ONLY, operationSet);
        //仅可读
        operationSet =  new TreeSet<>();
        operationSet.addAll(FileOperationPermissionDict.get(FileOperationPermission.VIEW_ONLY));
        operationSet.add(FileOperationField.download);
        FileOperationPermissionDict.put(FileOperationPermission.READ_ONLY, operationSet);
        //仅可写
        operationSet =  new TreeSet<>();
        operationSet.addAll(FileOperationPermissionDict.get(FileOperationPermission.VIEW_ONLY));
        operationSet.add(FileOperationField.makeDirectory);
        operationSet.add(FileOperationField.upload);
        FileOperationPermissionDict.put(FileOperationPermission.WRITE_ONLY, operationSet);
        //仅可更新
        operationSet =  new TreeSet<>();
        operationSet.addAll(FileOperationPermissionDict.get(FileOperationPermission.VIEW_ONLY));
        operationSet.add(FileOperationField.move);
        operationSet.add(FileOperationField.delete);
        operationSet.add(FileOperationField.changeName);
        FileOperationPermissionDict.put(FileOperationPermission.UPDATE_ONLY, operationSet);
        //可读 + 可写
        operationSet =  new TreeSet<>();
        operationSet.addAll(FileOperationPermissionDict.get(FileOperationPermission.READ_ONLY));
        operationSet.addAll(FileOperationPermissionDict.get(FileOperationPermission.WRITE_ONLY));
        FileOperationPermissionDict.put(FileOperationPermission.READ_AND_WRITE, operationSet);
        //可读 + 可写 + 可更新
        operationSet =  new TreeSet<>();
        operationSet.addAll(FileOperationPermissionDict.get(FileOperationPermission.READ_ONLY));
        operationSet.addAll(FileOperationPermissionDict.get(FileOperationPermission.WRITE_ONLY));
        operationSet.addAll(FileOperationPermissionDict.get(FileOperationPermission.UPDATE_ONLY));
        FileOperationPermissionDict.put(FileOperationPermission.ALLOW_EVERYTHING, operationSet);

        //初始化文件和目录 共有的操作 的集合
        fileOperation.add(FileOperationField.delete);
        fileOperation.add(FileOperationField.find);
        fileOperation.add(FileOperationField.changeName);
        fileOperation.add(FileOperationField.copy);
        fileOperation.add(FileOperationField.move);
        fileOperation.add(FileOperationField.upload);
        fileOperation.add(FileOperationField.download);

        //目录操作 copy 一下共有操作
        directoryOperation.addAll(fileOperation);
        directoryOperation.add(FileOperationField.makeDirectory);
        directoryOperation.add(FileOperationField.listFiles);
        directoryOperation.add(FileOperationField.changeDirectory);
        directoryOperation.add(FileOperationField.printWorkDirector);

        //文件专属操作
        fileOperation.add(FileOperationField.getMD5Hex);
        fileOperation.add(FileOperationField.getSHA256Hex);
    }

    public boolean isFileOperationAuthorized(FileOperationPermission permission, String operation){
        TreeSet<String> allowedOperation = FileOperationPermissionDict.get(permission);
        return allowedOperation.contains(operation);
    }

    /**
     * 列出目录下的子项目
     */
    public FileDirectoryView getFileDirectoryView(Long directoryId){
        //新建一个文件目录的视图
        FileDirectoryView view = new FileDirectoryView();
        DirectoryView directoryView = getDirectoryView(directoryId);
        //当前目录的信息
        view.currDirectory = directoryView.currDirectory;
        //子目录
        view.directories = directoryView.directories;
        //查询子文件
        List<UserFile> fileList =  dirFileService.queryAllFileInDir(directoryId);
        for(UserFile file: fileList) {
            view.files.put(file.getUserCustomFileName(), file);
        }
        return view;
    }

    /**
     * 判断一个 目录对象 是否满足 当前已登录的账号 的 操作
     */
    public boolean isAuthorized(HttpSession session, TbDirectory directory, String operation){
        //如果TbDirectory对象为空，直接返回false
        if(directory == null){
            return false;
        }
        //获取用户信息
        TbAccount tbAccount = (TbAccount) session.getAttribute(MySessionAttributes.TB_ACCOUNT);
        //获取文件操作权限信息
        FileOperationPermission[] permissions = getFileOperationElements(session);
        //该目录是属于该账号的
        if(tbAccount.getAccountId().equals(directory.getAccountId())) {
            return isFileOperationAuthorized(permissions[0], operation);
        }
        else return tbAccount.getAccountId().equals(TbAccountField.ACCOUNT_ID_ROOT);
        //不是超级用户又不是属于该账号的……暂时不实现跨账号访问，全部禁止
    }

    /**
     * 判断一个 目录文件关联对象 是否满足 当前已登录的账号 的 操作
     */
    public boolean isAuthorized(HttpSession session, DirFile dirFile, String operation){
        //如果TbFile对象为空，直接返回false
        if(dirFile == null ||
                dirFile.getAccountId() == null ||
                dirFile.getUserCustomFileName() == null ||
                dirFile.getFileId() == null ||
                dirFile.getDirectoryId() == null){
            return false;
        }
        //获取用户信息
        TbAccount tbAccount = (TbAccount) session.getAttribute(MySessionAttributes.TB_ACCOUNT);
        //获取文件操作权限信息
        FileOperationPermission[] permissions = getFileOperationElements(session);
        //查询目录文件关联对象所在目录
        List<DirFile> files = dirFileService.queryAll(dirFile);
        //如果文件所在目录无法被找到，或者不能唯一指定（攻击），直接返回false
        if(files.size() != 1)
            return false;
        //该文件是属于该账号的
        if(tbAccount.getAccountId().equals(files.get(0).getAccountId())) {
            return isFileOperationAuthorized(permissions[0], operation);
        }
        else return tbAccount.getAccountId().equals(TbAccountField.ACCOUNT_ID_ROOT);
        //不是超级用户又不是属于该账号的……暂时不实现跨账号访问，全部禁止
    }

    public FileOperationPermission[] getFileOperationElements(HttpSession session){
        //获取用户信息
        TbAccount tbAccount = (TbAccount) session.getAttribute(MySessionAttributes.TB_ACCOUNT);
        //尝试获取缓存在session里的权限信息
        TbPermission tbPermission = (TbPermission) session.getAttribute(MySessionAttributes.TB_PERMISSION);
        //核对是否是这个用户的权限信息（可能后台管理员更新修改了这个用户的权限）
        if(tbPermission == null ||  ! tbPermission.getPermissionId().equals(tbAccount.getPermissionId())) {
            tbPermission = tbPermissionService.queryById(tbAccount.getPermissionId());
            if(tbPermission == null)
                tbPermission = tbPermissionService.getEmailNotActivatedPermission();
            //更新session缓存
            session.setAttribute(MySessionAttributes.TB_PERMISSION, tbPermission);
            tbPermission = (TbPermission) session.getAttribute(MySessionAttributes.TB_PERMISSION);
        }
        return TbPermissionField.formatFileOperationCode(tbPermission.getFileOperation());
    }

    public void setJsonApiResult(JsonCommonApiResponseObject responseObject, FileApiResult apiResult){
        responseObject.success = apiResult.equals(FileApiResult.SUCCEED);
        responseObject.code = apiResult.getNum();
        responseObject.message = apiResult.getName();
    }

    /**
     * 检查完JSON数据结构是否合法之后，调用具体的业务函数
     */
    @Override
    public JsonCommonApiResponseObject getResult(HttpServletRequest request, JSONObject object) {
        JsonCommonApiResponseObject responseObject = new JsonCommonApiResponseObject(jsonApiVersion);
        BaseRequestObject baseRequestObject = object.toJavaObject(BaseRequestObject.class);
        try {
            //兼容旧API版本的请求
            switch (baseRequestObject.version) {
                //当前版本（最新版本）
                case jsonApiVersion:
                    //先检查登录了没有
                    HttpSession session = request.getSession();
                    if( ! accountService.checkIsLoggedIn(session)  ){
                        setJsonApiResult(responseObject, FileApiResult.FAILED_NO_LOGIN);
                        break;
                    }


                    //删除
                    else if(baseRequestObject.operation.equals(FileOperationField.delete)){
                        FileDirOperationResult operationResult = new FileDirOperationResult();

                        FileDirectoryView fileDirectoryView = baseRequestObject.data.toJavaObject(FileDirectoryView.class);
                        String operation = baseRequestObject.operation;

                        for(TbDirectory currDir : fileDirectoryView.directories.values()){
                            //优先判断有无权限执行文件操作
                            if(isAuthorized(session, currDir, operation) ){
                                try {
                                    if (tbDirectoryService.deleteById(currDir.getDirectoryId()) == 1) {
                                        operationResult.directorySuccessCount++;
                                        DirOperationResult result = new DirOperationResult();
                                        result.setFileApiResult(FileApiResult.SUCCEED);
                                        result.directory = currDir;
                                        operationResult.directories.put(currDir.getDirectoryName(), result);
                                    }
                                    else {
                                        operationResult.directoryFailCount ++;
                                        DirOperationResult result = new DirOperationResult();
                                        result.setFileApiResult(FileApiResult.FAILED_NOT_FOUND);
                                        result.directory = currDir;
                                        operationResult.directories.put(currDir.getDirectoryName(), result);
                                    }
                                }
                                catch (Exception e){
                                    logger.error(e.toString());
                                    operationResult.directoryFailCount ++;
                                    DirOperationResult result = new DirOperationResult();
                                    result.setFileApiResult(FileApiResult.OTHER_FAILED);
                                    result.directory = currDir;
                                    operationResult.directories.put(currDir.getDirectoryName(), result);
                                }
                            }
                            else {
                                operationResult.directoryFailCount ++;
                                DirOperationResult result = new DirOperationResult();
                                result.setFileApiResult(FileApiResult.FAILED_PERMISSION_DENIED);
                                result.directory = currDir;
                                operationResult.directories.put(currDir.getDirectoryName(), result);
                            }
                        }

                        for(UserFile currFile : fileDirectoryView.files.values()){
                            DirFile currDirFile = getDirFile(currFile);
                            //优先判断有无权限执行文件操作
                            if(isAuthorized(session, currDirFile, operation) ){
                                try {
                                    if (dirFileService.deleteAll(currDirFile) == 1) {
                                        operationResult.fileSuccessCount++;
                                        UserFileOperationResult result = new UserFileOperationResult();
                                        result.setFileApiResult(FileApiResult.SUCCEED);
                                        result.file = currDirFile;
                                        operationResult.files.put(currFile.getUserCustomFileName(), result);
                                    }
                                    else {
                                        operationResult.fileFailCount ++;
                                        UserFileOperationResult result = new UserFileOperationResult();
                                        result.setFileApiResult(FileApiResult.FAILED_NOT_FOUND);
                                        result.file = currDirFile;
                                        operationResult.files.put(currFile.getUserCustomFileName(), result);
                                    }
                                }
                                catch (Exception e){
                                    logger.error(e.toString());
                                    operationResult.fileFailCount ++;
                                    UserFileOperationResult result = new UserFileOperationResult();
                                    result.setFileApiResult(FileApiResult.OTHER_FAILED);
                                    result.file = currDirFile;
                                    operationResult.files.put(currFile.getUserCustomFileName(), result);
                                }
                            }
                            else {
                                operationResult.fileFailCount ++;
                                UserFileOperationResult result = new UserFileOperationResult();
                                result.setFileApiResult(FileApiResult.FAILED_PERMISSION_DENIED);
                                result.file = currDirFile;
                                operationResult.files.put(currFile.getUserCustomFileName(), result);
                            }
                        }
                        //当且仅当全部操作成功，才设置结果 SUCCESS 为 true
                        if( operationResult.fileFailCount == 0L && operationResult.directoryFailCount == 0L ){
                            setJsonApiResult(responseObject, FileApiResult.SUCCEED);
                        }
                        else {
                            setJsonApiResult(responseObject, FileApiResult.FAILED);
                        }
                        responseObject.data = MyJsonUtils.javaObject2jsonObject(operationResult);
                        break;
                    }
                    //查找
                    else if(baseRequestObject.operation.equals(FileOperationField.find)){
                        //TODO
                        break;
                    }
                    //修改名称
                    else if(baseRequestObject.operation.equals(FileOperationField.changeName)){
                        //TODO
                        break;
                    }
                    //复制
                    else if(baseRequestObject.operation.equals(FileOperationField.copy)){
                        //TODO
                        break;
                    }
                    //移动
                    else if(baseRequestObject.operation.equals(FileOperationField.move)){
                        //TODO
                        break;
                    }
                    //下载
                    else if(baseRequestObject.operation.equals(FileOperationField.download)){
                        download(responseObject, baseRequestObject, session);
                        break;
                    }
                    //列出文件（相当于Linux ls命令）
                    else if(baseRequestObject.operation.equals(FileOperationField.listFiles)){
                        DirectoryOperationResult operationResult = lsOrMkdir(baseRequestObject, session);
                        //转换数据为JSON通用格式
                        setJsonApiResult(responseObject, operationResult.apiResult);
                        //转换数据为JSON对象
                        responseObject.data = MyJsonUtils.javaObject2jsonObject(operationResult.currFileDirView);
                        break;
                    }
                    //切换工作目录（相当于Linux cd命令）
                    else if(baseRequestObject.operation.equals(FileOperationField.changeDirectory)){
                        changeDirectory(responseObject, baseRequestObject, session);
                        break;
                    }
                    //切换工作目录（相当于Linux pwd命令）
                    else if(baseRequestObject.operation.equals(FileOperationField.printWorkDirector)){
                        printWorkDirector(responseObject, baseRequestObject, session);
                        break;
                    }
                    //新建目录（相当于Linux mkdir命令）
                    else if(baseRequestObject.operation.equals(FileOperationField.makeDirectory)){
                        DirectoryOperationResult operationResult = lsOrMkdir(baseRequestObject, session);
                        //转换数据为JSON通用格式
                        setJsonApiResult(responseObject, operationResult.apiResult);
                        //转换数据为JSON对象
                        responseObject.data = MyJsonUtils.javaObject2jsonObject(operationResult.currFileDirView);
                        break;
                    }
                    //计算文件哈希值
                    else if(baseRequestObject.operation.equals(FileOperationField.getMD5Hex) ||
                            baseRequestObject.operation.equals(FileOperationField.getSHA256Hex)) {
                        //TODO
                        break;
                    }


                    break;
                default:
                    responseObject.setApiResult(JsonApiResult.FAILED_INVALID);
                    break;
            }
        } catch (Exception e) {
            logger.error(e.toString());
            responseObject.setApiResult(JsonApiResult.FAILED_INVALID);
        }
        return responseObject;
    }



    public void printWorkDirector(JsonCommonApiResponseObject responseObject,
                                BaseRequestObject baseRequestObject,
                                HttpSession session){
        DirectoryView pwd = (DirectoryView) session.getAttribute(MySessionAttributes.CURRENT_WORK_DIRECTOR);
        JSONObject dataObject = new JSONObject();
        dataObject.put(FileOperationField.printWorkDirector, pwd);
        baseRequestObject.data = dataObject;
        if( pwd == null){
            responseObject.setApiResult(JsonApiResult.OTHER_FAILED);
        }
        else {
            responseObject.setApiResult(JsonApiResult.SUCCEED);
        }
    }

    public void changeDirectory(JsonCommonApiResponseObject responseObject,
                          BaseRequestObject baseRequestObject,
                          HttpSession session){
        baseRequestObject.operation = FileOperationField.listFiles;
        DirectoryOperationResult operationResult = lsOrMkdir(baseRequestObject, session);
        setJsonApiResult(responseObject, operationResult.apiResult);

        DirectoryView cwd = null;
        FileDirectoryView fileDirectoryView = operationResult.currFileDirView;
        //如果ls成功了，直接cd到这个目录
        if(responseObject.success){
            cwd = fileDirectoryView;
        }
        else {//如果ls失败，直接cd到默认的用户根目录
            TbAccount tbAccount = (TbAccount) session.getAttribute(MySessionAttributes.TB_ACCOUNT);
            cwd = getDirectoryView(tbAccount.getRootDirectoryId());
            session.setAttribute(MySessionAttributes.CURRENT_WORK_DIRECTOR, cwd);
        }
        session.setAttribute(MySessionAttributes.CURRENT_WORK_DIRECTOR, cwd);
        responseObject.data = MyJsonUtils.javaObject2jsonObject(fileDirectoryView);
    }

    public DirectoryOperationResult lsOrMkdir(
                          BaseRequestObject baseRequestObject,
                          HttpSession session){
        DirectoryOperationResult operationResult = new DirectoryOperationResult();
        DirectoryOperationRequestData data = null;
        try {
             data = baseRequestObject.data.toJavaObject(DirectoryOperationRequestData.class);
        }
        catch (Exception e){
            operationResult.setApiResult(FileApiResult.FAILED_REQUEST_FORMAT_ERROR);
            return operationResult;
        }
        //TODO 如果请求是 ls ./*
        //优先检查数据
        if(data == null || MyStrIsOK.isEmpty(data.path) ||
                data.path.charAt(0) != '/' ){
            operationResult.setApiResult(FileApiResult.FAILED_REQUEST_FORMAT_ERROR);
            return operationResult;
        }
        //获取登录信息
        TbAccount tbAccount = (TbAccount) session.getAttribute(MySessionAttributes.TB_ACCOUNT);
        //获取用户根目录
        TbDirectory currDir = tbDirectoryService.queryById(tbAccount.getRootDirectoryId());
        //优先判断有无权限执行文件操作
        if( ! isAuthorized(session, currDir, baseRequestObject.operation) ){
            operationResult.setApiResult(FileApiResult.FAILED_PERMISSION_DENIED);
            return operationResult;
        }
        //执行操作
        operationResult = lsOrMkdir(data.path, baseRequestObject.operation, session);
        return operationResult;
    }

    public DirectoryOperationResult lsOrMkdir(String path, String operation, HttpSession session){
        DirectoryOperationResult directoryOperationResult = new DirectoryOperationResult();
        //获取登录信息
        TbAccount tbAccount = (TbAccount) session.getAttribute(MySessionAttributes.TB_ACCOUNT);
        //获取用户根目录
        TbDirectory currDir = tbDirectoryService.queryById(tbAccount.getRootDirectoryId());
        //优先判断有无权限执行文件操作
        if( ! isAuthorized(session, currDir, operation) ){
            directoryOperationResult.setApiResult(FileApiResult.FAILED_PERMISSION_DENIED);
            return directoryOperationResult;
        }
        //解析路径
        String[] subDirNameList = StringUtils.split(path, "/");
        //TODO 如果请求是 ls ./*
        //获取用户根目录视图
        DirectoryView currDirView = getDirectoryView(session, currDir);
        //预设子目录视图
        DirectoryView nextDirView;
        directoryOperationResult.setApiResult(FileApiResult.SUCCEED);
        //当前路径
        String cwd;
        //路径迭代，根目录本来就没有名字
        if(subDirNameList.length == 0) {
            cwd = "/";
        }
        else {
            cwd = "";
        }
        //迭代式打开目录
        for (String nextDirName : subDirNameList) {

            //备份当前路径
            String oldPath = cwd;

            //获取请求路径上的子目录
            TbDirectory nextDir = currDirView.directories.get(nextDirName);

            //是否执行 ls 功能
            if(operation.equals(FileOperationField.listFiles)) {
                //如果子目录不存在，说明路径不存在，直接停止迭代 返回 Not Found
                if(nextDir == null){
                    currDirView = null;
                    break;
                }
                //判断是否有权访问
                if( isAuthorized(session, nextDir, operation) ){
                    //进入子目录
                    nextDirView = getDirectoryView(session, nextDir.getDirectoryId());
                    //如果为空，说明路径不存在（可能数据库里临时被删了）
                    if (nextDirView == null) {
                        break;
                    }
                    currDirView = nextDirView;
                }
                else {
                    directoryOperationResult.setApiResult(FileApiResult.FAILED_PERMISSION_DENIED);
                    break;
                }
            }
            //是否执行 mkdir 功能
            else if(operation.equals(FileOperationField.makeDirectory)) {
                //如果子目录不存在，直接 创建一个新的目录
                if(nextDir == null){
                    TbDirectory dir = new TbDirectory();
                    dir.setParentDirectoryId(currDirView.currDirectory.getDirectoryId());
                    dir.setDirectoryName(nextDirName);
                    dir.setAccountId(tbAccount.getAccountId());
                    tbDirectoryService.insert(dir);
                    currDirView = getDirectoryView(session, tbDirectoryService.getId(dir));
                }
                //如果子目录存在
                else {
                    //优先判断是否有权访问
                    if( isAuthorized(session, nextDir, operation) ) {
                        //尝试进入子目录
                        nextDirView = getDirectoryView(session, nextDir.getDirectoryId());
                        //如果为空，说明路径不存在（可能数据库里临时被删了）
                        if (nextDirView == null) {
                            TbDirectory dir = new TbDirectory();
                            dir.setParentDirectoryId(currDirView.currDirectory.getDirectoryId());
                            dir.setDirectoryName(nextDirName);
                            dir.setAccountId(tbAccount.getAccountId());
                            tbDirectoryService.insert(dir);
                            nextDirView = getDirectoryView(session, tbDirectoryService.getId(dir));
                        }
                        currDirView = nextDirView;
                    }
                }
            }

            //路径迭代
            cwd= oldPath + "/" + nextDirName;
        }
        //最终迭代，如果找不到路径，currDirView 为 null
        if(currDirView == null){
            directoryOperationResult.setApiResult(FileApiResult.FAILED_NOT_FOUND);
        }
        else {
            currDirView.printWorkDirector = cwd;
            directoryOperationResult.currFileDirView.setDirectoryView(currDirView);
            List<UserFile> files = dirFileService.queryAllFileInDir(currDirView.currDirectory.getDirectoryId());
            for(UserFile fileItem : files){
                directoryOperationResult.currFileDirView.files.put(fileItem.getUserCustomFileName(), fileItem);
            }
        }

        return directoryOperationResult;
    }

    /**
     * 获取网盘存储文件的根目录路径
     * @return 网盘存储文件的根目录路径
     */
    public String getFileStorageRoot(){
        return StaticSpringApp.getBean(MyConst.class).getNetdiskStoragePath();
    }

    /**
     * 获取新文件的可用文件名，如果预设文件名与现有文件重名，则使用UUID作为文件名
     * 并抢先创建文件
     * @param filename 预设文件名
     * @return 可用文件名
     */
    public synchronized String getAvailableFileName(String filename) throws IOException {
        String rootPath = getFileStorageRoot() + "\\" ;
        String filepath = rootPath + filename;
        File file = new File(filepath);
        if(file.exists()){
            filename = StaticSpringApp.getUUID();
            filepath = rootPath + filename;
            file = new File(filepath);
        }
        file.createNewFile();
        return filename;
    }

    /**
     * 写入文件到指定路径的关键函数，干实事的好家伙
     * @param uploadFile 从前端上传过来的文件对象（MultipartFile类）
     * @return 操作是否成功
     */
    public boolean writeFile(MultipartFile uploadFile, String filePath) {
        try {
            InputStream input = uploadFile.getInputStream();
            File file = new File(filePath);
            OutputStream outputStream = new FileOutputStream(file);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream, (1 << 20));//1MiB 内存空间做缓冲
            byte[] tmpReadBuffer = new byte[1024];
            int currReadByteCount = 0;
            while ((currReadByteCount = input.read(tmpReadBuffer, 0, tmpReadBuffer.length)) != -1) {
                bufferedOutputStream.write(tmpReadBuffer, 0, currReadByteCount);
            }
            bufferedOutputStream.close();
            outputStream.close();
            input.close();
        }
        catch (IOException exception){
            logger.error(exception.toString());
            return false;
        }
        return true;
    }

    public String getFileSHA256Hex(File file){
        String result = null;
        try {
            InputStream fileInputStream = new FileInputStream(file);
            result = DigestUtils.sha256Hex(fileInputStream);
        }
        catch (IOException exception){
            logger.info(exception.toString());
        }
        return result;
    }

    public String getFileMD5Hex(File file){
        String result = null;
        try {
            InputStream fileInputStream = new FileInputStream(file);
            result = DigestUtils.md5Hex(fileInputStream);
        }
        catch (IOException exception){
            logger.info(exception.toString());
        }
        return result;
    }

    public String getFilePathInDisk(String filename){
        return getFileStorageRoot() + "\\" + filename;
    }

    public FileApiResult upload(MultipartFile uploadFile, HttpSession session){
        //首先检查对象是否为空
        if(uploadFile == null){
            return FileApiResult.FAILED_REQUEST_FORMAT_ERROR;
        }
        DirectoryView cwdView = (DirectoryView) session.getAttribute(MySessionAttributes.CURRENT_WORK_DIRECTOR);
        TbDirectory currDirectory = cwdView.currDirectory;
        //优先检查用户自定义名称是否合法
        if( ! MyStrIsOK.isValidFileName(uploadFile.getOriginalFilename())){
            return FileApiResult.FAILED_NAME_ILLEGAL;
        }
        //优先检查同一个目录下是否有重复的文件名
        DirFile querySameNameFileInDir = new DirFile();
        querySameNameFileInDir.setUserCustomFileName(uploadFile.getOriginalFilename());
        querySameNameFileInDir.setDirectoryId(currDirectory.getDirectoryId());
        //不允许出现同一个文件夹之内有重名文件
        if( dirFileService.queryAll(querySameNameFileInDir).size() != 0){
            return FileApiResult.FAILED_DUPLICATE_NAME;
        }
        FileApiResult fileApiResult = FileApiResult.OTHER_FAILED;
        TbAccount tbAccount = (TbAccount) session.getAttribute(MySessionAttributes.TB_ACCOUNT);
        //检查权限
        if(isAuthorized(session, currDirectory, FileOperationField.upload)){
            //获取本地可用文件名
            String outputFilename;
            try {
                outputFilename =  getAvailableFileName(uploadFile.getOriginalFilename());
            } catch (IOException exception) {
                logger.error(exception.toString());
                return fileApiResult;
            }
            String filepath = getFilePathInDisk(outputFilename);
            File file = new File(filepath);
            //先斩后奏，先写到本地再插数据库，写完了再计算HASH值
            if(writeFile(uploadFile, filepath)){
                try {
                    //新插入一行文件数据（初始化部分数据）
                    TbFile insertNewFile = new TbFile();
                    insertNewFile.setFileSize(file.length());
                    //计算SHA256哈希值
                    InputStream inputStream4sha256 = new FileInputStream(file);
                    String sha256HexStr = DigestUtils.sha256Hex(inputStream4sha256);
                    inputStream4sha256.close();
                    //设置新插入数据库的文件的SHA256值
                    insertNewFile.setFileHashSha256(sha256HexStr);
                    //计算文件MD5哈希值
                    InputStream inputStream4md5 = new FileInputStream(file);
                    String md5HexStr = DigestUtils.md5Hex(inputStream4md5);
                    inputStream4md5.close();
                    //设置新插入数据库的文件的MD5值
                    insertNewFile.setFileHashMd5(md5HexStr);
                    //查询数据库是否存在 file_size + MD5 + SHA256 匹配的文件
                    List<TbFile> matchedFileList = tbFileService.queryAll(insertNewFile);
                    TbFile fileInDb = null;
                    //新文件！执行记录工作。
                    if(matchedFileList.size() == 0){
                        insertNewFile.setFileName(outputFilename);
                        insertNewFile.setFileStatus(TbFileField.FILE_STATUS.DEFAULT);
                        insertNewFile.setFileType(TbFileField.FILE_TYPE.DEFAULT);
                        int affectedRowCount = 0;
                        insertNewFile.setFileUploadTime(new Date());
                        try {
                            affectedRowCount = tbFileService.insert(insertNewFile);
                        }
                        catch (DataAccessException e) {
                            Throwable cause = e.getCause();
                            if (cause instanceof MySQLIntegrityConstraintViolationException) {
                                logger.warn("插入文件数据时，发生了瞬时碰撞。操作失败的账号ID：" + tbAccount.getAccountId()
                                        + ",file_id=" + insertNewFile.getFileId()
                                        + ",file_name=" + insertNewFile.getFileName()
                                        + ",file_size=" + insertNewFile.getFileSize()
                                        + ",file_hash_md5=" + insertNewFile.getFileHashMd5()
                                        + ",file_hash_sha256=" + insertNewFile.getFileHashSha256()
                                        + ",error=" + e);
                                boolean isFileDeleted = file.delete();
                                if( ! isFileDeleted){
                                    logger.error("删除文件 " + file.getCanonicalPath() + " 失败！请检查并手动删除！");
                                }
                            }
                            else {
                                logger.error(e.toString());
                            }
                        }
                    }
                    //如果 文件大小、MD5 和 SHA256都匹配上了，认定为同一个文件，不需要重复记录，且物理文件只保留一个副本
                    else {
                        boolean isFileDeleted = file.delete();
                        if( ! isFileDeleted){
                            logger.error("删除文件 " + file.getCanonicalPath() + " 失败！请检查并手动删除！");
                        }
                    }
                    //获取 文件ID
                    try{
                        fileInDb  = tbFileService.queryByMD5andSHA256(insertNewFile);
                    }
                    catch (Exception e){
                        logger.error("查询文件发生了严重的异常错误！操作失败的账号ID：" + tbAccount.getAccountId()
                                + ",file_id=" + insertNewFile.getFileId()
                                + ",file_name=" + insertNewFile.getFileName()
                                + ",file_size=" + insertNewFile.getFileSize()
                                + ",file_hash_md5=" + insertNewFile.getFileHashMd5()
                                + ",file_hash_sha256=" + insertNewFile.getFileHashSha256()
                                + ",error=" + e);
                    }
                    //执行用户文件映射绑定
                    if(fileInDb != null){
                        DirFile dirFile = new DirFile();
                        dirFile.setFileId(fileInDb.getFileId());
                        dirFile.setAccountId(tbAccount.getAccountId());
                        dirFile.setDirectoryId(currDirectory.getDirectoryId());
                        dirFile.setUserCustomFileName(uploadFile.getOriginalFilename());
                        try {
                            dirFileService.insert(dirFile);
                        }
                        catch (DataAccessException e) {
                            Throwable cause = e.getCause();
                            if (cause instanceof MySQLIntegrityConstraintViolationException) {
                                fileApiResult = FileApiResult.FAILED_DUPLICATE_NAME;
                                return fileApiResult;
                            }
                        }
                        fileApiResult = FileApiResult.SUCCEED;
                    }
                } catch (IOException exception) {
                    logger.error(exception.toString());
                    return fileApiResult;
                }
            }
            else {
                boolean isFileDeleted = false;
                try {
                    isFileDeleted = file.delete();
                } catch (Exception e){
                    logger.error("删除文件 " + filepath + " 发生异常！error=" + e);
                }
                if( ! isFileDeleted){
                    logger.error("删除文件 " + filepath + " 失败！请检查并手动删除！");
                }
                return fileApiResult;
            }

        }


        return fileApiResult;
    }

    public void download(JsonCommonApiResponseObject responseObject,
                         BaseRequestObject baseRequestObject,
                         HttpSession session){
        DirFile data = null;
        //检查格式
        try {
            data = baseRequestObject.data.toJavaObject(DirFile.class);
        }
        catch (Exception e){
            setJsonApiResult(responseObject, FileApiResult.FAILED_REQUEST_FORMAT_ERROR);
            return;
        }
        //检查数据完整
        if(data == null ||
                data.getAccountId() == null ||
                data.getUserCustomFileName() == null ||
                data.getFileId() == null ||
                data.getDirectoryId() == null){
            setJsonApiResult(responseObject, FileApiResult.FAILED_REQUEST_FORMAT_ERROR);
            return;
        }
        //检查是否记录是否存在（过滤非法请求）
        long count = dirFileService.countAll(data);
        if(count == 0){
            setJsonApiResult(responseObject, FileApiResult.FAILED_NOT_FOUND);
            return;
        }

        String accountIdStr = MyConst.ANONYMOUS;

        TbAccount tbAccount = (TbAccount) session.getAttribute(MySessionAttributes.TB_ACCOUNT);
        if( tbAccount != null ){
            accountIdStr = tbAccount.getAccountId().toString();
        }

        responseObject.data = new JSONObject();
        String uri = getDownloadLink( accountIdStr, data );
        if(uri.equals(MyConst.ABOUT_BLANK_URL)){
            setJsonApiResult(responseObject, FileApiResult.FAILED_NOT_FOUND);
            return;
        }
        responseObject.data.put("uri",  uri);
        setJsonApiResult(responseObject, FileApiResult.SUCCEED);
    }

    public String getDownloadDetailKey(String directoryIdStr, String name){
        return directoryIdStr + '_' + name;
    }

    public String getDownloadLink(String accountIdStr, DirFile dirFile){
        ConcurrentHashMap<String , DownloadDetail> downloadDetailMap;
        downloadDetailMap = downloadLinkMap.get(accountIdStr);
        //解决高并发读写时，发生脏写。为防止数据被覆盖的情况出现，
        // 使用 ConcurrentHashMap 的 putIfAbsent 方法进行实例化
        if(downloadDetailMap == null){
            downloadDetailMap = downloadLinkMap.putIfAbsent(accountIdStr, new ConcurrentHashMap<>());
            if(downloadDetailMap == null){
                downloadDetailMap = downloadLinkMap.get(accountIdStr);
            }
        }

        //构建下载详情的键值
        String downloadDetailKey = getDownloadDetailKey( dirFile.getDirectoryId().toString(),
                dirFile.getUserCustomFileName());

        DownloadDetail downloadDetail = downloadDetailMap.get(downloadDetailKey);
        //解决高并发读写时，发生脏写。为防止数据被覆盖的情况出现，
        // 使用 ConcurrentHashMap 的 putIfAbsent 方法进行实例化
        if(downloadDetail == null){
            downloadDetail = downloadDetailMap.putIfAbsent(downloadDetailKey, new DownloadDetail());
            if(downloadDetail == null){
                downloadDetail = downloadDetailMap.get(downloadDetailKey);
            }
        }

        //首次下载
        if(downloadDetail.tbFile == null){
            downloadDetail.tbFile = tbFileService.queryById(dirFile.getFileId());
            //如果数据库里临时删除了文件记录
            if(downloadDetail.tbFile == null){
                //返还空白页URL，这个文件无法访问
                return MyConst.ABOUT_BLANK_URL;
            }
            downloadDetail.linkCreateDate = new Date();
            String filePath = getFilePathInDisk( downloadDetail.tbFile.getFileName() );
            downloadDetail.fileInDisk = new File(filePath);
        }
        String uri = dirFile.getDirectoryId()
                + "/" + dirFile.getUserCustomFileName();
        return uri;
    }



    /**
     * 给定一个session对象和一个目录ID，返回内存缓存中的  DirectoryView 对象缓存
     * （暂不实现缓存功能）
     * @param directoryId 目录ID
     */
    public DirectoryView getDirectoryView(HttpSession session, Long directoryId){
        return getDirectoryView(directoryId);
    }

    /**
     * 给定一个session对象和一个TbDirectory对象，返回内存缓存中的  DirectoryView 对象缓存
     * （暂不实现缓存功能）
     * @param directory 目录ID
     */
    public DirectoryView getDirectoryView(HttpSession session, TbDirectory directory){
        return getDirectoryView(directory);
    }

    /**
     * 给定一个目录ID，查询数据库，生成 DirectoryView 对象
     * @param directoryId 目录ID
     */
    public DirectoryView getDirectoryView(Long directoryId){
        return getDirectoryView(tbDirectoryService.queryById(directoryId));
    }

    /**
     * 给定一个目录对象，查询数据库，生成 DirectoryView 对象
     * @param directory TbDirectory目录对象实体类
     */
    public DirectoryView getDirectoryView(TbDirectory directory){
        if(directory == null)
            return null;
        DirectoryView view = new DirectoryView();
        //获取当前目录的信息
        view.currDirectory = directory;
        //查询子目录
        TbDirectory querySubDir = new TbDirectory();
        querySubDir.setParentDirectoryId(directory.getDirectoryId());
        List<TbDirectory> dirList =  tbDirectoryService.queryAll(querySubDir);
        for(TbDirectory dir: dirList) {
            view.directories.put(dir.getDirectoryName(), dir);
        }
        return view;
    }


    public DirFile getDirFile(UserFile userFile){
        DirFile dirFile = new DirFile();
        dirFile.setFileId(userFile.getFileId());
        dirFile.setUserCustomFileName(userFile.getUserCustomFileName());
        dirFile.setDirectoryId(userFile.getDirectoryId());
        dirFile.setAccountId(userFile.getAccountId());
        return dirFile;
    }

}
