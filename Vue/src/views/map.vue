<template>
  <div class="page">
    <div class="map-wrapper" :style="{ width: isCollapse ? 'calc(100% - 5vw)' : 'calc(100% - 10vw)' }">
      <div id="map"></div>
    </div>
    <div class="button-container">
      <el-button @click="clearMapAndRoute" class="refresh-button">
        <span class="el-icon-refresh"></span>
      </el-button>
      <el-button class="important-locations-button" @click="toggleImportantLocations" title="迷路频发地点">
        <span class="button-text">迷路频发地点</span>
      </el-button>
    </div>
    <div class="floating-form">
      <div class="input-row">
        <div v-if="mode === 'route'" class="search-box">
          <div class="search-group">
            <label for="start">起点：</label>
            <input type="text" id="start" v-model="startLocation" :class="{ 'empty': startLocation === '' }"/>
          </div>
          <div class="search-group">
            <label for="end">终点：</label>
            <input type="text" id="end" v-model="endLocation" :class="{ 'empty': endLocation === '' }"/>
          </div>
        </div>
        <div v-if="mode === 'mark'" class="search-box">
          <div class="search-group">
            <label for="location">地点：</label>
            <input type="text" id="location" v-model="location" :class="{ 'empty': location === '' }"/>
          </div>
        </div>
        <el-button
            :class="{ 'round': true }"
            type="primary"
            @click="mode === 'route' ? searchRoute() : markLocation()"
        >
          {{ mode === 'route' ? '规划' : '搜索' }}
        </el-button>
        <div class="mode-button">
          <el-button
              :class="{ 'round': true }"
              type="primary"
              @click="toggleMode"
          >
            切换
          </el-button>
        </div>
      </div>
      <el-dialog
          title="获取经纬度失败的地点"
          :visible.sync="showErrorDialog"
          :custom-class="'non-modal-dialog'"
          :modal="false"
      >
        <!-- 弹窗内容容器 -->
        <ul>
          <li v-for="(errorLocation, index) in errorLocations" :key="index">
            {{ errorLocation }}
          </li>
        </ul>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import L from 'leaflet';
import 'leaflet/dist/leaflet.css'; // Leaflet 样式文件
import 'leaflet-routing-machine/dist/leaflet-routing-machine.css'; // Leaflet Routing Machine 样式文件
import 'leaflet-routing-machine';
import 'leaflet-control-geocoder/dist/Control.Geocoder'; // Leaflet Control Geocoder 插件
import markerIcon from 'leaflet/dist/images/marker-icon.png';
import markerShadow from 'leaflet/dist/images/marker-shadow.png';
import axios from 'axios';

