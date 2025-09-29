const express = require('express');
const cors = require('cors');
const bodyParser = require('body-parser');

const app = express();

app.use(cors());
app.use(bodyParser.json());

// 模拟数据
const users = [
  { id: 1, username: 'admin', password: 'admin', role: 'ADMIN' },
  { id: 2, username: 'user', password: '123456', role: 'USER' }
];

const rooms = [
  { id: 1, roomNumber: '101', type: '标准大床房', price: 299, status: 'AVAILABLE' },
  { id: 2, roomNumber: '102', type: '标准双床房', price: 329, status: 'AVAILABLE' },
  { id: 3, roomNumber: '201', type: '豪华套房', price: 599, status: 'BOOKED' }
];

// 登录接口
app.post('/api/auth/login', (req, res) => {
  const { username, password } = req.body;
  const user = users.find(u => u.username === username && u.password === password);

  if (user) {
    res.json({
      code: 200,
      data: {
        token: 'vercel-token-' + Date.now(),
        user: { id: user.id, username: user.username, role: user.role }
      },
      msg: '登录成功'
    });
  } else {
    res.status(400).json({
      code: 400,
      msg: '用户名或密码错误'
    });
  }
});

// 房间列表
app.get('/api/room/list', (req, res) => {
  res.json({
    code: 200,
    data: rooms,
    msg: '成功'
  });
});

// 所有用户
app.get('/api/user/all', (req, res) => {
  res.json({
    code: 200,
    data: users,
    msg: '成功'
  });
});

// 所有订单
app.get('/api/order/all', (req, res) => {
  res.json({
    code: 200,
    data: [
      { id: 1, userId: 2, roomId: 3, checkIn: '2024-01-15', checkOut: '2024-01-17', status: 'CONFIRMED' }
    ],
    msg: '成功'
  });
});

module.exports = app;