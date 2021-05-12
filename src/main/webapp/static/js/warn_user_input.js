var warmingDivCount = 0;
var secondPasswordConfirm = null;
var passwordTypeCount = 0;

function xzzsInit(limitTargetElementList) {
    let EleId;
    for (let i = 0, tmpvar; i < limitTargetElementList.length; i++) {
        //let maxLen=limitTarget[i].getAttribute("maxlength");
        tmpvar = i + 1;
        EleId = limitTargetElementList[i].getAttribute("id");

        if (EleId == null) {
            limitTargetElementList[i].setAttribute("id", "inputNo_" + tmpvar);
            EleId = "inputNo_" + tmpvar;
        }
        console.log("process:" + EleId);
        //alert(limitTarget[i]);
        let parentOfinputE = xzzsParentNode(limitTargetElementList[i]);
        if (!parentOfinputE.getAttribute("id"))
            parentOfinputE.setAttribute("id", EleId + "_" + tmpvar);
        if (  (limitTargetElementList[i].getAttribute("type") === "text" ||
            limitTargetElementList[i].getAttribute("type") === "password")  ) {
            if (limitTargetElementList[i].getAttribute("type") === "password") {
                passwordTypeCount++;
                if (passwordTypeCount === 2) {
                    secondPasswordConfirm = limitTargetElementList[i].getAttribute("id");
                }
            }
            limitTargetElementList[i].addEventListener('focus', xzzsCreate.bind(this, EleId));
            limitTargetElementList[i].addEventListener('blur', xzzsRemove.bind(this, EleId));
            limitTargetElementList[i].addEventListener('keyup', xzzs.bind(this, EleId));
            /*
            limitTarget[i].addEventListener('focus',abc.bind(this,limitTarget[i].getAttribute("id")+" is focus."));
            limitTarget[i].addEventListener('blur',abc.bind(this,limitTarget[i].getAttribute("id")+" is blur."));
            */
            console.log("processed:" + EleId);
        }

        /*
        limitTarget[i].setAttribute("onfocus","xzzsCreate('"+EleId+"')");
        limitTarget[i].setAttribute("onblur","xzzsRemove('"+EleId+"')");
        limitTarget[i].setAttribute("onkeyup","xzzs('"+EleId+"')");
        */
    }
}

function xzzsCreate(inputElementId) {
    let idOfMarkedWords = inputElementId + 'xz';
    let markedWords;
    if (document.getElementById(idOfMarkedWords)) {
        return;
    }
    let aim = xzzsParentNode(document.getElementById(inputElementId));
    console.log("xzzsCreate(" + inputElementId + ");Running!");
    if (aim.tagName.toUpperCase() === "TABLE") {
        let trEle = aim.getElementsByTagName("tr");
        let childTdCountMax = 0;
        for (let i = 0, tmpCount; i < trEle.length; i++) {
            tmpCount = trEle[i].getElementsByTagName("td").length;
            if (tmpCount > childTdCountMax) {
                childTdCountMax = tmpCount;
            }
        }
        let inputEleTrNode = document.getElementById(inputElementId).parentNode;
        for (; inputEleTrNode.tagName.toUpperCase() !== "TR"; inputEleTrNode = inputEleTrNode.parentNode) ;
        markedWords = document.createElement("td");
        markedWords.setAttribute("id", idOfMarkedWords);
        markedWords.colSpan = childTdCountMax;
        markedWords.style.display = "table-cell";
        markedWords.classList.add("jsInsert");
        let markedWordsParent = document.createElement("tr");
        markedWordsParent.setAttribute("id", idOfMarkedWords + "P");
        markedWordsParent.appendChild(markedWords);
        /*
        for(let t=1 ; t < childTdCountMax ; t++)
        {
            markedWordsParent.appendChild(document.createElement("td"));
        }
        */
        insertAfter(markedWordsParent, inputEleTrNode);
    } else {
        markedWords = document.createElement("div");
        markedWords.setAttribute("id", idOfMarkedWords);
        markedWords.classList.add("jsInsert");
        markedWords.style.display = "block";
        aim.appendChild(markedWords);
    }
    //markedWords.innerHTML="<br>Worked!";
    warmingDivCount++;
    console.log("xzzsCreate(" + inputElementId + ");Done!\nWDC:" + warmingDivCount + "\n");
}

