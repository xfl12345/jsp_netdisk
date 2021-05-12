package com.github.xfl12345.jsp_netdisk.model.pojo;

import com.github.xfl12345.jsp_netdisk.appconst.field.TbPermissionField;
import com.github.xfl12345.jsp_netdisk.model.pojo.database.TbPermission;

public final class NormalAccountPermission extends TbPermission {

    public NormalAccountPermission() {
        super();
        super.setUploadFile(TbPermissionField.UPLOAD_FILE.SELF_ONLY);
        super.setDownloadFile(TbPermissionField.DOWNLOAD_FILE.SELF_ONLY);
        super.setAccountOperation(TbPermissionField.ACCOUNT_OPERATION.NORMAL_USER);
        super.setAccountInfoOperation(TbPermissionField.ACCOUNT_INFO_OPERATION.SELF_ONLY);
        super.setFileOperation(TbPermissionField.generateFileOperationCode(
                TbPermissionField.FileOperationElement.ALLOW_EVERYTHING,
                TbPermissionField.FileOperationElement.NO_PERMISSION,
                TbPermissionField.FileOperationElement.NO_PERMISSION));
    }
}
