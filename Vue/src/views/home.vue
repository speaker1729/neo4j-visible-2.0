<template>
  <div class="whole">
    <div id="control-panel" :class="{ 'collapsed': isCollapsed }">
      <div class="spacer">
        <span class="text" v-if="!isCollapsed">可视化知识图谱</span>
        <el-button
            class="edit-button"
            style="position: absolute; top: 10px; right: 10px;"
            type="text"
            @click="showPromptDialog"
        >
          <i
              class="el-icon-edit"
              :style="{ fontSize: '18px'}"
          ></i>
        </el-button>
      </div>

      <div class="control-buttons">
        <el-button class="search-button" @click="applySearch">
          搜索
        </el-button>
        <el-button class="reset-button" @click="resetSearch">
          重置
        </el-button>
      </div>

      <div class="control-item" v-for="column in columns" :key="column.name">
        <label class="control-label">
          <div class="label-switch-container">
            <span :style="{ color: column.switchActiveColor }">{{ column.label }}</span>
            <!-- 绑定到相应节点类别的 isVisible 属性 -->
            <el-switch v-model="column.isVisible" :active-color="column.switchActiveColor"
                       :inactive-color="column.switchInactiveColor"></el-switch>
          </div>
        </label>
        <input class="control-input" type="text" v-model="column.searchQuery" placeholder="搜索..."/>
      </div>
    </div>
    <div class="map-container" id="my-g" ref="map" @mousedown="onMouseDown">
      <div id="graph">
      </div>
    </div>
    <div class="node-info-popup" v-if="selectedNodeInfo">
      <div>ID（编码）: {{ selectedNodeInfo.id }}</div>
      <div>Label（类型）: {{ selectedNodeInfo.label }}</div>
      <div>Number（出现次数）: {{ selectedNodeInfo.number }}</div>
      <div>Name（名称）: {{ selectedNodeInfo.name }}</div>
    </div>
    <el-dialog
        title="场景说明"
        :visible.sync="dialogVisible"
        :modal="false"
        :modal-append-to-body="false"
        :close-on-click-modal="false"
        :lock-scroll="false"
        :append-to-body="true"
        :custom-class="'custom-dialog-style'"
    >
      <div v-html="Content" class="custom-dialog-content"></div>
    </el-dialog>
    <el-button class="fixed-button" @click="confirmDialog" style="background-color: deepskyblue;">查看3D模型</el-button>
  </div>
</template>

<script>
import * as d3 from 'd3';
import axios from 'axios';

