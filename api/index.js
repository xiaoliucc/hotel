// api/index.js
const express = require('express');
const cors = require('cors');
const app = express();

app.use(cors());
app.use(express.json());

// 模拟数据
const users = [...];
const rooms = [...];
const orders = [...];

// 接口
app.post('/api/user/login', (req, res) => { ... });
app.get('/api/room/list', (req, res) => { ... });
app.get('/api/order/all', (req, res) => { ... });

module.exports = app;