function xzzsRemove(inputElementId) {
    console.log("xzzsRemove(" + inputElementId + ");Running!");
    let idOfMarkedWords = document.getElementById(inputElementId).getAttribute("id") + 'xz';
    let markedWords = document.getElementById(idOfMarkedWords);
    /* DIY code start*/
    if (friendlyWarming(inputElementId)) {
        xzzsBlockShow(markedWords);
        /*console.log(entireNode.getAttribute("id"));*/
        console.log("xzzsRemove(" + inputElementId + ");exit!");
        return false;
    }
    /* DIY code end*/
    xzzsEntireNode(markedWords).parentNode.removeChild(xzzsEntireNode(markedWords));
    warmingDivCount--;
    console.log("xzzsRemove(" + inputElementId + ");Done!\nWDC:" + warmingDivCount + "\n");
    return true;
}

function xzzs(inputElementId) {
    let inputEle = document.getElementById(inputElementId);
    /*let inputEle = inputE;*/
    let idOfMarkedWords = inputElementId + 'xz';
    let markedWords = document.getElementById(idOfMarkedWords);
    let maxcharLength;
    if (inputEle.getAttribute("maxlength")) {
        maxcharLength = parseInt(inputEle.getAttribute("maxlength"));
    } else {
        maxcharLength = 0;
    }

    let lenTmp = inputEle.value.length;
    //if(markedWords.style.display=="none")
    //	markedWords.style.display="block";
    //markedWords.innerText='目前输入的长度为 '+lenTmp;
    if (lenTmp > (maxcharLength - 6)) {
        if (lenTmp <= maxcharLength && lenTmp >= maxcharLength - 5) {
            if (markedWords.style.display.toUpperCase() === "none".toUpperCase()) {
                xzzsBlockShow(markedWords);
            }
            markedWords.innerHTML = "您还可以继续输入 " + "<span style=\"color:blue;\">" + (maxcharLength - lenTmp) + "</span>" + " 个字符";
        }
    } else if (lenTmp > (maxcharLength - 6) / 4) {
        markedWords.innerHTML = "您已输入 " + "<span style=\"color:green;\">" + lenTmp + "</span>" + " 个字符";
    } else {
        markedWords.style.display = "none";
    }
}

function xzzsParentNode(inputElement) {
    let inputEParent = inputElement.parentNode;
    if (inputEParent.tagName.toUpperCase() === "TD") {
        for (inputEParent = inputEParent.parentNode; inputEParent.tagName.toUpperCase() !== "TABLE"; inputEParent = inputEParent.parentNode) ;
    }
    return inputEParent;
}

function xzzsEntireNode(markedWords) {
    let aim;
    if (markedWords.tagName.toUpperCase() === "TD") {
        aim = markedWords.parentNode;
    } else {
        aim = markedWords;
    }
    return aim;
}

function xzzsBlockShow(markedWords) {
    if (markedWords.tagName.toUpperCase() === "TD") {
        markedWords.style.display = "table-cell";
    } else {
        markedWords.style.display = "block";
    }
}

