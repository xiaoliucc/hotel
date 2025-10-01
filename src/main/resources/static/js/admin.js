// const baseUrl = 'http://localhost:8080';
const baseUrl = window.location.hostname.includes('localhost')
    ? 'http://localhost:8080'
    : 'http://8.129.97.121:8080';
let token = localStorage.getItem('token');

function checkLogin() {
    if (!token) {
        alert('请先登录');
        window.location.href = 'login.html';
        return false;
    }
    return true;
}
async function checkAdmin() {
    if (!checkLogin()) return;
    try {
        // 解析JWT token获取角色信息
        console.log('token:', token);
        const payload = JSON.parse(atob(token.split('.')[1]));
        const userRole = payload.role;

        console.log("role:", userRole);

        // 检查是否是管理员
        if (userRole !== 'ADMIN') {
            alert('权限不足，需要管理员权限');
            window.location.href = 'home.html';
            return false;
        }
        return true;
    } catch (error) {
        console.error('检查管理员权限失败:', error);
        return false;
    }

}
function showSection(sectionName) {
    //隐藏所有内容区域
    document.querySelectorAll('.content-section').forEach(section => {
        section.classList.remove('active');
    })

    //显示选中区域
    const targetSection = document.getElementById(sectionName);
    if (targetSection) {
        targetSection.classList.add('active');

        switch (sectionName) {
            case 'dashboard':
                loadDashboardData();
                break;
            case 'room-management':
                loadRooms();
                break;
            case 'order-management':
                loadOrders();
                break;
            case 'user-management':
                loadUsers();
                break;
        }
    }
}

