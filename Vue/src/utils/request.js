// 导入Axios库
import axios from 'axios';

// 创建一个Axios实例，配置基础URL
const api = axios.create({
    baseURL: 'http://localhost:9093', // 后端API的地址和端口
    timeout: 5000, // 超时时间（可选）
});