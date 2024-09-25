<template>
  <div id="mapContainer" style="width: 100%; height: 100%;"></div>
</template>

<script>
import * as echarts from 'echarts';
export default {
  name: 'MapComponent',
  data() {
    return {
      china: [
        {name: '河北', value: 108},
        {name: '山西', value: 65},
        {name: '上海', value: 373},
        {name: '广西', value: 71},
        {name: '广东', value: 436},
        {name: '浙江', value: 222},
        {name: '陕西', value: 132},
        {name: '湖北', value: 200},
        {name: '江西', value: 49},
        {name: '甘肃', value: 32},
        {name: '江苏', value: 240},
        {name: '辽宁', value: 67},
        {name: '安徽', value: 146},
        {name: '海南', value: 34},
        {name: '河南', value: 227},
        {name: '山东', value: 263},
        {name: '福建', value: 123},
        {name: '四川', value: 198},
        {name: '重庆', value: 297},
        {name: '吉林', value: 41},
        {name: '云南', value: 94},
        {name: '北京', value: 345},
        {name: '新疆', value: 35},
        {name: '宁夏', value: 23},
        {name: '香港', value: 32},
        {name: '天津', value: 39},
        {name: '湖南', value: 170},
        {name: '黑龙江', value: 35},
        {name: '内蒙古', value: 25},
        {name: '贵州', value: 87},
        {name: '青海', value: 8},
        {name: '西藏', value: 4},
        {name: '台湾', value: 7},
        {name: '澳门', value: 11}
      ]
    }
  },
  mounted() {
    this.initMap();
  },
  methods: {
    initMap() {
      const chart = echarts.init(document.getElementById('mapContainer'));
      echarts.registerMap('china', require('@/assets/china.json'));
      const option = {
        tooltip: {},
        visualMap: {
          min: 4,
          max: 440,
          splitNumber: 5,
          inRange: {
            color: ['#d94e5d', '#eac736', '#50a3ba'].reverse()
          },
          textStyle: {
            color: '#fff'
          }
        },
        series: [
          {
            name: '中国省市微博迷路信息地图',
            type: 'map',
            map: 'china',
            roam: true,
            itemStyle: {
              emphasis: {
                areaColor: "grey"
              }
            },
            label: {
              show: true
            },
            data: this.china
          }
        ]
      };
      chart.setOption(option);
    }
  }
};
</script>