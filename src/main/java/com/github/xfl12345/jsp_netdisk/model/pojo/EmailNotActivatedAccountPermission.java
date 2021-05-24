package com.github.xfl12345.jsp_netdisk.model.pojo;

import com.github.xfl12345.jsp_netdisk.appconst.field.TbPermissionField;
import com.github.xfl12345.jsp_netdisk.model.pojo.database.TbPermission;

public final class EmailNotActivatedAccountPermission extends TbPermission {

    public EmailNotActivatedAccountPermission() {
        super();
        super.setUploadFile(TbPermissionField.UPLOAD_FILE.NO_PERMISSION);
        super.setDownloadFile(TbPermissionField.DOWNLOAD_FILE.NO_PERMISSION);
        super.setAccountOperation(TbPermissionField.ACCOUNT_OPERATION.NO_PERMISSION);
        super.setAccountInfoOperation(TbPermissionField.ACCOUNT_INFO_OPERATION.SELF_ONLY);
        super.setFileOperation(TbPermissionField.generateFileOperationCode(
                TbPermissionField.FileOperationPermission.NO_PERMISSION,
                TbPermissionField.FileOperationPermission.NO_PERMISSION,
                TbPermissionField.FileOperationPermission.NO_PERMISSION));
    }
}
