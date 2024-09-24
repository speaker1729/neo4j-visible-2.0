<template>
  <div id="app">
    <!-- 主要内容区域 -->
    <section class="hero-section">
      <h1>Welcome to Our Knowledge Platform</h1>
      <p>提供强大的知识图谱展示、路线规划、地点标注、统计图、热力图和数据分析功能。</p>
      <div class="back-button-container">
        <el-button type="text" class="back-button" @click="goBack">
          <i class="el-icon-arrow-left"></i> 返回主页
        </el-button>
      </div>
    </section>

    <!-- 产品和功能介绍 -->
    <section class="features-section">
      <h2>我们的产品和功能</h2>
      <div class="feature-row">
        <div class="feature-col">
          <div class="feature">
            <i class="el-icon-coin"></i>
            <h3>知识图谱展示</h3>
            <p>展示复杂数据的关联和图形化呈现，助力决策和分析。</p>
          </div>
        </div>
        <div class="feature-col">
          <div class="feature">
            <i class="el-icon-map-location"></i>
            <h3>路线规划</h3>
            <p>智能路线规划和导航，为用户提供最佳出行方案。</p>
          </div>
        </div>
        <div class="feature-col">
          <div class="feature">
            <i class="el-icon-place"></i>
            <h3>地点标注</h3>
            <p>标注和管理重要地点，支持地图可视化展示。</p>
          </div>
        </div>
      </div>
      <div class="feature-row">
        <div class="feature-col">
          <div class="feature">
            <i class="el-icon-data-analysis"></i>
            <h3>统计图</h3>
            <p>生成各种统计图表，帮助用户理解数据趋势和模式。</p>
          </div>
        </div>
        <div class="feature-col">
          <div class="feature">
            <i class="el-icon-odometer"></i>
            <h3>热力图</h3>
            <p>展示数据在全国范围内的分布情况。</p>
          </div>
        </div>
        <div class="feature-col">
          <div class="feature">
            <i class="el-icon-paperclip"></i>
            <h3>3D模型</h3>
            <p>由第三方网站实现的数据库3D模型展示。</p>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
export default {
  data() {
    return {};
  },
  methods: {
    goBack() {
      this.$router.push('/home');
    }
  }
};
</script>

<style scoped>
/* 样式 */
@import url('https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css');

body {
  margin: 0;
  padding: 0;
  overflow-x: hidden;
}

#app {
  margin: 0;
  padding: 0;
}

.hero-section {
  text-align: center;
  margin-bottom: 40px;
  background-color: #007bff;
  color: #fff;
  padding: 60px 0;
}

h1 {
  font-size: 36px;
  margin-bottom: 20px;
}

.back-button-container {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
}

.back-button {
  background-color: #0056b3;
  height: 50px;
  width: 120px;
  padding: 10px;
  color: white;
  font-size: 15px;
  text-align: center;
  text-decoration: none;
  border-radius: 10px;
}

.back-button:hover {
  background-color: #00408a;
}

.features-section h2 {
  margin-bottom: 20px;
  text-align: center;
  font-size: 24px;
  color: #007bff;
}

.feature-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.feature-col {
  flex: 0 0 calc(33.33% - 10px);
  max-width: calc(33.33% - 10px);
}

.feature {
  text-align: center;
  padding: 20px;
  background-color: #fff;
  border: 1px solid #e0e0e0;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.feature i {
  font-size: 48px;
  color: #007bff;
  margin-bottom: 10px;
}

.feature p {
  color: #666;
  line-height: 1.6;
}
</style>