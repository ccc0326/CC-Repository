<template>
  <div>
    <!-- 卡片化走马灯 -->
    <el-carousel :interval="4000" type="card" height="500px">
      <el-carousel-item v-for="(item, index) in carouselImages" :key="index">
        <img :src="item.src" :alt="item.alt" class="carousel-img" @click="openDrawer(item)"/>
      </el-carousel-item>
    </el-carousel>
    <div class="home-background">
    <div class="whole">
      <!-- 左侧内容 -->
      <div class="left">
        <!-- 热映中 -->
        <div class="panel">
          <div class="panel-header">
            <div style="color: rgba(102,128,234,0.85); font-size: 28px;">
              <VideoCameraFilled style="font-size: 42px;" />
              <b> 正在热映</b>
            </div>
            <a href="/movie/movieOngoing">全部</a>
          </div>
          <div class="panel-content">
            <movie-item :movieItem="item" v-for="(item, index) in ongoingMovieList" :key="index"></movie-item>
          </div>
        </div>

        <!-- 即将上映 -->
        <div class="panel">
          <div class="panel-header">
            <div style="color: #2d98f3; font-size: 28px;">
              <CalendarFilled style="font-size: 42px;" />
              <b> 即将上映</b>
            </div>
            <a href="/movie/movieUpcoming">全部</a>
          </div>
          <div class="panel-content">
            <movie-item :movieItem="item" v-for="(item, index) in upcomingMovieList" :key="index"></movie-item>
          </div>
        </div>


      </div>

    </div>
    </div>

    <!-- 抽屉组件 -->
    <el-drawer
      title="电影介绍"
      :visible.sync="drawer"
      :direction="direction"
      :before-close="handleClose">
      <div class="drawer-content">
        <h3>{{ selectedMovie.title }}</h3>
        <img :src="selectedMovie.src" alt="Movie Image" class="drawer-img" />
        <p>{{ selectedMovie.description }}</p>
        <el-button type="primary" @click="goToDetail(selectedMovie.id)">点我了解详情</el-button>
      </div>
    </el-drawer>
  </div>
</template>


<script>
import { VideoCameraFilled, CalendarFilled, HeartFilled, TrophyFilled } from '@element-plus/icons-vue';
import movieItem from '../../components/movie/movie-item';
import moment from 'moment';

