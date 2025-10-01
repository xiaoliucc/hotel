// const baseUrl = 'http://localhost:8080';
const baseUrl = window.location.hostname.includes('localhost')
    ? 'http://localhost:8080'
    : 'http://8.129.97.121:8080';

let itemsPerPage = 6;//每页显示条数
let totalItems = 0;//总条数
let currentPage = 1;//当前页
let totalPages = 0;// Math.ceil(totalItems / itemsPerPage);//总页数
let currentRooms = [];
let token = localStorage.getItem('token');

function getSearchParams() {
    const roomType = document.getElementById('roomType').value;
    const checkIn = document.getElementById('checkIn').value;
    const checkOut = document.getElementById('checkOut').value;
    return {
        roomType: roomType || null,
        checkIn: checkIn || null,
        checkOut: checkOut || null
    };
}
function showMessage(msg, isError = true) {
    console.log(isError ? '错误信息' : '提示信息', msg);
    if (isError) {
        alert(msg);
    }
}

//后台加载数据
async function loadAllRooms() {
    try {
        const response = await fetch(`${baseUrl}/api/room/list`, {
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });
        const result = await response.json();
        if (result.code !== 200) {
            throw new Error(result.msg || '加载房间失败');
        } else {
            return result.data || [];
        }
    } catch (error) {
        showMessage('加载房间失败: ' + error.message);
        return [];
    }

}

let isLoading = false;
async function loadPage(page) {
    if (isLoading) return;
    try {
        const allRooms = await loadAllRooms();
        totalItems = allRooms.length;
        totalPages = Math.ceil(totalItems / itemsPerPage);
        const start = (page - 1) * itemsPerPage;
        const end = start + itemsPerPage;
        const rooms = allRooms.slice(start, end);

        const roomList = document.querySelector('.room-list');
        roomList.innerHTML = '';

        await renderRooms(rooms);
        updatePagination();
    } catch (error) {
        showMessage('加载房间失败: ' + error.message);
    } finally {
        isLoading = false;
    }
}

async function loadRoomImages(roomId) {
    console.log('roomId:', roomId);
    console.log('baseurl:', baseUrl);
    try {
        const token = localStorage.getItem('token');
        console.log('token:', token);
        const response = await fetch(`${baseUrl}/api/room-images/room/${roomId}`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        console.log('图片的请求', response);
        const result = await response.json();
        console.log('result json:', result);

        if (result.code === 200 && result.data.length > 0) {
            const primaryImage = result.data.find(img => img.isPrimary) || result.data[0];
            let imageUrl = primaryImage.imageUrl;
            console.log('原始图片URL：', imageUrl);
            // 统一处理所有图片URL
            if (imageUrl.includes('http://localhost:8080')) {
                imageUrl = imageUrl.replace('http://localhost:8080', baseUrl);
            } else if (!imageUrl.startsWith('http')) {
                imageUrl = baseUrl + (imageUrl.startsWith('/') ? imageUrl : '/' + imageUrl);
            }
            console.log('处理后imageUrl:', imageUrl);
            return imageUrl;
        }
    } catch (error) {
        console.error('加载房间图片失败', error);
    }
    return `${baseUrl}/images/default-room.jpg`;
}
async function renderRooms(rooms) {
    const roomList = document.querySelector('.room-list');

    roomList.innerHTML = '';

    if (rooms.length === 0) {
        roomList.innerHTML = '<p>没有找到符合条件的房间。</p>';
        return;
    }
    for (const room of rooms) {
        const imageUrl = await loadRoomImages(room.id);
        console.log('imageUrl:', imageUrl);
        const li = document.createElement('div');
        li.className = 'room-card';
        li.innerHTML = `
                    <div class="room-item">
                        <div class="room-img">
                            <img src="${imageUrl}" alt="${room.type}">
                        </div>
                        <div class="room-info">
                            <h3>${room.type}</h3>
                            <span>NO.${room.roomNumber}</span>
                            <p>价格：￥${room.price}/晚</p>
                            <p>简介：全新升级的豪华套房面积近百平方米，满足您更高品质居住需求。房间包含一室一厅两卫及多个更衣间，移门分隔的客厅和卧室可平衡社交与私密需求。宽敞的客厅拥有舒适的沙发区，独立的餐桌、冰箱、微波炉、Nespresso咖啡机、写字台、储物柜，65寸高清可旋转LCD电视，贴心满足长期居住或小型社交活动需求。卧室配备超大舒适床品、无线及USB充电装置，带暗格收纳的梳妆镜台。您也可尊享行政楼层礼遇，在充满摩登老上海风情的行政酒廊尽享早餐、下午茶、欢乐时光及会议室。</p>
                            <button onclick="bookRoom(${room.id})">预订</button>
                        </div>
                    </div>
                `;
        roomList.appendChild(li);
    }
}

function updateItemsPerPage() {
    const select = document.getElementById('itemsPerPage');
    select.value = itemsPerPage;
}


async function findRoom() {
    const roomType = document.getElementById('roomType').value;
    const checkIn = document.getElementById('checkIn').value;
    const checkOut = document.getElementById('checkOut').value;

    try {
        let rooms;

        // 如果有日期条件，使用搜索API
        if (checkIn && checkOut) {
            const response = await fetch(`${baseUrl}/api/room/search`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    roomType: roomType || null,
                    checkIn: checkIn,
                    checkOut: checkOut
                })
            });

            const result = await response.json();
            if (result.code !== 200) {
                throw new Error(result.msg || '搜索失败');
            }
            rooms = result.data || [];
        } else {
            // 没有日期条件，获取所有房间
            const response = await fetch(`${baseUrl}/api/room/list`, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });

            const result = await response.json();
            if (result.code !== 200) {
                throw new Error(result.msg || '加载房间失败');
            }
            rooms = result.data || [];

            // 前端过滤房间类型
            if (roomType) {
                rooms = rooms.filter(room => room.type === roomType);
            }
        }

        totalItems = rooms.length;
        updatePagination();
        renderRooms(rooms);

    } catch (error) {
        console.error('操作失败:', error);
        showMessage('操作失败: ' + error.message);
        loadPage(currentPage);
    }
}
function searchByType(roomType) {
    document.getElementById('roomType').value = roomType;
    currentPage = 1;
    findRoom();
}
function reSet() {
    document.getElementById('roomType').value = '';
    document.getElementById('checkIn').value = '';
    document.getElementById('checkOut').value = '';
    currentPage = 1;
    loadPage(currentPage);
}

