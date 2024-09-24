<template>
  <div>
    <div ref="map" class="map-container"></div>
    <el-button @click="switchLayer" class="map-button layer-button">切换地图图层</el-button>
    <el-button class="map-button heatmap-button" @click="HeatmapLayer">热力图</el-button>
    <el-button @click="refreshMap" icon="el-icon-refresh" circle class="map-button refresh-button"></el-button>
  </div>
</template>

<script>
import axios from 'axios';
import Map from 'ol/Map';
import View from 'ol/View';
import TileLayer from 'ol/layer/Tile';
import XYZ from 'ol/source/XYZ';
import { fromLonLat } from 'ol/proj';
import HeatmapLayer from 'ol/layer/Heatmap';
import VectorSource from 'ol/source/Vector';
import Feature from 'ol/Feature';
import Point from 'ol/geom/Point';
import ScaleLine from 'ol/control/ScaleLine';

export default {
  data() {
    return {
      map: null,
      heatmapLayer: null,
      heatmapVisible: false,
      currentLayerIndex: 0,
      mapLayers: [
        {
          id: 1,
          name: '高德地图新版',
          source: new XYZ({
            attributions: '地图数据 © <a href="https://amap.com">高德地图</a>',
            url: 'https://wprd0{1-4}.is.autonavi.com/appmaptile?lang=zh_cn&size=1&style=7&x={x}&y={y}&z={z}',
            wrapX: false,
          }),
        },
        {
          id: 2,
          name: '高德卫星图',
          source: new XYZ({
            attributions: '卫星影像 © <a href="https://amap.com">高德地图 - 卫星</a>',
            url: 'https://webst02.is.autonavi.com/appmaptile?style=6&x={x}&y={y}&z={z}',
          }),
        },
      ],
    };
  },
  mounted() {
    this.initMap();
    this.initializeMapAndHeatMap();
  },
  methods: {
    initMap() {
      const initialCenter = fromLonLat([116.4, 39.9]);
      this.map = new Map({
        target: this.$refs.map,
        layers: [
          new TileLayer({
            source: this.mapLayers[this.currentLayerIndex].source,
          }),
        ],
        view: new View({
          center: initialCenter,
          zoom: 5,
        }),
      });
      this.map.addControl(new ScaleLine());
    },
    refreshMap() {
      const initialCenter = fromLonLat([116.4, 39.9]);
      const view = this.map.getView();
      view.setCenter(initialCenter);
      view.setZoom(5);
    },
    async initializeMapAndHeatMap() {
      const heatMapData = await this.getHeatMapData();
      if (heatMapData) {
        this.drawHeatmap(heatMapData);
      }
    },
    async getHeatMapData() {
      try {
        const response = await axios.post(`http://localhost:9093/Node/findAllCity`);
        if (Array.isArray(response.data.object)) {
          const heatMapData = await Promise.all(response.data.object.map(async node => {
            const geoResponse = await axios.get(`https://restapi.amap.com/v3/geocode/geo?key=7b0b6af0df88479d156393c8d9fff4af&address=${encodeURIComponent(node.name)}`);
            if (Array.isArray(geoResponse.data.geocodes) && geoResponse.data.geocodes.length > 0) {
              const location = geoResponse.data.geocodes[0].location.split(',');
              return {
                lon: parseFloat(location[0]),
                lat: parseFloat(location[1]),
                count: node.count
              };
            } else {
              console.error(`错误：无法获取 ${node.name} 的经纬度信息`);
              return null;
            }
          }));
          return heatMapData.filter(data => data !== null);
        } else {
          console.error(`错误：响应数据.object不是数组`);
          return null;
        }
      } catch (error) {
        console.error(`获取热力图数据时出错:`, error);
        return null;
      }
    },
    drawHeatmap(data) {
      const features = data.map(item => {
        const coordinates = fromLonLat([item.lon, item.lat]);
        const feature = new Feature(new Point(coordinates));
        feature.set('weight', item.count); // 设置权重
        return feature;
      });
      const vectorSource = new VectorSource({ features });
      this.heatmapLayer = new HeatmapLayer({
        source: vectorSource,
        blur: 15,
        radius: 8,
        weight: function (feature) {
          return feature.get('weight');
        },
      });
      this.map.addLayer(this.heatmapLayer);
    },
    HeatmapLayer() {
      this.heatmapVisible = !this.heatmapVisible;
      this.heatmapLayer.setVisible(this.heatmapVisible); // 直接控制热力图图层可见性
    },
    switchLayer() {
      this.currentLayerIndex = (this.currentLayerIndex + 1) % this.mapLayers.length;
      this.map.getLayers().item(0).setSource(this.mapLayers[this.currentLayerIndex].source);
    },
  },
};
</script>

<style>
* {
  margin: 0;
  padding: 0;
  overflow: hidden;
}

html, body {
  height: 100%;
  width: 100%;
}

.map-container {
  width: 100vw;
  height: 100vh;
}

.map-button {
  position: absolute;
  z-index: 1000;
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 10px 15px;
  background-color: deepskyblue;
  color: #fff;
  font-size: 14px;
  white-space: nowrap;
  border: none;
}

.refresh-button {
  top: 10px;
  left: 40px;
  height: 40px;
  width: 40px;
  border-radius: 50%;
}

.layer-button {
  top: 10px;
  right: 10px;
}

.heatmap-button {
  top: 60px;
  right: 10px;
}
</style>
