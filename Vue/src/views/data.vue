<template>
  <div class="container">
    <!-- 第一行图表容器 -->
    <div class="chart-row">
      <div class="chart-item">
        <div id="pieChart" class="chart chart-move-down"></div>
      </div>
      <div class="chart-item">
        <div id="barChart" class="chart"></div>
      </div>
    </div>
    <!-- 第二行图表容器 -->
    <div class="chart-row">
      <div class="chart-item">
        <div id="donutChart" class="chart chart-move-down"></div>
      </div>
      <div class="chart-item">
        <div id="lineChart" class="chart"></div>
      </div>
    </div>
    <div class="sidebar">
      <el-menu
          default-active="province"
          class="sidebar-menu"
          background-color="skyblue"
          text-color="#333"
          unique-opened
      >
        <el-menu-item index="Province" @click="fetchNodeDataByType('Province')">省</el-menu-item>
        <el-menu-item index="City" @click="fetchNodeDataByType('City')">市</el-menu-item>
        <el-menu-item index="Area" @click="fetchNodeDataByType('Area')">区/县</el-menu-item>
        <el-menu-item index="Place" @click="fetchNodeDataByType('Location')">迷路地点</el-menu-item>
        <el-menu-item index="POI" @click="fetchNodeDataByType('POI')">POI类型</el-menu-item>
        <el-menu-item index="Time" @click="fetchNodeDataByType('Time')">迷路时间</el-menu-item>
        <el-menu-item index="Weather" @click="fetchNodeDataByType('Weather')">迷路天气</el-menu-item>
        <el-menu-item index="Person" @click="fetchNodeDataByType('Person')">迷路人物</el-menu-item>
        <el-menu-item index="TripMode" @click="fetchNodeDataByType('TripMode')">出行方式</el-menu-item>
        <el-menu-item index="Cause" @click="fetchNodeDataByType('Reason')">迷路原因</el-menu-item>
      </el-menu>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts';
import axios from "axios";

export default {
  data() {
    return {
      pieChart: null,
      barChart: null,
      donutChart: null,
      lineChart: null, // 新添加的折线图实例
      Data: []
    };
  },
  mounted() {
    this.initCharts();
  },
  methods: {
    fetchNodeDataByType(nodeType) {
      this.Data = [];
      this.activeTab = nodeType; // 设置activeTab为当前选中的标签
      const endpoint = `findAll${nodeType}`;
      axios.post(`http://localhost:9093/Node/${endpoint}`)
          .then(response => {
            // 确保 response.data 是一个数组
            if (Array.isArray(response.data.object)) {
              // 迭代新数据并将其追加到this.customNodes中
              response.data.object.forEach(node => {
                const newNode = {
                  value: node.count,
                  name: node.name
                };
                this.Data.push(newNode);
              });
              // 打印格式化后的数据
              console.log('格式化后的数据:', this.Data);
              // 成功获取数据后，更新所有图表
              this.updateData();
            } else {
              // 处理错误，如果 response.data.object 不是数组
              console.error(`错误：响应数据.object不是数组`);
            }
          })
          .catch(error => {
            // 处理错误
            console.error(`获取${nodeType}节点数据时出错:`, error);
          });
    },
    initCharts() {
      this.pieChart = echarts.init(document.getElementById('pieChart'));
      this.barChart = echarts.init(document.getElementById('barChart'));
      this.donutChart = echarts.init(document.getElementById('donutChart'));
      this.lineChart = echarts.init(document.getElementById('lineChart')); // 初始化折线图
      this.updateData();
    },
    updateData() {
      let data = this.Data.map(item => ({
        value: item.value,
        name: item.name
      }));
      this.pieChart.setOption({
        series: [
          {
            type: 'pie',
            data: data,
          },
        ],
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c} ({d}%)',
        },
        toolbox: {
          feature: {
            saveAsImage: {},
          },
        },
      });
      this.barChart.setOption({
        xAxis: {
          data: data.map(item => item.name),
        },
        yAxis: {},
        series: [
          {
            type: 'bar',
            data: data,
          },
        ],
        tooltip: {
          trigger: 'axis',
          formatter: '{b}: {c}',
        },
        toolbox: {
          feature: {
            saveAsImage: {},
          },
        },
        dataZoom: [
          {
            type: 'slider', // 添加滑动条
            show: true,
            start: 0, // 滚动条开始位置（0%）
            end: 100, // 滚动条结束位置（100%）
          },
        ],
      });
      this.donutChart.setOption({
        series: [
          {
            type: 'pie',
            radius: ['50%', '70%'],
            data: data,
          },
        ],
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c} ({d}%)',
        },
        toolbox: {
          feature: {
            saveAsImage: {},
          },
        },
      });
      this.lineChart.setOption({
        xAxis: {
          data: data.map(item => item.name),
        },
        yAxis: {},
        series: [
          {
            type: 'line',
            data: data.map(item => item.value),
            name: '折线图示例',
            symbol: 'circle',
            smooth: true,
          },
        ],
        tooltip: {
          trigger: 'axis',
          formatter: '{b}: {c}',
        },
        toolbox: {
          feature: {
            saveAsImage: {},
          },
        },
        dataZoom: [
          {
            type: 'slider', // 添加滑动条
            show: true,
            start: 0, // 滚动条开始位置（0%）
            end: 100, // 滚动条结束位置（100%）
          },
        ],
      });
    }
  },
};
</script>

<style>
.chart-move-down {
  margin-top: 45px;
}

.container {
  display: flex;
  min-height: 100vh;
  justify-content: space-between;
  align-items: flex-start;
  background-color: whitesmoke;
}

.chart-row {
  flex: 1;
  display: flex;
  flex-direction: column; /* 让两行图表垂直排列 */
  margin-right: 10px; /* 调整两行图表之间的间距 */
}

.chart-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 20px; /* 调整每个图表容器之间的间距 */
  width: 500px; /* 设置图表容器的宽度为50% */
  height: 400px;
  padding: 10px; /* 添加内边距来留出一段距离 */
}

.chart-item > .chart {
  width: 100%;
  height: 300px;
}

.sidebar {
  width: 100px;
  height: 100%;
  position: fixed;
  top: 0;
  right: 0;
  border-radius: 10px;
}

.sidebar-menu {
  width: 100%;
  height: 100%;
}
</style>