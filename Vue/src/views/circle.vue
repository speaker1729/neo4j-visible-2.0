<template>
  <div class="page">
    <div class="map-wrapper">
      <div id="map"></div>
    </div>
    <el-button @click="refreshMap" class="refresh-button">
      <span class="el-icon-refresh"></span>
    </el-button>
    <div class="search-box">
      <input v-model="searchQuery" placeholder="请输入城市名...">
      <button @click="searchData">搜索</button>
    </div>
  </div>
</template>

<script>
import * as L from "leaflet";
import "leaflet/dist/leaflet.css";
import "@geoman-io/leaflet-geoman-free";
import "@geoman-io/leaflet-geoman-free/dist/leaflet-geoman.css";
import markerIcon from 'leaflet/dist/images/marker-icon.png';
import markerShadow from 'leaflet/dist/images/marker-shadow.png';
import axios from "axios";

export default {
  data() {
    return {
      markerIcon: null,
      locations: [],
      city: [],
      searchQuery: "",
    };
  },
  mounted() {
    this.getData();
    // 创建地图容器
    const map = L.map('map').setView([39.9, 116.4], 10);
    this.map = map;
    this.markerIcon = L.icon({
      iconUrl: markerIcon,
      shadowUrl: markerShadow,
      iconSize: [25, 41],
      iconAnchor: [12, 41]
    });
    // 添加瓦片地图（这里以OpenStreetMap为例）
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; OpenStreetMap contributors'
    }).addTo(map);
    // 初始化Leaflet-Geoman插件，只保留画圆的控件
    map.pm.addControls({
      position: 'topleft',
      drawCircleMarker: false,
      drawPolyline: false,
      drawRectangle: false,
      drawPolygon: false,
      drawMarker: false,
      drawText: false,
      editMode: false,
      removalMode: false,
      cutPolygon:false,
      dragMode:false,
      rotateMode:false,
    });
    // 在绘制结束后触发事件
    map.on('pm:create', event => {
      const layer = event.layer;
      this.markLocationInCircle(layer);
    });
  },
  methods: {
    searchData() {
      if (this.searchQuery === '') {
        // 如果搜索框为空，弹出警告
        alert('请输入搜索内容');
        return;
      }
      // 使用高德API将城市名称转换为经纬度
      this.geocode(this.searchQuery)
          .then(data => {
            const latlng = L.latLng(data.lat, data.lon);
            // 定位地图到目标城市
            this.map.setView(latlng, 10);
          })
          .catch(error => {
            console.error(error);
            alert('无法找到目标城市的经纬度');
          });
      axios
          .post(`http://localhost:9093/Node/findLocationByCity?cityName=${this.searchQuery}`)
          .then((response) => {
            console.log('响应:', response.data);
            if (Array.isArray(response.data.object) && response.data.object.length > 0) {
              this.city = response.data.object.map((node) => {
                return {
                  name: node.name,
                  query: node.name,
                  number: node.count
                };
              });
              console.log('城市结果:', this.city); // 记录城市结果
              this.city.forEach(location => {
                this.geocode(this.searchQuery + location.query) // 修改此行
                    .then(data => {
                      const latlng = L.latLng(data.lat, data.lon);
                      this.addMarkerToMap(latlng, location);
                    })
                    .catch(error => {
                      console.error(error);
                    });
              });
            } else {
              // 如果后端返回数据为空，弹出提示
              alert('没有找到相关数据');
            }
          })
          .catch((error) => {
            console.error('获取城市数据时出错:', error);
          });
    },
    getData() {
      axios
          .post('http://localhost:9093/Node/findAllLocation')
          .then(response => {
            if (Array.isArray(response.data.object)) {
              this.locations = response.data.object.map(node => {
                return {
                  name: node.name,
                  query: node.name,
                  number: node.count
                };
              });
            }
          })
          .catch(error => {
            console.error('获取地点数据时出错:', error);
          });
    },
    refreshMap() {
      // 从地图中移除所有图层和标记
      this.map.eachLayer((layer) => {
        if (layer !== this.map.pm.globalEditLayer) {
          this.map.removeLayer(layer);
        }
      });
      // 重新添加瓦片图层以刷新地图
      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; OpenStreetMap 贡献者',
      }).addTo(this.map);
    },
    markLocationInCircle(circle) {
      const circleBounds = circle.getBounds();
      this.locations.forEach(location => {
        this.geocode(location.query)
            .then(data => {
              const latlng = L.latLng(data.lat, data.lon);
              if (circleBounds.contains(latlng)) {
                // 传递包含 name 和 number 属性的对象
                this.addMarkerToMap(latlng, {
                  name: location.name,
                  number: location.count
                });
              }
            })
            .catch(error => {
              console.error(error);
            });
      });
    },
    addMarkerToMap(latlng, location) {
      const marker = L.marker(latlng, { icon: this.markerIcon }).addTo(this.map);
      const popupContent = `<b>名称:</b> ${location.name}<br><b>出现次数:</b> ${location.number}<br><b>经度:</b> ${latlng.lat}<br><b>纬度:</b> ${latlng.lng}`;
      marker.bindPopup(popupContent);
      marker.on('contextmenu', (e) => {
        this.removeMarker(marker);
      });
    },
    geocode(query) {
      const requestUrl = `https://restapi.amap.com/v3/geocode/geo?key=7b0b6af0df88479d156393c8d9fff4af&address=${query}`;
      return new Promise((resolve, reject) => {
        fetch(requestUrl)
            .then(response => response.json())
            .then(data => {
              const location = data.geocodes[0].location.split(",");
              const lon = parseFloat(location[0]);
              const lat = parseFloat(location[1]);
              resolve({lon, lat});
            })
            .catch(error => {
              reject(error);
            });
      });
    },
  },
}
</script>

