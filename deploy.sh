#!/bin/bash
echo "🚀 开始部署酒店管理系统..."

# 创建部署目录
mkdir -p .vercel/output/static

# 复制所有HTML文件到Vercel输出目录
echo "📁 复制静态文件..."
cp -r hotel/src/main/resources/static/* .vercel/output/static/

# 复制API文件
echo "🔧 复制API文件..."
cp -r api .vercel/output/

echo "✅ 部署文件准备完成"
echo "📄 文件列表:"
ls -la .vercel/output/static/