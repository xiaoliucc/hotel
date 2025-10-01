// const baseUrl = 'http://localhost:8080';
const baseUrl = window.location.hostname.includes('localhost')
    ? 'http://localhost:8080'
    : 'http://8.129.97.121:8080';
let bookedTimeRanges = []; // 存储已预订的时间段
function logout() {
    // 清除登录状态（例如，清除本地存储的token）
    localStorage.removeItem('token');
    // 重定向到登录页面
    window.location.href = 'login.html';
}

document.addEventListener('DOMContentLoaded', function () {
    const roomId = new URLSearchParams(window.location.search).get('id');
    if (!roomId) {
        alert('无效的房间ID');
        window.location.href = 'home.html';
        return;
    }

    Promise.all([
        fetch(`${baseUrl}/api/room/detail/${roomId}`, {
            headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token') }
        }),
        fetch(`${baseUrl}/api/room-images/room/${roomId}`, {
            headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token') }
        }),
        fetch(`${baseUrl}/api/order/room/${roomId}/booked-times`, {
            headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token') }
        })
    ])
        .then(response => Promise.all(response.map(response => response.json())))
        .then(([roomResult, imageResult, orderResult]) => {
            console.log('房间数据', roomResult);
            console.log('图片数据', imageResult);

            if (roomResult.code === 200) {
                const room = roomResult.data;
                // 加载基础数据
                document.getElementById('roomType').textContent = room.type || '未知类型';
                document.getElementById('roomNumber').textContent = `房间号: ${room.roomNumber || '未知'}`;
                document.getElementById('roomPrice').textContent = `价格: ￥${room.price || '未知'}/晚`;
                document.getElementById('roomDescription').innerHTML = '全新升级的豪华套房面积近百平方米，满足您更高品质居住需求...';

                if (imageResult.code === 200 && imageResult.data.length > 0) {
                    const primaryImage = imageResult.data.find(img => img.isPrimary) || imageResult.data[0];
                    console.log('primaryImage', primaryImage)
                    let imageUrl = primaryImage.imageUrl;

                    // 统一处理所有图片URL
                    if (imageUrl.includes('http://localhost:8080')) {
                        imageUrl = imageUrl.replace('http://localhost:8080', baseUrl);
                        console.log('修复后的图片URL:', imageUrl);
                    } else if (!imageUrl.startsWith('http')) {
                        // 如果是相对路径，拼接基础URL
                        imageUrl = baseUrl + (imageUrl.startsWith('/') ? imageUrl : '/' + imageUrl);
                    }
                    document.getElementById('roomImage').src = imageUrl;
                    document.getElementById('roomImage').alt = room.type;
                } else {
                    // 如果没有图片，显示默认图片
                    document.getElementById('roomImage').src = `${baseUrl}/images/default-room.jpg`;
                    document.getElementById('roomImage').alt = '默认房间图片';
                }
            } else {
                throw new Error(roomResult.msg || '获取房间详情失败');
            }
            if (orderResult.code === 200) {
                const bookedOrders = orderResult.data;
                bookedTimeRanges = bookedOrders || [];
                displayBookedTimes(bookedOrders);
            }
        })
        .catch(error => {
            console.error('Error fetching room details:', error);
            alert('获取房间详情失败: ' + error.message);
        });

    fetch(`${baseUrl}/api/room/detail/${roomId}`, {
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('token')
        }
    })
        .then(response => {
            console.log('Fetch response status:', response.status);
            return response;
        })
        .then(response => response.json())
        .then(result => {
            console.log('完整API响应:', result);
            if (result.code === 200) {
                const room = result.data;
                console.log('房间数据:', room); // 调试输出

                // 加载基础数据
                document.getElementById('roomType').textContent = room.type || '未知类型';
                document.getElementById('roomNumber').textContent = `房间号: ${room.roomNumber || '未知'}`;
                document.getElementById('roomPrice').textContent = `价格: ￥${room.price || '未知'}/晚`;
                document.getElementById('roomDescription').innerHTML = '全新升级的豪华套房面积近百平方米，满足您更高品质居住需求。房间包含一室一厅两卫及多个更衣间，移门分隔的客厅和卧室可平衡社交与私密需求。宽敞的客厅拥有舒适的沙发区，独立的餐桌、冰箱、微波炉、Nespresso咖啡机、写字台、储物柜，65寸高清可旋转LCD电视，贴心满足长期居住或小型社交活动需求。卧室配备超大舒适床品、无线及USB充电装置，带暗格收纳的梳妆镜台。您也可尊享行政楼层礼遇，在充满摩登老上海风情的行政酒廊尽享早餐、下午茶、欢乐时光及会议室。';

                //加载预订时间
                return fetch(`${baseUrl}/api/order/room/${roomId}/booked-times`, {
                    headers: {
                        'Authorization': 'Bearer ' + localStorage.getItem('token')
                    }
                });

            } else {
                throw new Error(result.msg || '获取房间详情失败');
            }
        })
        .then(response => response.json())
        .then(result => {
            if (result.code === 200) {
                const bookedOrders = result.data;
                bookedTimeRanges = bookedOrders || [];
                displayBookedTimes(bookedOrders);
            }
        })
        .catch(error => {
            console.error('Error fetching room details:', error);
            alert('获取房间详情失败: ' + error.message);
        });

    // 设置日期选择的最小值为今天
    const today = new Date().toISOString().split('T')[0];
    document.getElementById('checkIn').setAttribute('min', today);
    document.getElementById('checkOut').setAttribute('min', today);

    // 监听日期变化
    document.getElementById('checkIn').addEventListener('change', validateDates);
    document.getElementById('checkOut').addEventListener('change', validateDates);
});
//显示预订时间
function displayBookedTimes(bookedOrders) {
    const bookedTimesElem = document.getElementById('bookedTimes');
    if (!bookedOrders || bookedOrders.length === 0) {
        bookedTimesElem.innerHTML = '<span style="color: green;">✅ 当前可预订</span>';
        return;
    }
    // 格式化预订时间
    const timeRanges = bookedOrders.map(order => {
        try {
            const checkIn = new Date(order.checkIn).toISOString().split('T')[0];
            const checkOut = new Date(order.checkOut).toISOString().split('T')[0];
            return `${checkIn} 至 ${checkOut}`;
        } catch (e) {
            console.error('日期格式错误:', e);
            return '未知时间';
        }
    });

    bookedTimesElem.innerHTML = `<strong>📅 已预订时段:</strong><br>${timeRanges.join('<br>')}`;
}
//验证日期
function validateDates() {
    const checkIn = document.getElementById('checkIn').value;
    const checkOut = document.getElementById('checkOut').value;
    const bookButton = document.querySelector('.book-button');

    bookButton.disabled = true;
    bookButton.textContent = '预订';

    if (!checkIn || !checkOut) {
        return;
    }

    const validation = validateDateRules(checkIn, checkOut);
    if (!validation.valid) {
        alert(validation.message);
        return;
    }

    const conflictCheck = checkDateConflicts(checkIn, checkOut);
    if (!conflictCheck.isAvailable) {
        alert(conflictCheck.message);
        return;
    }

    bookButton.disabled = false;
    bookButton.textContent = '预订';
}
function validateDateRules(checkIn, checkOut) {
    const today = new Date();
    const checkInDate = new Date(checkIn);
    const checkOutDate = new Date(checkOut);
    today.setHours(0, 0, 0, 0); // 设置为今天的开始时间
    if (checkInDate < today) {
        return { valid: false, message: '入住日期不能早于今天' };
    }
    if (checkOutDate <= checkInDate) {
        return { valid: false, message: '离店日期必须晚于入住日期' };
    }
    const nights = Math.ceil((checkOutDate - checkInDate) / (1000 * 60 * 60 * 24));
    if (nights > 30) {
        return { valid: false, message: '最长预订30晚' };
    }
    return { valid: true };
}
function checkDateConflicts(checkIn, checkOut) {
    const checkInDate = new Date(checkIn);
    const checkOutDate = new Date(checkOut);

    for (const order of bookedTimeRanges) {
        const orderStart = new Date(order.checkIn);
        const orderEnd = new Date(order.checkOut);
        if (
            (checkInDate >= orderStart && checkInDate < orderEnd) ||
            (checkOutDate > orderStart && checkOutDate <= orderEnd) ||
            (checkInDate <= orderStart && checkOutDate >= orderEnd)
        ) {
            return { isAvailable: false, message: `所选日期与已有预订冲突(${order.checkIn}至${order.checkOut})` };
        };
    }
    return { isAvailable: true };
}


