export const changeUTCtoDateTime = (format, UTCTime = +new Date()) => {
    if (!format) {
        return '';
    }

    let timeString = UTCTime;
    if (UTCTime instanceof Date || typeof UTCTime === 'string') {
        timeString = +UTCTime;
    }

    const timeDate = new Date(timeString);
    const year = timeDate.getFullYear();
    const month =
        timeDate.getMonth() + 1 < 10
            ? '0' + (timeDate.getMonth() + 1)
            : timeDate.getMonth() + 1;
    const date =
        timeDate.getDate() < 10 ? '0' + timeDate.getDate() : timeDate.getDate();
    const hour =
        timeDate.getHours() < 10
            ? '0' + timeDate.getHours()
            : timeDate.getHours();
    const minute =
        timeDate.getMinutes() < 10
            ? '0' + timeDate.getMinutes()
            : timeDate.getMinutes();
    const second =
        timeDate.getSeconds() < 10
            ? '0' + timeDate.getSeconds()
            : timeDate.getSeconds();

    if (format === 'yyyy-MM-dd HH:mm:ss') {
        return `${year}-${month}-${date} ${hour}:${minute}:${second}`;
    } else if (format === 'yyyy-MM-dd') {
        return `${year}-${month}-${date}`;
    } else {
        return '';
    }
};

export const checkType = anyTypeValue => {
    const array = {
        '[object Number]': 'number',
        '[object String]': 'string',
        '[object Boolean]': 'boolean',
        '[object Undefined]': 'undefined',
        '[object Null]': 'null',
        '[object Object]': 'object',
        '[object Array]': 'array',
        '[object Symbol]': 'symbol',
        '[object Date]': 'date',
        '[object Error]': 'error',
        '[object RegExp]': 'regExp',
        '[object Function]': 'function',
        '[object Math]': 'math',
        '[object JSON]': 'json',
        '[object Arguments]': 'arguments'
    };
    return array[Object.prototype.toString.call(anyTypeValue)];
};
