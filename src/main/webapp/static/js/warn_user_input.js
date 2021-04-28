var warmingDivCount = 0;
var secondPasswordConfirm = null;
var passwordTypeCount = 0;

function xzzsInit(limitTarget) {
    var EleId;
    for (var i = 0, tmpvar; i < limitTarget.length; i++) {
        //var maxLen=limitTarget[i].getAttribute("maxlength");
        tmpvar = i + 1;
        EleId = limitTarget[i].getAttribute("id");

        if (EleId == null) {
            limitTarget[i].setAttribute("id", "inputNo_" + tmpvar);
            EleId = "inputNo_" + tmpvar;
        }
        console.log("process:" + EleId);
        //alert(limitTarget[i]);
        var parentOfinputE = xzzsParentNode(limitTarget[i]);
        if (!parentOfinputE.getAttribute("id"))
            parentOfinputE.setAttribute("id", EleId + "_" + tmpvar);
        if ((limitTarget[i].getAttribute("type") === "text" || limitTarget[i].getAttribute("type") === "password")) {
            if (limitTarget[i].getAttribute("type") === "password") {
                passwordTypeCount++;
                if (passwordTypeCount === 2) {
                    secondPasswordConfirm = limitTarget[i].getAttribute("id");
                }
            }
            limitTarget[i].addEventListener('focus', xzzsCreate.bind(this, EleId));
            limitTarget[i].addEventListener('blur', xzzsRemove.bind(this, EleId));
            limitTarget[i].addEventListener('keyup', xzzs.bind(this, EleId));
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

function xzzsCreate(inputE) {
    var idOfMarkedWords = inputE + 'xz';
    var markedWords;
    if (document.getElementById(idOfMarkedWords)) {
        return;
    }
    var aim = xzzsParentNode(document.getElementById(inputE));
    console.log("xzzsCreate(" + inputE + ");Running!");
    if (aim.tagName.toUpperCase() === "TABLE") {
        var trEle = aim.getElementsByTagName("tr");
        var childTdCountMax = 0;
        for (var i = 0, tmpCount; i < trEle.length; i++) {
            tmpCount = trEle[i].getElementsByTagName("td").length;
            if (tmpCount > childTdCountMax) {
                childTdCountMax = tmpCount;
            }
        }
        var inputEleTrNode = document.getElementById(inputE).parentNode;
        for (; inputEleTrNode.tagName.toUpperCase() !== "TR"; inputEleTrNode = inputEleTrNode.parentNode) ;
        markedWords = document.createElement("td");
        markedWords.setAttribute("id", idOfMarkedWords);
        markedWords.colSpan = childTdCountMax;
        markedWords.style.display = "table-cell";
        markedWords.classList.add("jsInsert");
        var markedWordsParent = document.createElement("tr");
        markedWordsParent.setAttribute("id", idOfMarkedWords + "P");
        markedWordsParent.appendChild(markedWords);
        /*
        for(var t=1 ; t < childTdCountMax ; t++)
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
    console.log("xzzsCreate(" + inputE + ");Done!\nWDC:" + warmingDivCount + "\n");
}

function xzzsRemove(inputE) {
    console.log("xzzsRemove(" + inputE + ");Running!");
    var idOfMarkedWords = document.getElementById(inputE).getAttribute("id") + 'xz';
    var markedWords = document.getElementById(idOfMarkedWords);
    /* DIY code start*/
    if (friendlyWarming(inputE)) {
        xzzsBlockShow(markedWords);
        /*console.log(entireNode.getAttribute("id"));*/
        console.log("xzzsRemove(" + inputE + ");exit!");
        return;
    }
    /* DIY code end*/
    xzzsEntireNode(markedWords).parentNode.removeChild(xzzsEntireNode(markedWords));
    warmingDivCount--;
    console.log("xzzsRemove(" + inputE + ");Done!\nWDC:" + warmingDivCount + "\n");
}

function xzzs(inputE) {
    var inputEle = document.getElementById(inputE);
    /*var inputEle = inputE;*/
    var idOfMarkedWords = inputE + 'xz';
    var markedWords = document.getElementById(idOfMarkedWords);
    var maxcharLength;
    if (inputEle.getAttribute("maxlength")) {
        maxcharLength = parseInt(inputEle.getAttribute("maxlength"));
    } else {
        maxcharLength = 0;
    }

    var lenTmp = inputEle.value.length;
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

function xzzsParentNode(inputE) {
    var inputEParent = inputE.parentNode;
    if (inputEParent.tagName.toUpperCase() === "TD") {
        for (inputEParent = inputEParent.parentNode; inputEParent.tagName.toUpperCase() !== "TABLE"; inputEParent = inputEParent.parentNode) ;
    }
    return inputEParent;
}

function xzzsEntireNode(markedWords) {
    var aim;
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

function friendlyWarming(inputEleId) {
    var aim = document.getElementById(inputEleId);
    var idOfMarkedWords = inputEleId + 'xz';
    var markedWords = document.getElementById(idOfMarkedWords);
    var typeOfEle = aim.getAttribute("type").toUpperCase();
    var typeString1 = 'password'.toUpperCase();
    var typeString2 = 'text'.toUpperCase();
    var keepWarming = false;
    var isNullable;

    if (typeOfEle === typeString1) {
        var value1 = aim.value;
        if ( ! aim.getAttribute("compareWithTarget")){

            var markedWordsTmpNote = "";
            var okCount = 0;

            var containUppercaseLetter = /(.*?)[A-Z](.*?)/;
            var containLowercaseLetter = /(.*?)[a-z](.*?)/;
            var containNum = /(.*?)\d+(.*?)/;
            //( ) ` ~ ! @ # $ % ^ & * - _ + = | { } [ ] : ; ' < > , . ? /
            var containAllowedSpecialCharacter = new RegExp("[()`~!@#$%^&*-_+=|{}\\[\\]:;'<>,.\?/]");
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
                markedWords.innerText = markedWordsTmpNote;
            }
        }else {
            var value2 = document.getElementById(aim.getAttribute("compareWithTarget")).value;
            if (value1 !== value2) {
                markedWords.innerText = '密码不一致，请重新输入！';
                keepWarming = true;
            }
        }
    }
    else if (typeOfEle === typeString2) {
        if (inputEleId === 'nickName') {
            isNullable = aim.getAttribute("isNullable");
            if (aim.value.length < 1) {
                if(isNullable === "false") {
                    markedWords.innerText = '昵称别漏了哦！';
                    keepWarming = true;
                }
            }
        } else if (inputEleId === 'userName') {
            isNullable = aim.getAttribute("isNullable");
            if (aim.value.length < 1) {
                if(isNullable === "false") {
                    markedWords.innerText = '用户名别漏了哦！';
                    keepWarming = true;
                }
            }
        } else if (inputEleId === 'email') {
            isNullable = aim.getAttribute("isNullable");
            if (aim.value.length < 1) {
                if(isNullable === "false"){
                    markedWords.innerText = '邮箱地址别漏了哦！';
                    keepWarming = true;
                }
            } else if (!isEmail(aim.value)) {
                markedWords.innerText = '请重新输入一个合法的邮箱地址！';
                keepWarming = true;
            }
        }
    }
    if (!keepWarming) {
        var minLenA = aim.getAttribute("minlength");
        if (minLenA) {
            var minLen = parseInt(minLenA);
            if (aim.value.length <= minLen) {
                minLen++;
                markedWords.innerText = '长度至少 ' + minLen + ' 位！';
                keepWarming = true;
            }
        }
    }
    return keepWarming;
}

function isEmail(str) {
    let reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
    return reg.test(str);
}

function inputUnfinishCount(target) {
    var count = 0;
    for (var i = 0, tmpvar; i < target.length; i++) {
        let currType = target[i].getAttribute("type").toUpperCase();
        if ( currType === "text".toUpperCase() ||
            currType === "password".toUpperCase() )
            if (target[i].value == null) {
                console.log("ID='" + target[i].getAttribute("id") + "' is not finished yet!");
                count++;
            } else if (target[i].value.length === 0) {
                console.log("ID='" + target[i].getAttribute("id") + "' is not finished yet!");
                count++;
            }
    }
    return count;
}

function inputLenghtCheck(target) {
    if (target.value == null) {
        return false;
    } else if (target.value.length <= 1) {
        return false;
    }
    return true;
}

function finalcheck() {
    var is_done = true;

    var AllinputE = document.getElementsByTagName("input");
    var AlltextareaE = document.getElementsByTagName("textarea");
    var inputEleUnfinishCount = inputUnfinishCount(AllinputE);
    var textareaEleUnfinishCount = inputUnfinishCount(AlltextareaE);
    console.log("finalcheck:inputEle unfinishCount=" + inputEleUnfinishCount);
    console.log("finalcheck:textareaEle unfinishCount=" + textareaEleUnfinishCount);
    if (textareaEleUnfinishCount > 0) {
        is_done = false;
    }
    if (!is_done) {
        var tmp_output = document.getElementById("jsDebug");
        var outputStr = "还有些东东没搞定呢！"
        if (tmp_output != null) {
            tmp_output.innerText = outputStr;
        } else {
            alert(outputStr);
        }
    }

    return is_done;
}

function insertAfter(newElement, targetElement) {
    var parent = targetElement.parentNode;
    if (parent.lastChild === targetElement) {
        parent.appendChild(newElement);
    } else {
        parent.insertBefore(newElement, targetElement.nextSibling);
    }
}

window.onload = function () {
    /*
    var kuan=document.documentElement.clientWidth;
    var gao=document.documentElement.clientHeight;
    document.styleSheets[0].cssRules[0].style.left=kuan/2 +"px";
    */

    var AllinputE = document.getElementsByTagName("input");

    xzzsInit(AllinputE);
    var AlltextareaE = document.getElementsByTagName("textarea");
    xzzsInit(AlltextareaE);

    if (secondPasswordConfirm != null) {
        var passwordRecheckEle = document.getElementById(secondPasswordConfirm);
        if (passwordRecheckEle)
            passwordRecheckEle.setAttribute("compareWithTarget", "password");
    }
    warmingDivCount = 0;
};