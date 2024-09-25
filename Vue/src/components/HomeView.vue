<template>
  <div class="home">
    <div v-if="showWelcomeModal" class="welcome-modal">
      <div class="welcome-content">
        <img src="../assets/logo.png" alt="Logo"/>
        <h1>欢迎您访问项目城市导航迷路场景关联知识图谱与计算系统</h1>
        <button @click="closeWelcomeModal">关闭</button>
      </div>
    </div>
    <el-menu
        default-active="1-4-1"
        class="el-menu-vertical-demo"
        @open="handleOpen"
        @close="handleClose"
        :collapse="isCollapse"
    >
      <div class="custom-button" :style="{ left: isCollapse ? '0' : '67px' }">
        <img
            src="../assets/button.png"
            alt="Button"
            style="width: 50px; height: 50px; cursor: pointer;"
            @click="isCollapse=!isCollapse"
        />
      </div>
      <el-menu-item index="0" style="font-weight: bold; font-size: 22px;">
        <span style="display: flex; justify-content: center; align-items: center;">城市导航迷路场景</span>
      </el-menu-item>
      <el-menu-item index="0" style="font-weight: bold; font-size: 16px;">
        <span style="display: flex; justify-content: center; align-items: center;">关联知识图谱与计算系统</span>
      </el-menu-item>
      <router-link :to="{ name: 'home' }">
        <el-menu-item index="1">
          <i class="el-icon-coin"></i>
          <span slot="title">迷路场景知识图谱</span>
        </el-menu-item>
      </router-link>
      <router-link :to="{ name: 'map' }">
        <el-menu-item index="2">
          <i class="el-icon-map-location"></i>
          <span slot="title">出行路线规划</span>
        </el-menu-item>
      </router-link>
      <router-link :to="{ name: 'circle' }">
        <el-menu-item index="3">
          <i class="el-icon-scissors"></i>
          <span slot="title">时空间查询分析</span>
        </el-menu-item>
      </router-link>
      <router-link :to="{ name: 'statistics' }">
        <el-menu-item index="4">
          <i class="el-icon-odometer"></i>
          <span slot="title">空间统计分析</span>
        </el-menu-item>
      </router-link>
      <router-link :to="{ name: 'china' }">
        <el-menu-item index="5">
          <i class="el-icon-discover"></i>
          <span slot="title">省级迷路地图</span>
        </el-menu-item>
      </router-link>
      <router-link :to="{ name: 'data' }">
        <el-menu-item index="6">
          <i class="el-icon-data-analysis"></i>
          <span slot="title">多维迷路指标统计</span>
        </el-menu-item>
      </router-link>
    </el-menu>
    <div class="tomain" @click="goto()">
      <i class="el-icon-info"></i>
    </div>
    <div class="center" :style="{ width: isCollapse ? '95vw' : '85vw' }">
      <router-view></router-view>
    </div>
  </div>
</template>

<script>
import router from "@/router";

export default {
  data() {
    return {
      isLoading: false,
      isCollapse: false,
      showWelcomeModal: true, // 添加欢迎弹窗的显示状态
    };
  },
  methods: {
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    },
    goto() {
      router.push('/about')
    },
    closeWelcomeModal() {
      this.showWelcomeModal = false; // 关闭欢迎弹窗
    },
  },
  mounted() {
    setTimeout(() => {
      this.closeWelcomeModal();
    }, 1000);
  },
};
</script>

<style>
.home {
  width: 100vw;
  height: 100vh;
  position: relative;
  display: flex; /* 使用 flex 布局 */
  overflow: hidden; /* 隐藏溢出内容 */
  transition: width 0.3s; /* 添加过渡效果，用于平滑调整侧边栏宽度 */
}

.tomain {
  position: absolute;
  left: 1%;
  bottom: 3%;
  width: 50px;
  height: 50px;
  font-size: 25px;
  color: black;
  z-index: 99;
  background-color: steelblue;
  border-radius: 50%;
  text-align: center;
  display: flex;
  justify-content: center;
  align-items: center;
}

.tomain:hover {
  transform: scale(1.2);
  cursor: pointer;
}

.center {
  position: absolute;
  right: 0;
  top: 0;
  height: 100%;
  transition: margin-left 0.3s;
}

.center {
  position: absolute;
  right: 0;
  top: 0;
  height: 100%;
  transition: margin-left 0.3s;
}

.custom-button {
  position: absolute;
  width: 5vw;
  height: 5vw;
  top: 5px;
  left: 0;
  z-index: 99;
  text-align: center;
  line-height: 5vh;
  font-size: 25px;
  transition: transform 0.2s;
}

.custom-button:hover {
  transform: scale(1.1);
  cursor: pointer;
}

.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 15vw;
}

.el-menu-vertical-demo {
  box-sizing: border-box;
  padding-top: 50px;
  width: 5vw;
  height: 100vh;
  z-index: 9;
  border: none;
  background: steelblue;
  box-shadow: 2px 0 6px rgba(0, 0, 0, 0.2);
}

.el-menu-item:hover {
  background-color: #0f81e1;
}

.el-menu-item.is-active {
  background-color: #0f81e1;
  color: ghostwhite;
}

.el-menu-item i {
  color: #000;
  font-size: 28px;
}

.welcome-modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.7);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
}

.welcome-content {
  text-align: center;
  background-color: white;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
}

.welcome-content img {
  width: 90%;
  height: 90%;
}

.welcome-content h1 {
  font-size: 24px;
  margin-bottom: 10px;
}

.welcome-content button {
  background-color: #007BFF;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  font-size: 16px;
  cursor: pointer;
}

.welcome-content button:hover {
  background-color: #0056b3;
}
</style>