export default {
  name: 'Page',
  data() {
    return {
      startLocation: '',
      endLocation: '',
      location: '',
      map: null,
      markerIcon: null,
      routingControl: null,
      markers: [], // 保存标记点的数组
      mode: 'route', // 初始模式为路径规划
      isCollapse: false,
      showImportantLocations: false,
      importantLocations: [],
      errorLocations: [], // 用于存储获取经纬度失败的地点信息
      showErrorDialog: false
    };
  },
  methods: {
    // 新方法，将地图重新定位到用户的当前位置
    repositionToUserLocation() {
      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(position => {
          const {latitude, longitude} = position.coords;
          const userLocation = [latitude, longitude];
          // 将地图中心设置为用户的当前位置
          this.map.setView(userLocation, 12); // 12 是缩放级别，根据需要进行调整
          // 关闭重要地点开关
          this.showImportantLocations = false;
        });
      }
    },
    toggleImportantLocations() {
      this.showImportantLocations = true;
      // 设置地图缩放级别为5
      this.map.setZoom(5);
      if (this.showImportantLocations) {
        // 先清除已有的标记
        this.markers.forEach(marker => this.map.removeLayer(marker));
        this.markers = [];

        axios
            .post('http://localhost:9093/Node/findAllLocation')
            .then(response => {
              console.log('响应:', response.data);
              if (Array.isArray(response.data.object)) {
                this.importantLocations = response.data.object.map(node => {
                  return {
                    name: node.name,
                    number: node.count,
                    address: node.name
                  };
                });
                console.log('重要地点:', this.importantLocations); // 记录重要地点
                this.addMarkers();
              }
            })
            .catch(error => {
              console.error('获取地点数据时出错:', error);
            });
      } else {
        // 清除标记
        this.markers.forEach(marker => this.map.removeLayer(marker));
        this.markers = [];
      }
    },
    addMarkers() {
      console.log('添加标记...');
      const promises = this.importantLocations.map(location => {
        return this.getLocation(location.address)
            .then(latlng => {
              const customIcon = L.icon({
                iconUrl: markerIcon,
                shadowUrl: markerShadow,
                iconSize: [25, 41],
                iconAnchor: [12.5, 41],
              });
              const popupText = `地点名: ${location.name}<br>经度: ${latlng[1]}<br>纬度: ${latlng[0]}<br>出现次数: ${location.number}<br>`;
              const marker = L.marker(latlng, { icon: customIcon })
                  .addTo(this.map)
                  .bindPopup(popupText)
                  .openPopup();
              marker.on('contextmenu', (e) => {
                this.removeMarker(marker);
              });

              this.markers.push(marker); // 将标记添加到this.markers数组
            })
            .catch(error => {
              console.error(error);
              this.errorLocations.push(location.name);
            });
      });

      Promise.all(promises)
          .then(() => {
            // 启用地图的自动跳转
            this.map.dragging.enable();
          });
    },
    getLocation(address) {
      return new Promise((resolve, reject) => {
        const url = `https://restapi.amap.com/v3/geocode/geo?key=7b0b6af0df88479d156393c8d9fff4af&address=${encodeURIComponent(
            address
        )}`;
        axios
            .get(url)
            .then(response => {
              const data = response.data;
              if (data.status === '1' && data.count > 0) {
                const location = data.geocodes[0].location;
                const [lon, lat] = location.split(',');
                // 打印经纬度到控制台
                console.log(`经度: ${parseFloat(lon)}, 纬度: ${parseFloat(lat)}`);
                resolve([parseFloat(lat), parseFloat(lon)]);
              } else {
                reject(new Error(`获取${address}的经纬度失败！`));
              }
            })
            .catch(error => {
              reject(error);
            });
      });
    },
    searchRoute() {
      if (this.mode === 'route') {
        // 执行路径规划操作
        const geocoder = new L.Control.Geocoder.Nominatim();
        geocoder.geocode(this.startLocation, (startResults) => {
          if (startResults && startResults.length > 0) {
            const startLatLng = startResults[0].center;

            geocoder.geocode(this.endLocation, (endResults) => {
              if (endResults && endResults.length > 0) {
                const endLatLng = endResults[0].center;

                if (this.routingControl) {
                  this.map.removeControl(this.routingControl);
                }

                this.routingControl = L.Routing.control({
                  waypoints: [L.latLng(startLatLng), L.latLng(endLatLng)],
                  routeWhileDragging: true,
                  createMarker: () => null // 这里使用了一个空白的函数，不创建起点和终点的图标
                }).addTo(this.map);

                this.addMarker(startLatLng, this.startLocation);
                this.addMarker(endLatLng, this.endLocation);

                // 调用高德API路径规划服务
                const key = '7b0b6af0df88479d156393c8d9fff4af';
                const geocodeUrl = `https://restapi.amap.com/v3/geocode/regeo?key=${key}&location=${startLatLng.lng},${startLatLng.lat}`;

                fetch(geocodeUrl)
                    .then(response => response.json())
                    .then(data => {
                      console.log("经度: " + startLatLng.lng);
                      console.log("纬度: " + startLatLng.lat);
                      const cityCode = data.regeocode.addressComponent.adcode;
                      const weatherUrl = `https://restapi.amap.com/v3/weather/weatherInfo?key=${key}&city=${cityCode}`;
                      const trafficUrl = `https://restapi.amap.com/v3/traffic/status/circle?location=116.3057764,39.98641364&radius=1500&key=${key}`;

                      fetch(weatherUrl)
                          .then(response => response.json())
                          .then(data => {
                            console.log(data)
                            // 解析天气信息
                            const temperature = data.lives[0].temperature;
                            const weather = data.lives[0].weather;

                            // 创建带白色底框的弹框显示起始点天气信息
                            const tipBox = L.control({
                              position: 'topright'
                            });

                            tipBox.onAdd = function(map) {
                              const div = L.DomUtil.create('div', 'tip-box');
                              div.style.backgroundColor = '#fff';
                              div.style.padding = '10px';
                              div.style.border = '1px solid #ccc';
                              div.style.borderRadius = '5px';
                              div.style.width = '200px';

                              const title = L.DomUtil.create('h4', '', div);
                              title.textContent = '沿途天气预报';
                              title.style.fontSize = '15px';

                              const tempText = L.DomUtil.create('p', '', div);
                              tempText.textContent = `温度: ${temperature}℃`;

                              const weatherText = L.DomUtil.create('p', '', div);
                              weatherText.textContent = `天气: ${weather}`;

                              // 判断特定经纬度，添加额外的信息
                              if (startLatLng.lng === 114.29383458206709 && startLatLng.lat === 30.610614599999998) {
                                const additionalText = L.DomUtil.create('p', '', div);
                                additionalText.innerHTML = `
                            1.驾驶模式下，青年路附近立交容易迷路，建议进行避让。<br>
                            2.驾驶模式下，雄楚大道立交桥容易迷路，建议进行避让。<br>
                            3.若为暴雨天气，协和医院十字路口容易迷路，建议避让。`;
                              }

                              return div;
                            };

                            // 清除先前的弹框，如果有的话
                            if (this.tipBox) {
                              this.map.removeControl(this.tipBox);
                            }
                            this.tipBox = tipBox;

                            // 将弹框添加到地图上
                            tipBox.addTo(this.map);
                          });
                    });
              }
            });
          }
        });
      }
    },
    markLocation() {
      if (this.mode === 'mark') {
        // 执行地点标注操作
        const geocoder = new L.Control.Geocoder.Nominatim();
        // 根据输入的地点进行标注
        geocoder.geocode(this.location, (results) => {
          if (results && results.length > 0) {
            const latLng = results[0].center;
            const popupText = `地点: ${this.location}<br>经度: ${latLng.lat}<br>纬度: ${latLng.lng}`;
            if (this.routingControl) {
              this.map.removeControl(this.routingControl);
            }
            this.routingControl = null;
            this.addMarker(latLng, popupText);
            this.map.flyTo(latLng, 13);
          }
        });
      }
    },
    addMarker(latLng, popupText) {
      const marker = L.marker(latLng, {icon: this.markerIcon}).addTo(this.map).bindPopup(popupText);
      marker.on('contextmenu', (e) => {
        this.removeMarker(marker);
      });
      this.markers.push(marker);
    },
    removeMarker(marker) {
      this.map.removeLayer(marker);
      const index = this.markers.indexOf(marker);
      if (index !== -1) {
        this.markers.splice(index, 1);
      }
    },
    toggleMode() {
      const tipBox = document.querySelector('.tip-box');
      if (this.mode === 'route') {
        this.mode = 'mark';
        tipBox.remove();
        this.clearMap();
      } else {
        this.mode = 'route';
        this.clearMap();
      }
    },
    clearMapAndRoute() {
      this.startLocation = '';
      this.endLocation = '';
      this.location = '';
      this.clearMap();
      this.clearRoute();
      const tipBox = document.querySelector('.tip-box');
      tipBox.remove();
      // 将地图重新定位到用户的当前位置
      this.repositionToUserLocation();
    },
    clearRoute() {
      if (this.routingControl) {
        this.map.removeControl(this.routingControl);
        this.routingControl = null;
      }
    },
    clearMap() {
      if (this.routingControl) {
        this.map.removeControl(this.routingControl);
        this.routingControl = null;
      }
      this.markers.forEach(marker => this.map.removeLayer(marker));
      this.markers = [];
    }
  },
  mounted() {
    this.map = L.map('map').setView([30.5928, 114.3055], 13);
    this.markerIcon = L.icon({
      iconUrl: markerIcon,
      shadowUrl: markerShadow,
      iconSize: [25, 41],
      iconAnchor: [12, 41]
    });
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors'
    }).addTo(this.map);
    this.$nextTick(() => {
      this.map.invalidateSize();
    });
  }
};
</script>

