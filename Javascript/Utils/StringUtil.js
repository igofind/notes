// 数据库编码为UTF-8,JS对于中文的长度的统计(length)不准确
function getStrLength(value) {
    var num = 0;
    value = (value + "").split("");
    for (var i = 0; i < value.length; i++) {
        var cn = value[i];
        //全角字符校验，中文是全角，此处用的是UTF-8
        var re = /[^\x00-\x80]/;//  /[^\x00-\x80]/; //或    /[^\0-\127]/    十进制表示
        if (/^[\u4e00-\u9fa5]+$/.test(cn) || re.test(cn)) {
            num = num + 3;
        } else {
            num = num + 1;
        }
    }
    return num;
}