<style>
.page {
  position: relative;
  display: flex;
  height: 100vh;
}

.map-wrapper {
  flex-grow: 1;
  position: relative;
  display: flex; /* 使用 flex 布局 */
  flex-direction: column; /* 设置为纵向布局，以便调整高度 */
  justify-content: flex-start; /* 顶部对齐，可以根据需要调整 */
}

#map {
  flex-grow: 1; /* 填充剩余的垂直空间 */
  height: 100%;
  width: 100%;
  z-index: 10;
}

/* 刷新按钮样式 */
.refresh-button {
  background-color: white; /* 设置为白色背景 */
  border: none;
  font-size: 18px;
  font-weight: bold;
  color: black; /* 黑色字体颜色 */
  margin: 10px; /* 添加外边距，调整按钮位置 */
  transition: transform 0.2s ease; /* 添加悬停动画效果 */
  height: 40px; /* 设置按钮高度与迷路地点按钮相等 */
  width: 40px; /* 设置按钮宽度，使其成为正方形 */
  border-radius: 5px; /* 圆角矩形按钮 */
  display: flex;
  justify-content: center; /* 水平居中对齐 */
  align-items: center; /* 垂直居中对齐 */
  padding: 0; /* 移除默认内边距 */
  position: absolute; /* 将按钮定位为绝对位置 */
  bottom: 10px; /* 距离底部的距离 */
  left: 10px; /* 距离左侧的距离 */
  z-index: 99;
}

.refresh-button i {
  font-size: 18px; /* 刷新图标大小 */
}

.refresh-button:hover {
  color: darkgray;
  transform: scale(1.1); /* 悬停时稍微放大一圈 */
}

.search-box {
  position: absolute;
  bottom: 10px;
  right: 10px;
  display: flex;
  align-items: center;
  background-color: white;
  border: 1px solid #ccc;
  border-radius: 4px;
  padding: 5px;
  box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.2);
  z-index: 99;
}

.search-box input {
  border: none;
  padding: 5px;
  margin-right: 5px;
  border-radius: 4px;
}

.search-box button {
  background-color: #007BFF;
  color: white;
  border: none;
  border-radius: 4px;
  padding: 5px 10px;
  cursor: pointer;
}

.search-box button:hover {
  background-color: #0056b3;
}
</style>