export default {
  data() {
    return {
      Content: '加载中(ﾟДﾟ≡ﾟдﾟ)',
      inputAI: [],
      translateX: 0,
      translateY: 0,
      node1: '',
      mouseX: 0,
      mouseY: 0,
      mapX: 0,
      mapY: 0,
      dialogVisible: false,
      isDragging: false,
      driver: null,
      session: null,
      isCollapsed: false,
      selectedNodeInfo: null, // 用于存储选中节点的信息
      columns: [
        {
          name: 'Province',
          label: '省',
          isVisible: false,
          searchQuery: '',
          switchActiveColor: '#3ca8cc',
          switchInactiveColor: '#ccc'
        },
        {
          name: 'City',
          label: '市',
          isVisible: false,
          searchQuery: '',
          switchActiveColor: '#c9b051',
          switchInactiveColor: '#ccc'
        },
        {
          name: 'Area',
          label: '区/县',
          isVisible: false,
          searchQuery: '',
          switchActiveColor: '#49a964',
          switchInactiveColor: '#ccc'
        },
        {
          name: 'Location',
          label: '迷路地点',
          isVisible: false,
          searchQuery: '',
          switchActiveColor: '#fc7349',
          switchInactiveColor: '#ccc'
        },
        {
          name: 'POI',
          label: 'POI类型',
          isVisible: false,
          searchQuery: '',
          switchActiveColor: '#7fad7b',
          switchInactiveColor: '#ccc'
        },
        {
          name: 'Time',
          label: '迷路时间',
          isVisible: false,
          searchQuery: '',
          switchActiveColor: '#6f95d0',
          switchInactiveColor: '#ccc'
        },
        {
          name: 'Weather',
          label: '迷路天气',
          isVisible: false,
          searchQuery: '',
          switchActiveColor: '#e8a75a',
          switchInactiveColor: '#ccc'
        },
        {
          name: 'Person',
          label: '迷路人物',
          isVisible: false,
          searchQuery: '',
          switchActiveColor: '#c280c2',
          switchInactiveColor: '#ccc'
        },
        {
          name: 'TripMode',
          label: '出行方式',
          isVisible: false,
          searchQuery: '',
          switchActiveColor: '#be7843',
          switchInactiveColor: '#ccc'
        },
        {
          name: 'Reason',
          label: '迷路原因',
          isVisible: false,
          searchQuery: '',
          switchActiveColor: '#8377b6',
          switchInactiveColor: '#ccc'
        }
      ],
      SearchKey: [],
      customNodes: [],
      customRelationships: [],
    };
  },
  async mounted() {
    const relationTypes = ['located_in', 'lost_time', 'lost_POI', 'lost_weather', 'lost_person', 'lost_reason', 'by'];
    for (let i = 0; i < relationTypes.length; i++) {
      const relationType = relationTypes[i];
      await this.fetchRelationDataByType(relationType);
    }
    const nodeTypes = ['Province', 'City', 'Area', 'Location', 'POI', 'Time', 'Weather', 'Person', 'TripMode', 'Reason'];
    for (let i = 0; i < nodeTypes.length; i++) {
      const nodeType = nodeTypes[i];
      await this.fetchNodeDataByType(nodeType);
    }
    this.SearchKey = this.customNodes;
  },
  methods: {
    confirmDialog() {
      window.location.href = 'https://app.graphxr.cn/p/6642d5029d07497b78b872f9/3D%E7%9F%A5%E8%AF%86%E5%9B%BE%E8%B0%B1';
    },
    async callAI() {
      if (this.inputAI.length === 0) {
        this.Content = '很遗憾，您所提到的元素没有强相关性，或许您可以尝试其他方式搜索。';
        return;
      }
      const axios = require('axios');
      try {
        const tokenUrl = 'http://localhost:9093/proxyBaiduToken';
        const tokenResponse = await axios.get(tokenUrl);
        const accessToken = tokenResponse.data.access_token; // 提取 access_token 的值
        const apiUrl = 'http://localhost:9093/callBaiduAI';
        const headers = {
          'Content-Type': 'application/json',
          'access_token': accessToken,
        };
        const data = {
          messages: [
            { role: 'user', content: `请生成与${this.inputAI.join(' ')}有关的发生迷路事件的描述，最多150字，要求情感中立，注重客观陈述` }
          ],
        };
        const response = await axios.post(apiUrl, data, { headers });
        this.Content = response.data.result;
        console.log('生成的与迷路场景相关的文本说明:', this.Content);
      } catch (error) {
        console.error('调用AI时出错：', error);
      }
    },
    showPromptDialog() {
      this.$prompt('请输入问题，类似"武汉市哪些地点容易迷路？"', '提示', {
        inputType: 'textarea',
        inputRows: 10, // 控制输入框的行数
        inputPlaceholder:
            '要求输入的文字要使用关键词标明以帮助我们确认需要查询的内容，如：“哪些天气”、“哪些类型”、“哪些方式”、“哪些人群”、“什么时间”、“什么原因”',
        inputAttributes: {
          style: 'height: 300px; max-height: 500px;', // 调整输入框大小
        },
        inputPattern: /\S/, // 输入内容不能为空格
        inputPatternMessage: '输入内容不能为空',
      }).then(async ({ value }) => {
        this.Content = '加载中(ﾟДﾟ≡ﾟдﾟ)';
        await this.processInput(value);
        setTimeout(() => {
          this.callAI();
        }, 2000);
        this.dialogVisible = true;
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '取消输入',
        });
      });
    },
    processInput(input) {
      const matchedResults = [];
      this.SearchKey.forEach((node) => {
        const regex = new RegExp(`${node.properties.name}`, "g");
        const matches = input.match(regex);
        if (matches) {
          matchedResults.push({
            id: node.id.toString(),
            label: node.label,
            properties: {
              number: node.properties.number,
              name: node.properties.name
            }
          });
        }
      });
      this.columns.forEach((column) => {
        column.isVisible = false;
        column.searchQuery = '';
      });
      const keywords = ["省份","城市","地点", "天气", "类型", "时间", "天气", "人群", "原因", "方式"];
      keywords.forEach((keyword) => {
        const regex = new RegExp(`${keyword}`, "g");
        const matches = input.match(regex);
        if (matches) {
          switch (keyword) {
            case "省份":
              this.columns.find((column) => column.name === 'Province').isVisible = true;
              break;
            case "城市":
              this.columns.find((column) => column.name === 'City').isVisible = true;
              break;
            case "地点":
              this.columns.find((column) => column.name === 'Location').isVisible = true;
              break;
            case "类型":
              this.columns.find((column) => column.name === 'POI').isVisible = true;
              break;
            case "人群":
              this.columns.find((column) => column.name === 'Person').isVisible = true;
              break;
            case "方式":
              this.columns.find((column) => column.name === 'TripMode').isVisible = true;
              break;
            case "天气":
              this.columns.find((column) => column.name === 'Weather').isVisible = true;
              break;
            case "原因":
              this.columns.find((column) => column.name === 'Reason').isVisible = true;
              break;
            case "时间":
              this.columns.find((column) => column.name === 'Time').isVisible = true;
              break;
            default:
              break;
          }
        }
      });
      // 填充匹配结果到相应输入框中并设置开关状态
      matchedResults.forEach((result) => {
        const { label, properties } = result;
        const targetColumn = this.columns.find((column) => column.name === label);
        if (targetColumn) {
          targetColumn.isVisible = true;
          targetColumn.searchQuery = properties.name;
        }
      });
      // 检查Place输入框内容是否与前面输入框相同
      const placeColumn = this.columns.find((column) => column.name === 'Place');
      if (placeColumn) {
        const isSameAsOtherColumns = this.columns
            .filter((column) => column.name !== 'Place')
            .some((column) => column.searchQuery === placeColumn.searchQuery);
        if (isSameAsOtherColumns) {
          placeColumn.searchQuery = '';
        }
      }
      this.applySearch();
    },
    onMouseDown(event) {
      this.mouseX = event.clientX;
      this.mouseY = event.clientY;
      this.isDragging = true;
      document.addEventListener('mousemove', this.onMouseMove);
      document.addEventListener('mouseup', this.onMouseUp);
    },
    onMouseMove(event) {
      if (this.isDragging) {
        const deltaX = event.clientX - this.mouseX;
        const deltaY = event.clientY - this.mouseY;
        this.translateX += deltaX;
        this.translateY += deltaY;
        this.node1.style.transform = `translate(` + this.translateX + 'px,' + this.translateY + `px)`;
        this.mouseX = event.clientX;
        this.mouseY = event.clientY;
      }
    },
    onMouseUp() {
      this.isDragging = false;
      document.removeEventListener('mousemove', this.onMouseMove);
      document.removeEventListener('mouseup', this.onMouseUp);
    },
    // 通用函数，用于获取不同类型节点的数据并追加到this.customNodes中
    fetchNodeDataByType(nodeType) {
      const endpoint = `findAll${nodeType}`;
      axios.post(`http://localhost:9093/Node/${endpoint}`)
          .then(response => {
            // 确保 response.data 是一个数组
            if (Array.isArray(response.data.object)) {
              // 迭代新数据并将其追加到this.customNodes中
              response.data.object.forEach(node => {
                const newNode = {
                  id: node.uuid, // 将id转换为字符串形式
                  label: nodeType, // 使用节点类型作为标签
                  properties: {
                    number: node.count, // 增加索引以匹配你所需的数字格式
                    name: node.name // 使用属性名称动态获取节点名称
                  }
                };
                this.customNodes.push(newNode);
              });
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
    fetchNodeDataByName(nodeType, nodeName) {
      const endpoint = `findBy${nodeType}Name?${nodeType}Name=${nodeName}`;
      axios.post(`http://localhost:9093/Node/${endpoint}`)
          .then(response => {
            // 确保 response.data 是一个数组
            if (Array.isArray(response.data.object)) {
              // 迭代新数据并将其追加到 this.customNodes 中
              response.data.object.forEach(node => {
                const newNode = {
                  id: node.uuid, // 将id转换为字符串形式
                  label: nodeType, // 使用节点类型作为标签
                  properties: {
                    number: node.count, // 增加索引以匹配你所需的数字格式
                    name: node.name // 使用属性名称动态获取节点名称
                  }
                };
                this.customNodes = this.customNodes.concat(newNode);
              });
            } else {
              // 处理错误，如果 response.data.object 不是数组
              console.error(`错误：响应的${nodeType}数据.object不是数组`);
            }
          })
          .catch(error => {
            // 处理错误
            console.error(`获取${nodeType}节点数据时出错:`, error);
          });
    },
    // 通用关系数据获取函数
    fetchRelationDataByType(relationType) {
      axios.post(`http://localhost:9093/Relation/find_${relationType}`)
          .then(response => {
            const dataArray = response.data.object;
            if (Array.isArray(dataArray)) {
              const formattedData = dataArray.map(item => {
                // 提取所有具有 'uuid' 属性的字段，并作为潜在的 source 和 target 节点
                const keys = Object.keys(item).filter(key => item[key]?.uuid);
                // 通过 keys 长度判断并分配 source 和 target
                if (keys.length >= 2) {
                  const [sourceKey, targetKey] = keys;
                  return {
                    source: item[sourceKey].uuid,
                    sourceLabel: sourceKey.charAt(0).toUpperCase() + sourceKey.slice(1),
                    target: item[targetKey].uuid,
                    targetLabel: targetKey.charAt(0).toUpperCase() + targetKey.slice(1),
                    type: `${relationType}:${item.count || 0}`
                  };
                } else {
                  console.error(`无法解析来自${relationType}数据的source和target:`, item);
                  return null;
                }
              }).filter(Boolean); // 过滤掉 null 项
              console.log(response.data.object);
              // 将格式化后的数据添加到 customRelationships 数组中
              this.customRelationships = this.customRelationships.concat(formattedData);
            } else {
              console.error(`来自后端的${relationType}数据不是一个数组:`, dataArray);
            }
          })
          .catch(error => {
            console.error(`来自后端的${relationType}数据错误:`, error);
          });
    },
    showNodeInfo(node) {
      if (node && node.label) {
        this.selectedNodeInfo = {
          id: node.id,
          label: node.label,
          number: node.number,
          name: node.name,
        };
      } else {
        // 处理数据不完整的情况，或者其他错误情况
        console.error('节点数据不完整或无效:', node);
      }
    },
    handleNodeClick(event) {
      const customData = JSON.parse(event.currentTarget.getAttribute("data-custom-data"));
      // 输出 customData 到控制台，以检查其内容
      console.log("customData:", customData);
      // 使用 customData 对象来访问节点的自定义数据
      const id = customData.id;
      const label = customData.label;
      const name = customData.properties.name;
      const number = customData.properties.number;
      // 创建一个包含节点信息的对象
      const nodeInfo = {
        id,
        label,
        name,
        number
      };
      // 调用 showNodeInfo 方法来显示节点信息
      this.showNodeInfo(nodeInfo);
    },
    getDataAndDraw() {
      console.log(this.customNodes);
      console.log(this.customRelationships);
      this.clearGraph(); // 清空旧图形和力导向图 simulation
      const nodes = this.customNodes.filter(node => {
        const column = this.columns.find(column => column.name === node.label);
        return column && column.isVisible;
      });
      if (nodes.length === 0) {
        alert("找不到数据");
      }
      const nodeIds = nodes.map(node => node.id); // 获取所有节点的id
      const links = [];
      const nodeNames = {};
      this.customRelationships.forEach(link => {
        // 检查source和target是否都存在于节点id中
        if (nodeIds.includes(link.source) && nodeIds.includes(link.target)) {
          links.push({ ...link });
          if (!nodeNames[link.source]) {
            const sourceNode = this.customNodes.find(node => node.id === link.source);
            nodeNames[link.source] = sourceNode.properties.name;
          }
          if (!nodeNames[link.target]) {
            const targetNode = this.customNodes.find(node => node.id === link.target);
            nodeNames[link.target] = targetNode.properties.name;
          }
        }
      });
      this.inputAI = Object.values(nodeNames); // 保存节点名称
      const graphContainer = d3.select("#graph");
      const svg = graphContainer.append("svg")
          .style("width", "700vw")
          .style("height", "700vw")
          .attr("id", 'map')
          .append("g");
      // 在创建力导向布局时添加力导向约束
      const simulation = d3.forceSimulation(nodes)
          .force("link", d3.forceLink(links).id(d => d.id).distance(160))
          .force("charge", d3.forceManyBody().strength(-300))
          .force("center", d3.forceCenter(800 / 2, 600 / 2))
          .force("x", d3.forceX().strength(0.05).x(d => {
            // 限制节点在图形容器的水平范围内
            return Math.max(28, Math.min(400 - 28, d.x));
          }))
          .force("y", d3.forceY().strength(0.05).y(d => {
            // 限制节点在图形容器的垂直范围内
            return Math.max(28, Math.min(300 - 28, d.y));
          }));
      // 创建关系容器
      const linkContainer = svg.append("g")
          .selectAll("g")
          .data(links)
          .enter()
          .append("g");
      // 创建箭头定义
      svg.append("defs").append("marker")
          .attr("id", "arrow")
          .attr("viewBox", "0 -5 10 10")
          .attr("refX", 40) // 箭头距离目标节点的距离，根据节点半径来设置
          .attr("refY", 0)
          .attr("markerWidth", 4.5)
          .attr("markerHeight", 4.5)
          .attr("orient", "auto")
          .append("path")
          .attr("d", "M0,-5L10,0L0,5")
          .attr("class", "arrow-head")
          .attr("fill", "#999");
      // 创建关系
      const link = linkContainer
          .append("line")
          .attr("stroke", "#999")
          .attr("stroke-opacity", 0.6)
          .attr("stroke-width", 2)
          .attr("marker-end", "url(#arrow)") // 使用箭头
      // 为关系容器添加文本说明
      linkContainer.each(function (d) {
        const container = d3.select(this);
        const labelDescription = d.type; // 获取关系的说明文字
        // 计算关系容器的中心位置
        const linkLength = link.node().getTotalLength();
        const linkCenter = linkLength / 2;
        // 创建文本元素并放置在关系容器中心
        container
            .append("text")
            .attr("text-anchor", "middle")
            .attr("font-size", "12px")
            .attr("x", linkCenter)
            .attr("y", 0) // 根据需要调整垂直位置
            .text(labelDescription)
            .style("pointer-events", "none"); // 防止文本标签干扰拖动
      });
      const node = svg.selectAll(".node")
          .data(nodes)
          .enter()
          .append("g")
          .attr("class", "node")
          .attr("data-custom-data", d => JSON.stringify(d)) // 将节点数据转换为字符串并存储在自定义数据属性中
          .on("click", d => this.handleNodeClick(d)) // 添加点击事件监听器
          .call(this.drag(simulation));
      // 创建节点图形
      node.append("circle")
          .attr("r", 28)
          .attr("stroke", "#fff")
          .attr("stroke-width", 1)
          .attr("fill", d => {
            // 根据节点的属性来决定节点的颜色
            switch (d.label) {
              case 'Province':
                return d.name ? d.switchActiveColor : '#3ca8cc';
              case 'City':
                return d.name ? d.switchActiveColor : '#c9b051';
              case 'Area':
                return d.name ? d.switchActiveColor : '#49a964';
              case 'Location':
                return d.name ? d.switchActiveColor : '#fc7349';
              case 'POI':
                return d.name ? d.switchActiveColor : '#7fad7b';
              case 'Time':
                return d.name ? d.switchActiveColor : '#6f95d0';
              case 'Weather':
                return d.name ? d.switchActiveColor : '#e8a75a';
              case 'Person':
                return d.name ? d.properties.switchActiveColor : '#c280c2';
              case 'TripMode':
                return d.name ? d.switchActiveColor : '#be7843';
              case 'Reason':
                return d.name ? d.switchActiveColor : '#8377b6';
              default:
                return '#ccc';
            }
          })
      node.append("foreignObject")
          .attr("width", "56")
          .attr("height", "56")
          .style("display", "flex")
          .style("justify-content", "flex-start")
          .style("align-items", "flex-start")
          .html(d => {
            const textContent = (d.properties.name?.length || 0) > 5 ? d.properties.name.substring(0, 5) + '...' : d.properties.name;
            return `<div style="width:100%;height:100%;display:flex;align-items:center;justify-content:center;font-size:11px; text-align:center;overflow:hidden;">${textContent}</div>`
          })
          .attr("x", -28)
          .attr("y", -28);
      simulation.on("tick", () => {
        // 更新节点和关系的位置
        node.attr("transform", d => `translate(${d.x + 4500},${d.y + 4500})`);
        link.attr("x1", d => d.source.x + 4500)
            .attr("y1", d => d.source.y + 4500)
            .attr("x2", d => d.target.x + 4500)
            .attr("y2", d => d.target.y + 4500);
        // 更新关系标签的位置
        linkContainer.selectAll("text")
            .attr("x", d => ((d.source.x + d.target.x) / 2) + 4500) // 将标签放在关系的中间
            .attr("y", d => ((d.source.y + d.target.y) / 2) + 4500);
      });
      let that = this
      that.node1 = document.getElementById('map');
      document.documentElement.scrollLeft = document.documentElement.scrollWidth / 5;
      document.documentElement.scrollTop = document.documentElement.scrollHeight / 2;
      that.node1.addEventListener('mousedown', function (event) {
        that.onMouseDown(event)
      });
    },
    drag(simulation) {
      function dragstarted(event) {
        if (!event.active) simulation.alphaTarget(0.3).restart();
        event.subject.fx = event.subject.x;
        event.subject.fy = event.subject.y;
      }

      function dragged(event) {
        event.subject.fx = event.x;
        event.subject.fy = event.y;
      }

      function dragended(event) {
        if (!event.active) simulation.alphaTarget(0);
        event.subject.fx = null;
        event.subject.fy = null;
      }

      return d3.drag()
          .on("start", dragstarted)
          .on("drag", dragged)
          .on("end", dragended);
    },
    clearGraph() {
      // 清空图形和 simulation
      if (this.simulation) {
        this.simulation.stop();
      }
      const graphContainer = d3.select("#graph");
      graphContainer.selectAll("*").remove(); // 移除所有子元素
    },
    async applySearch() {
      // 重置容器位置为初始值
      this.translateX = 0;
      this.translateY = 0;
      this.customNodes = [];
      // 遍历所有搜索框
      for (const [index, column] of this.columns.entries()) {
        // 获取用户输入的搜索内容
        const userInput = column.searchQuery;
        // 根据搜索框的名称执行不同的搜索操作
        if (userInput) {
          await this.fetchNodeDataByName(column.name, userInput);
        } else {
          await this.fetchNodeDataByType(column.name);
        }
      }
      setTimeout(() => {
        this.getDataAndDraw();
      }, 1000)
    },
    resetSearch() {
      this.columns.forEach(column => {
        column.searchQuery = '';
        column.isVisible = false; // 关闭开关
      });
      this.clearGraph();
    },
  }
};
</script>

<style>
* {
  overflow: hidden;
}

/* 整体样式 */
.whole {
  position: relative;
  width: 100%;
  height: 100%;
  background-color: #dddddd;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  z-index: 0;
}

.node-info-popup {
  position: absolute;
  top: 10px;
  left: 20px;
  z-index: 999;
}

.map-container {
  position: absolute;
  width: 600vw;
  height: 600vw;
  cursor: move;
  overflow: auto;
  background-color: #dddddd;
  top: -300vw; /* 扩展上边界 */
  left: -300vw; /* 扩展左边界 */
  z-index: 2;
}

#graph {
  width: 100%;
  height: 100%;
}

/* 控制面板 */
#control-panel {
  position: fixed;
  top: 0;
  right: 0; /* 移动到右侧 */
  bottom: 0;
  display: flex;
  flex-direction: column;
  padding-top: 5px;
  width: 220px;
  background-color: whitesmoke;
  transition: width 0.3s ease;
  z-index: 99;
  border-left: 1px solid #ccc; /* 左侧边框 */
  overflow: auto;
}