function bookRoom(roomId) {
    if (!token) {
        alert('请先登录');
        location.href = 'login.html';
        return;
    }
    window.location.href = `room-detail.html?id=${roomId}`;
}
function updatePagination() {
    console.log('更新分页显示:totalItems=' + totalItems + ', currentPage=' + currentPage + ', itemsPerPage=' + itemsPerPage);

    const currentPageElement = document.getElementById('currentPage');
    if (currentPageElement) {
        currentPageElement.innerText = currentPage;
    }

    const totalItemsElement = document.getElementById('totalItems');
    if (totalItemsElement) {
        totalItemsElement.innerText = '共' + totalItems + '条';
    }

    totalPages = Math.ceil(totalItems / itemsPerPage);/*计算总页数*/
    console.log('总页数：' + totalPages);

    // 禁用上一页按钮
    document.querySelector('button[onclick="prevPage()"]').disabled = currentPage === 1;
    // 禁用下一页按钮
    document.querySelector('button[onclick="nextPage()"]').disabled = currentPage === totalPages;
}
function prevPage() {
    if (currentPage > 1) {
        currentPage--;
        loadPage(currentPage);
    }
}
function nextPage() {
    if (currentPage < totalPages) {
        currentPage++;
        loadPage(currentPage);
    }
}
function goToPage() {
    const pageInput = document.getElementById('goToPageInput');
    const page = parseInt(pageInput.value);
    if (page >= 1 && page <= totalPages) {
        currentPage = page;
        loadPage(currentPage);
    } else {
        alert('请输入有效的页码');
    }
    pageInput.value = '';
}
function closeModal() {
    document.getElementById('mask').style.display = 'none';
}

function logout() {
    localStorage.removeItem('token');
    location.href = 'login.html';
}

//事件监听
document.addEventListener('DOMContentLoaded', function () {
    const token = localStorage.getItem('token');
    const today = new Date().toISOString().split('T')[0];
    const tomorrow = new Date(Date.now() + 86400000).toISOString().split('T')[0];
    document.getElementById('checkIn').value = today;
    document.getElementById('checkOut').value = tomorrow;
    if (!token) {
        alert('请先登录');
        location.href = 'login.html';
    }

    updateItemsPerPage();

    document.getElementById('roomType').addEventListener('change', function () {
        currentPage = 1;
        findRoom();
    });
    document.getElementById('checkIn').addEventListener('change', function () {
        currentPage = 1;
        findRoom();
    });
    document.getElementById('checkOut').addEventListener('change', function () {
        currentPage = 1;
        findRoom();
    });
    document.getElementById('itemsPerPage').addEventListener('change', function () {
        itemsPerPage = parseInt(this.value);
        currentPage = 1;
        loadPage(currentPage);
    });
    loadPage(currentPage);
});