<style>
.page {
  position: relative;
  display: flex;
  height: 100vh;
}

/* 按钮容器样式 */
.button-container {
  position: absolute;
  bottom: 20px; /* 距离底部的距离 */
  left: 20px; /* 距离左侧的距离 */
  z-index: 1000; /* 确保按钮位于最上层 */
  display: flex;
  align-items: center; /* 垂直居中对齐 */
}

/* 刷新按钮样式 */
.refresh-button {
  background-color: white; /* 设置为白色背景 */
  border: none;
  font-size: 18px;
  font-weight: bold;
  color: black; /* 黑色字体颜色 */
  margin-right: 10px; /* 与迷路地点按钮留出一定间隔 */
  transition: transform 0.2s ease; /* 添加悬停动画效果 */
  height: 40px; /* 设置按钮高度与迷路地点按钮相等 */
  width: 40px; /* 设置按钮宽度 */
  border-radius: 5px; /* 圆角矩形按钮 */
  display: flex;
  justify-content: center; /* 水平居中对齐 */
  align-items: center; /* 垂直居中对齐 */
  padding: 0; /* 移除默认内边距 */
}

.refresh-button i {
  font-size: 18px; /* 刷新图标大小 */
}

.refresh-button:hover {
  color: darkgray;
  transform: scale(1.1); /* 悬停时稍微放大一圈 */
}