//预订房间
function bookRoom() {
    const roomId = new URLSearchParams(window.location.search).get('id');
    const checkIn = document.getElementById('checkIn').value;
    const checkOut = document.getElementById('checkOut').value;
    if (!roomId) {
        alert('无效的房间ID');
        return;
    }
    if (!checkIn || !checkOut) {
        alert('请选择入住和离店日期');
        return;
    }
    //验证日期
    const validation = validateDateRules(checkIn, checkOut);
    if (!validation.valid) {
        alert(validation.message);
        return;
    }
    const conflictCheck = checkDateConflicts(checkIn, checkOut);
    if (!conflictCheck.isAvailable) {
        alert(conflictCheck.message);
        return;
    }
    const bookButton = document.querySelector('.book-button');
    bookButton.disabled = true;
    bookButton.textContent = '预订中...';

    fetch(`${baseUrl}/api/order`, {
        method: 'POST',
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('token'),
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            roomId: parseInt(roomId),
            checkIn: checkIn,
            checkOut: checkOut
        })
    })
        .then(response => response.json())
        .then(result => {
            if (result.code === 200) {
                alert('预订成功！');
                window.location.reload();
            } else {
                alert('预订失败：' + result.message);
            }
        })
        .catch(error => {
            console.error('Error booking room:', error);
            alert('预订失败，请稍后重试。');
        });

}