export default {
  name: 'Home',
  components: {
    movieItem,
    VideoCameraFilled,
    CalendarFilled,
    HeartFilled,
    TrophyFilled
  },
  data() {
    return {
      drawer: false, // 控制抽屉的显示
      direction: 'rtl', // 抽屉的打开方向
      selectedMovie: { // 当前选中的电影信息
        title: '',
        description: '',
        id: ''
      },
      carouselImages: [
        { src: require('@/assets/21shiji.jpg'), alt: 'Image 1', title: '从21世纪安全撤离', description: '友情提示：请在地球人陪同下前往影院观看。在地球人都不知道的K星，三个被命运选中的少年，靠打个喷嚏就可以往返未来二十年。面对毁灭世界的惊天阴谋，他们除了清澈的愚蠢和个位数的战力外一无所有，如何变身英雄对抗意图毁灭世界的超级坏坏的大坏蛋。8月3日，外表看似大人，头脑却异于常人的“王炸”兄弟组合，代表k星拯救未来。', id: '2' },
        { src: require('@/assets/mosha.jpg'), alt: 'Image 2', title: '默杀', description: '初中女生惠君（徐娇 饰）长期遭校园霸凌高处坠亡，暴行非但没有停止，甚至祸延到闺蜜小彤（王圣迪 饰）身上。然而，霸凌者也接连殒命于重锤之下。在连环杀人案的背后，难脱嫌疑的众人逐一浮出水面：小彤的母亲（张钧甯 饰）目睹女儿被胶水封嘴却闭口不言，校工林在福（王传君 饰）看似知晓内情却冷漠疏离，暗中窥探的神秘男吴望（黄明昊 饰）看似身陷险境却面露笑容，杀害霸凌者的雨夜屠夫究竟是他们其中的何人。调查案件的警官戴国栋（吴镇宇 饰）似乎被假象蒙蔽，探秘过程中，宗宗藏污纳垢的旧事被揭开，所有人都被推到了风口浪尖。', id: '1' },
        { src: require('@/assets/xiyangyang.jpg'), alt: 'Image 3', title: '喜羊羊与灰太狼之守护', description: '自然界中的神奇宝石维护着世界的稳定和平，一股神秘力量正蠢蠢欲动，危机四处蔓延。众羊狼与新伙伴再踏上守护世界的征途，争分夺秒与神秘势力的阴谋赛跑，并在一次次的冒险与闯关中共同成长，最终明白了守护的意义。', id: '3' },
        { src: require('@/assets/chuanshuo.jpg'), alt: 'Image 4', title: '传说', description: '考古专家陈教授发现，学生王靖带来的文物上的纹理和自己梦中女子梦云所带玉佩极其相似。冥冥之中，他感觉到两者必然有所关联，带着所有的问题，他和考察队开启了一场奇幻的冒险之旅。只是随着考察的深入，众人来到冰川下的神庙，当教授看到冰床上的梦云，一切又变得那么真实和虚无，而所有答案都只能等待梦云的苏醒。', id: '4' }
      ],
      queryInfo1: {
        total: 0,
        pageSize: 8,
        pageNum: 1,
        startDate: moment().subtract(365, "days").format("YYYY-MM-DD"),
        endDate: moment().format('YYYY-MM-DD'),
      },
      queryInfo2: {
        total: 0,
        pageSize: 8,
        pageNum: 1,
        startDate: moment().format('YYYY-MM-DD')
      },
      queryInfo3: {
        total: 0,
        pageSize: 8,
        pageNum: 1
      },
      queryInfo4: {
        pageSize: 10,
        pageNum: 1
      },
      ongoingMovieList: [],
      upcomingMovieList: [],
      classicMovieList: [],
      totalBoxOfficeList: [],
      carouselHeight: '500px'  // 走马灯高度，可以根据需要调整
    };
  },
  created() {
    this.getOngoingMovieList();
    this.getUpcomingMovieList();
    this.getClassicMovieList();
    this.getTotalBoxOfficeList();
  },
  methods: {
    // 打开抽屉并设置选中的电影信息
    openDrawer(item) {
      this.selectedMovie = {
        title: item.title,
        description: item.description,
        src: item.src,
        id: item.id
      };
      this.drawer = true;
    },
    // 处理抽屉关闭
    handleClose(done) {
      this.$confirm('确认关闭？')
        .then(_ => {
          done();
        })
        .catch(_ => {});
    },
    // 跳转到电影详情页面
    goToDetail(id) {
      this.$router.push(`/movieInfo/${id}`);
    },
    async getOngoingMovieList() {
      const { data: res } = await axios.get('sysMovie/find', { params: this.queryInfo1 });
      if (res == undefined) {
        return;
      }
      this.ongoingMovieList = res.data;
      this.total = res.total;
    },
    async getUpcomingMovieList() {
      const { data: res } = await axios.get('sysMovie/find', { params: this.queryInfo2 });
      if (res == undefined) {
        return;
      }
      this.upcomingMovieList = res.data;
      this.total = res.total;
    },
    async getClassicMovieList() {
      const { data: res } = await axios.get('sysMovie/find', { params: this.queryInfo3 });
      if (res == undefined) {
        return;
      }
      this.classicMovieList = res.data;
      this.total = res.total;
    },
    async getTotalBoxOfficeList() {
      const { data: resp } = await axios.get('sysMovie/find/rankingList/1', { params: this.queryInfo4 });
      if (resp == undefined) {
        return;
      }
      if (resp.code !== 200) return this.$message.error(resp.msg);
      this.totalBoxOfficeList = resp.data;
    }
  }
};
</script>

<style scoped>
.el-carousel {
  width: 80%;
  margin: 30px auto;
}

.carousel-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  cursor: pointer;
  border-radius: 8px;
}

.home-background {
  background: linear-gradient(135deg, #5f97ff, rgba(255, 255, 255, 0.63));
  background-size: cover;
  min-height: 100vh;
  padding: 20px;
}
/* 调整内容的整体布局，使其在背景上居中 */
.whole {
  width: 1200px;
  margin: 30px auto;
  display: flex;
  background-color: rgba(255, 255, 255, 0.9); /* 半透明白色背景，突出内容 */
  border-radius: 15px; /* 添加圆角 */
  padding: 20px; /* 内边距 */
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2); /* 添加阴影效果 */
}
.left {
  width: 70%;
}

.right {
  width: 30%;
  margin-left: 100px;
}

.panel-header > a {
  text-align: center;
  text-decoration: none;
  color: #999;
  padding-right: 14px;
}

.panel-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.movie-item {
  margin-left: 0;
  margin-right: 30px;
}

.movie-item:nth-child(4n) {
  margin-right: 0;
}

.board {
  display: flex;
  margin: 10px 10px;
}

.board-left {
  display: flex;
  align-items: center;
}

.board-middle {
  display: flex;
  margin-left: 10px;
  width: 150px;
  font-size: 18px;
}

.board-middle > a {
  text-decoration: none;
  color: #333;
}

.board-index {
  color: #ffb400;
  display: inline-block;
  width: 50px;
  height: 50px;
  line-height: 50px;
  text-align: center;
  font-size: 18px;
  align-items: center;
}

.panel-content {
  margin: 0px 0px 50px 0px;
}

.drawer-content {
  text-align: center;
}

.drawer-img {
  width: 100%;
  max-width: 400px;
  margin: 20px auto;
  border-radius: 8px;
  border: 2px solid #ebeef5;
}

.drawer-content p {
  margin: 20px 0;
}

.el-button {
  margin-top: 20px;
}

</style>