/* 迷路地点频发按钮样式 */
.important-locations-button {
  background-color: white;
  border: none;
  font-size: 16px;
  font-weight: bold;
  color: black;
  border-radius: 5px; /* 圆角矩形按钮 */
  padding: 8px 16px; /* 按钮内边距 */
  margin-right: 10px; /* 与刷新按钮留出一定间隔 */
  transition: transform 0.2s ease; /* 添加悬停动画效果 */
  height: 40px; /* 设置按钮高度与刷新按钮相等 */
  line-height: 40px; /* 垂直居中对齐 */
  display: flex;
  align-items: center; /* 垂直居中对齐 */
}

.important-locations-button .button-text {
  flex-grow: 1; /* 让文本占据剩余空间 */
  text-align: center; /* 文本居中对齐 */
}

.important-locations-button:hover {
  transform: scale(1.1); /* 悬停时稍微放大一圈 */
}

/* 全局样式，将模态对话框的遮罩层背景色设置为透明 */
.el-dialog__wrapper.is-open {
  background-color: transparent !important;
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
}

.floating-form {
  position: fixed;
  bottom: 10px;
  right: 9px;
  background-color: white;
  padding: 15px;
  border-radius: 5px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
  z-index: 1000;
}

.input-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.input-row input {
  flex-grow: 1;
}

/* 修改上下边距 */
.input-row {
  display: flex;
  flex-wrap: wrap; /* 允许元素换行 */
  align-items: center; /* 垂直居中对齐 */
  backdrop-filter: blur(10px); /* 增加虚化程度 */
  background-color: rgba(255, 255, 255, 0.5); /* 背景透明度调整 */
  padding: 10px 10px; /* 缩短上下边距 */
}

/* 输入框未输入时的样式 */
input.empty {
  color: gray; /* 文本颜色变灰 */
}

/* 圆形按钮样式 */
.round.el-button {
  width: 50px; /* 圆形按钮的宽度 */
  height: 50px; /* 圆形按钮的高度 */
  font-size: 15px; /* 字体大小 */
  border-radius: 50%; /* 圆形按钮的圆角半径 */
  padding: 0; /* 移除默认内边距 */
  margin-left: 10px; /* 添加左侧间距 */
  background-color: dodgerblue; /* 正常状态下的背景颜色 */
  color: white; /* 正常状态下的文字颜色 */
}

.round.el-button:hover {
  background-color: mediumslateblue; /* 鼠标悬停状态下的背景颜色 */
}
</style>