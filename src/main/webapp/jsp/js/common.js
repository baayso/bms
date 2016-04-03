/* 毫秒数转换为YYYY-MM-dd hh:mm:ss */
function getLocalTime(ns) {
    if (!isNaN(ns)) {
        var now = new Date(ns);
        var year = now.getFullYear();
        var month = now.getMonth() + 1;
        if (month < 10) {
            month = "0" + month;
        }
        var date = now.getDate();
        if (date < 10) {
            date = "0" + date;
        }
        var hour = now.getHours();
        if (hour < 10) {
            hour = "0" + hour;
        }
        var minute = now.getMinutes();
        if (minute < 10) {
            minute = "0" + minute;
        }
        var second = now.getSeconds();
        if (second < 10) {
            second = "0" + second;
        }
        return year + "-" + month + "-" + date + " " + hour + ":" + minute + ":" + second;
    }
    else {
        return "";
    }
}