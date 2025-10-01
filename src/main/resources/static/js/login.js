// const baseUrl = 'http://localhost:8080';
const baseUrl = window.location.hostname.includes('localhost')
    ? 'http://localhost:8080'
    : 'http://8.129.97.121:8080';
const msgBox = document.getElementById('msg');
function showMsg(txt, isError = true) {
    msgBox.textContent = txt;
    if (isError) {
        msgBox.className = 'error';
    } else {
        msgBox.className = 'success';
    }
}

/* 登录 */
async function handleLogin() {
    const username = document.getElementById('loginUser').value.trim();
    const password = document.getElementById('password').value.trim();

    if (!username || !password) {
        showMsg('用户名和密码不能为空');
        return;
    }


    try {
        const response = await fetch(`${baseUrl}/api/user/login?username=${username}&password=${password}`, {
            method: 'POST'
        });
        const result = await response.json();
        if (result.code === 200) {
            localStorage.setItem('token', result.data.token);
            showMsg('登录成功', false);
            setTimeout(() => {
                window.location.href = '/home.html';
            }, 1000);
        } else {
            showMsg(result.msg || '登录失败');
        }
    } catch (error) {
        showMsg('网络错误，请稍后重试' + error.message);
    }
}

/* 注册 */
async function handleRegister() {
    const username = document.getElementById('registerUser').value.trim();
    const password = document.getElementById('registerPassword').value.trim();

    if (!username || !password) {
        showMsg('用户名和密码不能为空');
        return;
    }

    try {
        const response = await fetch(`${baseUrl}/api/user/register?username=${username}&password=${password}`, {
            method: 'POST'
        });
        const result = await response.json();
        if (result.code === 200) {
            showMsg('注册成功，请登录', false);
            document.getElementById('registerUser').value = '';
            document.getElementById('registerPassword').value = '';
        } else {
            showMsg(result.msg || '注册失败');
        }
    } catch (error) {
        showMsg('网络错误，请稍后重试' + error.message);
    }
}