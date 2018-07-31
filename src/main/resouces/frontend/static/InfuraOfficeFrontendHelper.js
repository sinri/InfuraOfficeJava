// ES2016
// use https://github.com/js-cookie/js-cookie
const CookieHelper = {
    tokenKey: "infura-office-java-token",
    usernameKey: "infura-office-java-username",
    isLogin: function () {
        let token = CookieHelper.getToken();
        console.log('isLogin?', token, !!token);
        return !!token;
    },
    getToken: function () {
        return Cookies.get(CookieHelper.tokenKey);
    },
    getUsername: function () {
        return Cookies.get(CookieHelper.usernameKey);
    },
    setToken: function (token, username, life_seconds) {
        if (!life_seconds) {
            life_seconds = -1;
        }
        Cookies.set(CookieHelper.tokenKey, token, {
            expires: new Date(new Date().getTime() + life_seconds * 1000)
        });
        Cookies.set(CookieHelper.usernameKey, username, {
            expires: new Date(new Date().getTime() + life_seconds * 1000)
        });
    },
    clearToken: function () {
        CookieHelper.setToken(null);
    }
};

const callInfuraOfficeJsonAPI = function (method, url, data, callbackForOK, callbackForFail) {
    if (!data) data = {};
    data.token = CookieHelper.getToken();
    axios({
        method: method,
        url: url,
        data: data
    }).then((response) => {
        console.log(response.data);
        console.log(response.status);
        console.log(response.statusText);
        console.log(response.headers);
        console.log(response.config);

        if (response.data.code === 'OK') {
            if (callbackForOK) callbackForOK(response.data.data);
        } else {
            if (callbackForFail) callbackForFail(response.data.data);
        }
    }).catch((error) => {
        console.log(error);
        if (error.response && error.response.status === 403) {
            //goto login
            console.log("token invalidated -> logout");
            CookieHelper.setToken(null);
            window.location.href = "login.html";
        } else {
            if (callbackForFail) callbackForFail(error);
        }
    });
};

const AliyunRegionDictionary = [
    {label: '青岛 / 华北1', key: "cn-qingdao"},
    {label: '北京 / 华北2', key: "cn-beijing"},
    {label: '张家口 / 华北3', key: "cn-zhangjiakou"},
    {label: '呼和浩特 / 华北5', key: "cn-huhehaote"},
    {label: '杭州 / 华东1', key: "cn-hangzhou"},
    {label: '上海 / 华东2', key: "cn-shanghai"},
    {label: '深圳 / 华南1', key: "cn-shenzhen"},
    {label: '香港', key: "cn-hongkong"},
    {label: '新加坡 / 亚太东南1', key: "ap-southeast-1"},
    {label: '悉尼 / 亚太东南2', key: "ap-southeast-2"},
    {label: '吉隆坡 / 亚太东南3', key: "ap-southeast-3"},
    {label: '雅加达 / 亚太东南5', key: 'ap-southeast-5'},
    {label: '孟买 / 亚太南部1', key: 'ap-south-1'},
    {label: '东京 / 亚太东北1', key: "ap-northeast-1"},
    {label: '硅谷 / 美西1', key: "us-west-1"},
    {label: '弗吉尼亚 / 美东1', key: "us-east-1"},
    {label: '法兰克福 / 欧洲中部1', key: "eu-central-1"},
    {label: '迪拜 / 中东东部1', key: "me-east-1"},
];
const QcloudRegionDictionary = [];

function allPlatformAreaCodeOptions() {
    let areaCodeOptions = {
        "": [
            {label: 'None', key: ''}
        ],
        aliyun: [],
        qcloud: []
    };
    AliyunRegionDictionary.forEach(x => {
        areaCodeOptions.aliyun.push(x)
    });
    return areaCodeOptions;
}

function jsReadableValue(anything) {
    if (typeof anything === 'object') {
        let str = '';
        if (Array.isArray(anything)) {
            for (let i = 0; i < anything.length; i++) {
                str += jsReadableValue(anything[i]) + '\n';
            }
        } else {
            for (let k in anything) {
                if (!anything.hasOwnProperty(k)) continue;
                str += k + ": " + jsReadableValue(anything[k]) + '\n';
            }
        }
        return str;
    } else {
        return "" + anything;
    }
}