async function loadDashboardData() {
    if (!checkLogin()) return;

    try {
        //获取房间总数
        const roomsResponse = await fetch(`${baseUrl}/api/room/list`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        const roomsResult = await roomsResponse.json();
        const totalRooms = roomsResult.code === 200 ? roomsResult.data.length : 0;

        // 获取订单总数
        const ordersResponse = await fetch(`${baseUrl}/api/order/all`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        const ordersResult = await ordersResponse.json();
        const totalOrders = ordersResult.code === 200 ? ordersResult.data.length : 0;

        // 获取用户总数
        const usersResponse = await fetch(`${baseUrl}/api/user/all`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        const usersResult = await usersResponse.json();
        const totalUsers = usersResult.code === 200 ? usersResult.data.length : 0;

        //更新仪表盘数据
        document.getElementById('totalRooms').textContent = totalRooms;
        document.getElementById('totalOrders').textContent = totalOrders;
        document.getElementById('totalUsers').textContent = totalUsers;
    } catch (error) {
        console.error('加载仪表盘数据失败:', error);
    }
}

//加载房间数据
async function loadRooms() {
    if (!checkLogin()) return;

    try {
        const response = await fetch(`${baseUrl}/api/room/list`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        const result = await response.json();
        if (result.code === 200) {
            renderRooms(result.data);
        } else {
            throw new Error(result.msg);
        }
    } catch (error) {
        console.error('加载房间失败:', error);
    }
}

function renderRooms(rooms) {
    const tbody = document.querySelector('#roomTable tbody');
    tbody.innerHTML = '';

    rooms.forEach(room => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
                    <td>${room.id}</td>
                    <td>${room.roomNumber}</td>
                    <td>${room.type}</td>
                    <td>￥${room.price}</td>
                    <td>${room.status}</td>
                    <td>
                    <button class="btn-edit" onclick="editRoom(${room.id})">编辑</button>
                    <button class="btn-delete" onclick="deleteRoom(${room.id})">删除</button>
                    <button style="background: #52c41a; color: white; padding: 6px 12px; border: none; border-radius: 4px; cursor: pointer;" 
                        onclick="openImageManager(${room.id})">图片管理</button>
                    </td>
                `
        tbody.appendChild(tr);
    })
}

//加载订单数据
async function loadOrders() {
    if (!checkLogin()) return;

    try {
        const response = await fetch(`${baseUrl}/api/order/all`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        const result = await response.json();
        if (result.code === 200) {
            renderOrders(result.data);
        } else {
            throw new Error(result.msg);
        }
    } catch (error) {
        console.error('加载订单失败:', error);
    }
}
function renderOrders(orders) {
    const tbody = document.querySelector('#orderTable tbody');
    tbody.innerHTML = '';

    orders.forEach(order => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
                    <td>${order.id}</td>
                    <td>${order.userId}</td>
                    <td>${order.roomId}</td>
                    <td>${order.checkIn}</td>
                    <td>${order.checkOut}</td>
                    <td>${order.status}</td>
                    <td>${order.createTime || 'N/A'}</td>
                    <td>
                    <button class="btn-edit" onclick="viewOrder(${order.id})">详情</button>
                    <button class="btn-delete" onclick="cancelOrder(${order.id})">取消</button>
                    </td>
                `
        tbody.appendChild(tr);
    })
}

//加载用户数据
async function loadUsers() {
    if (!checkLogin()) return;

    try {
        const response = await fetch(`${baseUrl}/api/user/all`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        const result = await response.json();
        if (result.code === 200) {
            renderUsers(result.data);
        } else {
            throw new Error(result.msg);
        }
    } catch (error) {
        console.error('加载用户失败:', error);
    }
}
function renderUsers(users) {
    const tbody = document.querySelector('#userTable tbody');
    tbody.innerHTML = '';

    users.forEach(user => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
                    <td>${user.id}</td>
                    <td>${user.username}</td>
                    <td>${user.role}</td>
                    <td>${user.createTime}</td>
                    <td>
                    <button class="btn-edit" onclick="editUser(${user.id})">编辑</button>
                    <button class="btn-delete" onclick="deleteUser(${user.id})">删除</button>
                    </td>
                `
        tbody.appendChild(tr);
    })
}
//图片管理
async function openImageManager(roomId) {
    const modal = document.getElementById('imageManagerModal');
    if (!modal) {
        createImageManagerModal();
    }
    document.getElementById('imageManagerModal').style.display = 'flex';
    document.getElementById('currentRoomId').value = roomId;
    await loadRoomImages(roomId);

}
function createImageManagerModal() {
    const modalHTML = `
                <div id="imageManagerModal" class="modal-mask">
                    <div class="modal-content" style="max-width: 800px;">
                        <div class="modal-header">
                            <h3>房间图片管理</h3>
                            <button class="modal-close" onclick="closeImageManager()">&times;</button>
                        </div>
                        <div class="form-group">
                            <label>上传图片</label>
                            <input type="file" id="imageFile" accept="image/*">
                            <label style="display: inline-block; margin-left: 10px;">
                                <input type="checkbox" id="setAsPrimary"> 设为主图
                            </label>
                            <button onclick="uploadImage()" style="margin-top: 10px;">上传</button>
                        </div>
                        <div id="imageList" style="margin-top: 20px; display: grid; grid-template-columns: repeat(3, 1fr); gap: 10px;">
                            <!-- 图片列表将在这里显示 -->
                        </div>
                        <input type="hidden" id="currentRoomId">
                    </div>
                </div>
                `;
    document.body.insertAdjacentHTML('beforeend', modalHTML);
}
async function loadRoomImages(roomId) {
    try {
        const response = await fetch(`${baseUrl}/api/room-images/room/${roomId}`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        const result = await response.json()

        const imageList = document.getElementById('imageList');
        imageList.innerHTML = '';

        if (result.code === 200 && result.data.length > 0) {
            result.data.forEach(image => {
                let imageUrl = image.imageUrl;
                console.log('原始imagerul:', imageUrl);
                if (imageUrl.startsWith('http')) {
                    // 如果包含localhost，替换为服务器IP
                    if (imageUrl.includes('localhost:8080')) {
                        imageUrl = imageUrl.replace('http://localhost:8080', baseUrl);
                    }
                } else {
                    imageUrl = `${baseUrl}${image.imageUrl}`;
                }
                console.log('处理后imageUrl:', imageUrl); // 可验证处理结果
                const imageItem = `
                            <div class="image-item" style="border: 1px solid #ddd; padding: 10px; border-radius: 4px;">
                                <img src="${imageUrl}" style="width: 100%; height: 120px; object-fit: cover;">
                                <div style="text-align: center; margin-top: 5px;">
                                    ${image.isPrimary ? '<span style="color: green; font-size: 12px;">主图</span>' :
                        `<button onclick="setPrimaryImage(${image.id})" style="font-size: 12px; padding: 2px 6px;">设为主图</button>`}
                                    <button onclick="deleteImage(${image.id})" style="font-size: 12px; padding: 2px 6px; background: #ff4d4f; margin-left: 5px;">删除</button>
                                </div>
                            </div>
                            `;
                imageList.innerHTML += imageItem;
            });
        } else {
            imageList.innerHTML = '<p>暂无图片</p>';;
        }
    } catch (error) {
        console.error('加载图片失败：', error)
    }
}

async function uploadImage() {
    const roomId = document.getElementById('currentRoomId').value;
    const fileInput = document.getElementById('imageFile');
    const isPrimary = document.getElementById('setAsPrimary').checked;

    if (!fileInput.files[0]) {
        alert('请选择图片');
        return;
    }
    const formData = new FormData();
    formData.append('roomId', roomId);
    formData.append('image', fileInput.files[0]);
    formData.append('isPrimary', isPrimary);

    try {
        const response = await fetch(`${baseUrl}/api/room-images/upload`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`
            },
            body: formData

        });
        const result = await response.json();
        if (result.code === 200) {
            alert('图片上传成功');
            fileInput.value = '';
            document.getElementById('setAsPrimary').checked = false;
            await loadRoomImages(roomId);
        } else {
            throw new Error(result.msg);
        }
    } catch (error) {
        console.error('上传失败', error);
        alert('上传失败' + error.message);
    }
}
async function setPrimaryImage(imageId) {
    try {
        const response = await fetch(`${baseUrl}/api/room-images/${imageId}/set-primary`, {
            method: 'PUT',
            headers: { 'Authorization': `Bearer ${token}` }
        });
        const result = await response.json();
        if (result.code === 200) {
            alert('设置主图成功');
            const roomId = document.getElementById('currentRoomId').value;
            await loadRoomImages(roomId);
        } else {
            throw new Error(result.msg);
        }
    } catch (error) {
        console.error('设置主图失败', error);
        alert('设置主图失败' + error.message);
    }

}
async function deleteImage(imageId) {
    if (!confirm('确定要删除这张图片吗？')) return;

    try {
        const response = await fetch(`${baseUrl}/api/room-images/${imageId}`, {
            method: 'DELETE',
            headers: { 'Authorization': `Bearer ${token}` }
        });
        const result = await response.json();
        if (result.code === 200) {
            alert('删除成功');
            const roomId = document.getElementById('currentRoomId').value;
            await loadRoomImages(roomId);
        } else {
            throw new Error(result.msg);
        }
    } catch (error) {
        console.error('删除失败', error);
        alert('删除失败' + error.message);
    }

}

function closeImageManager() {
    document.getElementById('imageManagerModal').style.display = 'none';
}
//操作函数
function editRoom(roomId) {
    if (!checkLogin()) return;

    fetch(`${baseUrl}/api/room/detail/${roomId}`, {
        headers: { 'Authorization': `Bearer ${token}` }
    })
        .then(response => response.json())
        .then(result => {
            if (result.code === 200) {
                openRoomModal('edit', result.data);
            } else {
                throw new Error(result.msg);
            }
        })
        .catch(error => {
            console.error('获取房间信息失败:', error);
            alert('获取房间信息失败: ' + error.message);
        });
}
function addRoom() {
    openRoomModal('add');
}
function openRoomModal(mode, roomData = null) {
    const modal = document.getElementById('roomModal');
    const title = document.getElementById('modalTitle');
    const form = document.getElementById('roomForm');

    if (mode === 'add') {
        title.textContent = '新增房间';
        form.reset();
        document.getElementById('roomId').value = '';
    } else {
        title.textContent = '编辑房间';
        if (roomData) {
            document.getElementById('roomId').value = roomData.id;
            document.getElementById('roomNumber').value = roomData.roomNumber;
            document.getElementById('roomType').value = roomData.type;
            document.getElementById('roomPrice').value = roomData.price;
            document.getElementById('roomStatus').value = roomData.status;

        }
    }
    modal.style.display = 'flex';
}
function closeRoomModal() {
    document.getElementById('roomModal').style.display = 'none';
}
async function saveRoom() {
    const id = document.getElementById('roomId').value;
    const data = {
        roomNumber: document.getElementById('roomNumber').value.trim(),
        type: document.getElementById('roomType').value,
        price: parseFloat(document.getElementById('roomPrice').value),
        status: document.getElementById('roomStatus').value
    };

    if (!data.roomNumber || !data.type || !data.price) {
        alert('请填写完整信息');
        return;
    }
    try {
        let url = `${baseUrl}/api/room`;
        let method = 'POST';

        if (id) {
            data.id = parseInt(id);
            url = `${baseUrl}/api/room/${id}`;
            method = 'PUT';
        }
        const response = await fetch(
            url, {
            method: method,
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });
        const result = await response.json();
        if (result.code === 200) {
            alert('保存成功');
            closeRoomModal();
            loadRooms(); // 刷新房间列表
        } else {
            throw new Error(result.msg);
        }
    } catch (error) {
        console.error('保存房间失败:', error);
        alert('保存失败: ' + error.message);
    }
}
function deleteRoom(roomId) {
    if (confirm('确定要删除这个房间吗？')) {
        fetch(`${baseUrl}/api/room/${roomId}`, {
            method: 'DELETE',
            headers: { 'Authorization': `Bearer ${token}` }
        })
            .then(response => response.json())
            .then(result => {
                if (result.code === 200) {
                    alert('删除成功');
                    loadRooms();
                } else {
                    alert('删除失败: ' + result.msg);
                }
            });

    }
}

function cancelOrder(orderId) {
    if (confirm('确定要取消这个订单吗?')) {
        fetch(`${baseUrl}/api/order/${orderId}/forceCancel`, {
            method: 'PUT',
            headers: { 'Authorization': `Bearer ${token}` }
        })
            .then(response => response.json())
            .then(result => {
                if (result.code === 200) {
                    alert('取消成功');
                    loadOrders();
                } else {
                    alert('取消失败: ' + result.msg);
                }
            });
    }
}

function goToFront() {
    window.location.href = 'home.html';
}
function viewOrder(orderId) {
    alert('查看订单详情: ' + orderId);
}

function editUser(userId) {
    alert('编辑用户: ' + userId);
}

function deleteUser(userId) {
    if (confirm('确定要删除这个用户吗？')) {
        alert('删除用户: ' + userId);
    }
}
//页面加载时初始化
document.addEventListener('DOMContentLoaded', function () {
    if (!checkLogin()) return;
    checkAdmin();
    console.log('token:', token);
    showSection('dashboard');

    document.getElementById('back-home').addEventListener('click', function () {
        window.location.href = 'home.html';
    })
    document.querySelectorAll('.nav-items a').forEach(link => {
        link.addEventListener('click', function (e) {
            e.preventDefault();
            const sectionMap = {
                '首页': 'dashboard',
                '客房管理': 'room-management',
                '订单管理': 'order-management',
                '用户管理': 'user-management',
                '个人中心': 'profile'
            };
            const sectionName = sectionMap[this.textContent];
            if (sectionName) {
                showSection(sectionName);

                if (sectionName === 'room-management') {
                    loadRooms();
                } else if (sectionName === 'order-management') {
                    loadOrders();
                } else if (sectionName === 'user-management') {
                    loadUsers();
                }
            }
        });
    });
});