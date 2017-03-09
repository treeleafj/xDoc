function checkRetCode(code) {
    if (code == '999993') {
        window.location = '/#/login';
    }
}

export default {
    checkRetCode : checkRetCode
}