function friendlyWarming(inputElementId) {
    let aim = document.getElementById(inputElementId);
    let idOfMarkedWords = inputElementId + 'xz';
    let markedWords = document.getElementById(idOfMarkedWords);
    if( ! markedWords ){
        xzzsCreate(inputElementId);
        idOfMarkedWords = inputElementId + 'xz';
        markedWords = document.getElementById(idOfMarkedWords);
        if( markedWords )
            return ! xzzsRemove(inputElementId);
    }
    let typeOfEle = aim.getAttribute("type").toUpperCase();
    let typeString1 = 'text'.toUpperCase();
    let typeString2 = 'password'.toUpperCase();
    let typeString3 = 'textarea'.toUpperCase();
    let keepWarming = false;
    let isNullable;

    if (typeOfEle === typeString1) {
        if (inputElementId === 'nickName') {
            isNullable = aim.getAttribute("isNullable");
            if (aim.value.length < 1) {
                if(isNullable === "false") {
                    if( markedWords )
                        markedWords.innerText = '昵称别漏了哦！';
                    keepWarming = true;
                }
            }
        } else if (inputElementId === 'userName') {
            isNullable = aim.getAttribute("isNullable");
            if (aim.value.length < 1) {
                if(isNullable === "false") {
                    if( markedWords )
                        markedWords.innerText = '用户名别漏了哦！';
                    keepWarming = true;
                }
            }
        } else if (inputElementId === 'email') {
            isNullable = aim.getAttribute("isNullable");
            if (aim.value.length < 1) {
                if(isNullable === "false"){
                    if( markedWords )
                        markedWords.innerText = '邮箱地址别漏了哦！';
                    keepWarming = true;
                }
            } else if (!isEmail(aim.value)) {
                if( markedWords )
                    markedWords.innerText = '请重新输入一个合法的邮箱地址！';
                keepWarming = true;
            }
        }
        else if (typeOfEle === typeString2) {
            let value1 = aim.value;
            if ( ! aim.getAttribute("compareWithTarget")){

                let markedWordsTmpNote = "";
                let okCount = 0;

                let containUppercaseLetter = /(.*?)[A-Z](.*?)/;
                let containLowercaseLetter = /(.*?)[a-z](.*?)/;
                let containNum = /(.*?)\d+(.*?)/;
                //( ) ` ~ ! @ # $ % ^ & * - _ + = | { } [ ] : ; ' < > , . ? /
                let containAllowedSpecialCharacter = new RegExp("[()`~!@#$%^&*-_+=|{}\\[\\]:;'<>,.\?/]");
                if (value1[0] === '/') {
                    markedWordsTmpNote += "不能以\"/\"作为首字符。";
                    keepWarming = true;
                }
                if (value1.match(containUppercaseLetter)) {
                    okCount++;
                }
                if (value1.match(containLowercaseLetter)) {
                    okCount++;
                }
                if (value1.match(containNum)) {
                    okCount++;
                }
                if (containAllowedSpecialCharacter.test(value1)) {
                    okCount++;
                }
                if (okCount < 3) {
                    markedWordsTmpNote += "密码需满足其中 3 项：至少包含1个大写英文字母、" +
                        "至少包含1个小写英文字母、至少包含1个数字或至少包含1个特殊字符，" +
                        "您当前已满足 " + okCount + " 项，";
                    keepWarming = true;
                }
                if (keepWarming) {
                    keepWarming = true;
                    markedWordsTmpNote += "请修正密码！";
                    if( markedWords )
                        markedWords.innerText = markedWordsTmpNote;
                }
            }else {
                let value2 = document.getElementById(aim.getAttribute("compareWithTarget")).value;
                if (value1 !== value2) {
                    keepWarming = true;
                    if( markedWords )
                        markedWords.innerText = '密码不一致，请重新输入！';
                }
            }
        }
        else if (typeOfEle === typeString3){
            //Nothing to do...I have no idea.
        }
    }
    if (!keepWarming) {
        let minLenA = aim.getAttribute("minlength");
        if (minLenA) {
            let minLen = parseInt(minLenA);
            if ( aim.value != null && aim.value.length < minLen) {
                // minLen++;
                keepWarming = true;
                if( markedWords ){
                    markedWords.innerText = '长度至少 ' + minLen + ' 位！';
                }
            }
        }
    }
    return keepWarming;
}

function isEmail(str) {
    let reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
    return reg.test(str);
}

function inputUnfinishCount(targetElement) {
    let count = 0;
    for (let i = 0, tmpvar; i < targetElement.length; i++) {
        let currEleId = targetElement[i].getAttribute("id");
        if(friendlyWarming(currEleId)){
            console.log("ID='" + currEleId + "' is not finished yet!");
            count++;
        }
    }
    return count;
}


function finalcheck() {
    let is_done = true;

    let AllinputE = document.getElementsByTagName("input");
    let AlltextareaE = document.getElementsByTagName("textarea");
    let inputEleUnfinishCount = inputUnfinishCount(AllinputE);
    let textareaEleUnfinishCount = inputUnfinishCount(AlltextareaE);
    console.log("finalcheck:inputEle unfinishCount=" + inputEleUnfinishCount);
    console.log("finalcheck:textareaEle unfinishCount=" + textareaEleUnfinishCount);
    if (inputEleUnfinishCount > 0 || textareaEleUnfinishCount > 0) {
        is_done = false;
    }
    if (!is_done) {
        let tmp_output = document.getElementById("jsDebug");
        let outputStr = "还有些东东没搞定呢！"
        if (tmp_output != null) {
            tmp_output.innerText = outputStr;
        } else {
            alert(outputStr);
        }
    }

    return is_done;
}

function insertAfter(newElement, targetElement) {
    let parent = targetElement.parentNode;
    if (parent.lastChild === targetElement) {
        parent.appendChild(newElement);
    } else {
        parent.insertBefore(newElement, targetElement.nextSibling);
    }
}

window.onload = function () {
    /*
    let kuan=document.documentElement.clientWidth;
    let gao=document.documentElement.clientHeight;
    document.styleSheets[0].cssRules[0].style.left=kuan/2 +"px";
    */

    let AllinputE = document.getElementsByTagName("input");

    xzzsInit(AllinputE);
    let AlltextareaE = document.getElementsByTagName("textarea");
    xzzsInit(AlltextareaE);

    if (secondPasswordConfirm != null) {
        let passwordRecheckEle = document.getElementById(secondPasswordConfirm);
        if (passwordRecheckEle)
            passwordRecheckEle.setAttribute("compareWithTarget", "password");
    }
    warmingDivCount = 0;
};