#control-panel::-webkit-scrollbar {
  width: 1px; /* 滚动条宽度 */
}

/* 间隔 */
.spacer {
  height: 45px;
  width: 100%;
  background-color: transparent;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0;
  padding: 0;
  border: none;
  transition: transform 0.3s ease;
}

/* 控制标签文本 */
.label-container span {
  margin-right: 5px; /* 添加一定的右边距以与开关分隔开 */
}

/* 文本样式 */
.text {
  font-weight: bold;
  font-size: 1.2em;
  text-align: center; /* 文本居中对齐 */
}

/* 控制项容器 */
.control-item {
  margin-bottom: 5px;
  display: flex;
  align-items: center; /* 垂直居中对齐 */
  justify-content: center; /* 水平居中对齐 */
  flex-direction: column; /* 垂直排列 */
  padding: 2px;
  background-color: #f9f9f9;
}

/* 控制标签 */
.control-label {
  font-weight: bold;
  display: block;
  margin-bottom: 5px;
  font-size: 1em;
  text-align: center; /* 文本居中对齐 */
}

/* 控制输入框 */
.control-input {
  width: 95%;
  padding: 5px 10px;
  font-size: 0.9em;
  box-sizing: border-box;
  border: 1px solid #ccc;
  border-radius: 4px;
  margin-bottom: 5px;
  background-color: #f9f9f9;
  margin-left: 5px; /* 左边距，留出一定距离 */
  margin-right: 5px; /* 右边距，留出一定距离 */
  text-align: center; /* 文本居中对齐 */
}

/* 输入框选中状态 */
input:checked {
  background-color: #483d8b;
}

input:checked:before {
  transform: translateX(20px);
}

/* 添加按钮样式 */
.control-buttons {
  margin-top: 10px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-button, .reset-button {
  width: 48%;
  border-radius: 10px; /* 圆角矩形 */
  text-align: center;
  font-size: 16px;
  padding: 10px 0; /* 调整按钮内边距 */
  cursor: pointer;
  height: 35px;
  margin: 5px;
  transition: background-color 0.3s ease, color 0.3s ease; /* 添加颜色过渡效果 */
}

.search-button:hover, .reset-button:hover {
  background-color: #409EFF; /* 悬停时的背景颜色 */
  color: #fff; /* 悬停时的文本颜色 */
}

.fixed-button {
  position: absolute;
  bottom: 15px;
  left: 40px;
  transform: translateX(-20px);
  z-index: 2000;
  border: none;
  color:white;
}

.fixed-button:hover{
  color:white;
  font-size: 16px;
}
</style>