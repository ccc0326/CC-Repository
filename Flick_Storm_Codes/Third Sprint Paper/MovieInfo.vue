<template>
  <div class="movie-container">
    <div class="header">

      <div class="header-inner clearfix">
        <div class="movie-info-left">
          <div class="avatar-shadow">
            <img class="avatar" :src="movieInfo.moviePoster">
          </div>

        </div>

        <div class="movie-info-right">
          <div class="movie-info-msg">
            <h1 class="movie-name">{{movieInfo.movieName}}</h1>
            <ul>
              <li>{{movieInfo.movieCategoryList}}</li>
              <li>{{movieInfo.movieArea}} / {{movieInfo.movieLength}}分钟</li>
              <li>{{movieInfo.releaseDate}} {{movieInfo.movieArea}} 上映</li>
            </ul>
          </div>

          <div class="movie-info-btn">

            <el-button class="rate-btn" type="warning" @click="openRatingDialog" style="font-size: 22px;">
              <i class="iconfont icon-star" style="font-size: 26px;"></i> 评分
            </el-button>
            <el-button class="buy-btn" type="primary" @click="showCinemas" style="font-size: 22px;">
              <i class="el-icon-shopping-cart-2" style="font-size: 24px;"></i> 查看影院信息
            </el-button>

          </div>


          <div class="movie-info-score">
            <div class="movie-index reputation-container">
              <span class="movie-index-title">口碑</span>
              <el-rate
                v-if="ratingCount > 0"
                v-model="reputationScore"
                disabled
                show-score
                text-color="#ff9900"
                score-template="{value}">
              </el-rate>
              <div v-else>{{ reputationScore }}</div>
              <div>{{ ratingCount }}人评价</div>

            </div>
            <div class="movie-index box-office-container">
              <span class="movie-index-title">累计票房</span>
              <div style="display: flex; align-items: flex-end;">
                <span class="box-office">{{movieInfo.movieBoxOffice}}</span>
                <span class="unit">元</span>
              </div>
            </div>
          </div>
        </div>

      </div>




    </div>


    <div class="movie-info-detail-container">
      <div class="movie-info-detail clearfix">
        <div class="main-content">
          <div class="crumbs-nav-container">
            <a href="/welcome">线上购票平台</a> &gt; <a href="/movie">电影</a> &gt; {{movieInfo.movieName}}
          </div>
          <el-tabs v-model="activeName">
            <el-tab-pane label="介绍" name="introduction">
              <div class="tab-body">
                <!-- 剧情简介 -->
                <div class="module">
                  <div class="mod-title">
                    <h2>剧情简介</h2>
                  </div>
                  <div class="mod-content">
                    <span class="dra">
                      {{movieInfo.movieIntroduction}}
                    </span>
                  </div>
                </div>
                <!-- 图集 -->
                <div class="module">
                  <div class="mod-title">
                    <h2>图集</h2>
                    <a class="more" @click="showPictures">全部</a>
                    <!-- 添加评论按钮 -->
                    <a class="more" @click="showReviews">评论</a>

                  </div>
                  <div class="mod-content">
                    <div class="pictures-list">
                      <div v-if="movieInfo.moviePictures.length === 0">
                        暂无图片资源
                      </div>
                      <el-image
                        class="default-img"
                        :src="movieInfo.moviePictures[0]"
                        :preview-src-list="movieInfo.moviePictures"
                        v-if="movieInfo.moviePictures.length > 0">
                      </el-image>
                      <div class="little-pictures">
                        <el-image
                          class="default-img"
                          v-for="(item,index) in movieInfo.moviePictures.slice(1)"
                          :key="index"
                          :src="item"
                          :preview-src-list="movieInfo.moviePictures">
                        </el-image>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </el-tab-pane>
            <el-tab-pane label="图集" name="pictures">
              <div class="tab-body">
                <div class="pictures-list">
                  <div class="little-pictures">
                    <div v-if="movieInfo.moviePictures.length == 0">
                      暂无图片资源
                    </div>
                    <el-image
                      fit="cover"
                      class="default-img"
                      v-for="(item,index) in movieInfo.moviePictures"
                      :key="index"
                      :src="item"
                      :preview-src-list="movieInfo.moviePictures">
                    </el-image>
                  </div>
                </div>
              </div>
            </el-tab-pane>
            <el-tab-pane label="评论" name="reviews">
              <div class="tab-body">
                <div v-if="reviews && reviews.length === 0">
                  暂无评论
                </div>
                <div v-for="(review, index) in reviews" :key="index" class="review-item">
                  <p><strong>评分: </strong>{{ review.rating }}</p>
                  <p><strong>评论: </strong>{{ review.comment }}</p>
                  <p><small>{{ formatDate(review.createdAt) }}</small></p>
                  <hr>
                </div>
              </div>
            </el-tab-pane>
            <el-tab-pane label="影院" name="cinemas">
              <div class="cinema-list">
                <div v-if="cinemas && cinemas.length === 0">
                  暂无影院信息
                </div>
                <ul v-else>
                  <li v-for="cinema in cinemas" :key="cinema.cinemaId" class="cinema-item">
                    <div class="cinema-info">
                      <p><strong>{{ cinema.name }}</strong></p>
                      <p>{{ cinema.address }}</p>
                      <div class="cinema-tags">
                        <el-tag v-for="tag in cinema.tags" :key="tag" type="info">{{ tag }}</el-tag>
                      </div>
                    </div>
                    <div class="cinema-pricing">
                      <span class="price">￥{{ cinema.price }}起</span>
                      <el-button class="buy-btn1" type="primary" @click="toChooseSession(cinema.cinemaId)" style="font-size: 22px;">
                        <i class="el-icon-shopping-cart-2" style="font-size: 24px;"></i> 选座购票
                      </el-button>
                    </div>
                  </li>
                </ul>
              </div>
            </el-tab-pane>



          </el-tabs>
        </div>
      </div>
    </div>

    <!-- 评分对话框 -->
    <el-dialog title="电影评分" :visible.sync="ratingDialogVisible">
      <div>
        <el-rate v-model="userRating" :colors="colors" show-score></el-rate>
      </div>
      <div>
        <el-input type="textarea" v-model="userComment" placeholder="请输入对电影的评价"></el-input>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="ratingDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitRating">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import movieItem from './../../components/movie/movie-item';
import moment from 'moment'

export default {
  name: "MovieInfo",
  components:{
    movieItem
  },
  data() {
    return {
      ratingCount: 0, // 存储评价人数
      movieInfo: {
        moviePictures: []
      },
      movieId: this.$route.params.movieId,
      activeName: 'introduction',
      ratingDialogVisible: false, // 控制评分对话框的显示与隐藏
      userRating: 0, // 存储用户评分
      userComment: '', // 存储用户评价
      colors: ['#99A9BF', '#F7BA2A', '#FF9900'], // 评分星星颜色
      reputationScore: "暂无评分", // 初始值设为"暂无评分"
      reviews: [], // 存储评论列表
      showRatingProgress: true, // 控制评分进度条的显示与隐藏
      computed: {
        // reputationScore 在 0-5 分之间，这里需要乘以20转换为百分比
        percentage() {
          return this.reputationScore * 20;
        }
      },
      cinemas: [
        {
          cinemaId: 1,
          name: '济南影院',
          tags: ['IMAX巨幕厅', '激光厅'],
          address: '济南市',
          price: 40
        },
        {
          name: "高新影院",
          tags: ["IMAX巨幕厅", "巨幕激光厅"],
          address: '济南市',
          price: "40",
        },
      ],
      httpURL: this.global.base
    }
  },
  created() {
    this.getMovieInfo();
    this.fetchReviews(); // 页面加载时获取评论

  },
  methods: {
    showCinemas() {
      this.activeName = 'cinemas';  // 切换到“影院”选项卡
    },
    fetchCinemas() {
      this.$axios.get('/sysCinema')
        .then(response => {
          console.log('Response from server:', response); // 调试信息
          if (response.data.code === 200) {
            this.cinemas = response.data.data || [];
            console.log('Fetched cinemas:', this.cinemas); // 调试信息
          } else {
            this.$message.error('获取影院信息失败');
          }
        })
        .catch(error => {
          console.error('Error fetching cinemas:', error);
          this.$message.error('获取影院信息失败');
        });
    },
    goToChooseSession(cinemaId) {
      this.$router.push({ name: 'ChooseSession', params: { cinemaId } });

  },
  mounted() {
    this.fetchCinemas();
  },
    async getMovieReputation() {
      try {
        const { data } = await axios.get(`/api/reviews/average-rating/${this.movieId}`);
        if (data.ratingCount > 0) {
          this.reputationScore = parseFloat(data.averageRating).toFixed(1);
          this.ratingCount = data.ratingCount;
        } else {
          this.reputationScore = "暂无评分";
          this.ratingCount = 0;
        }
      } catch (error) {
        console.error('Failed to fetch movie reputation:', error);
        this.$message.error('获取电影口碑失败');
      }
    },
    async getMovieInfo(){
      await this.getMovieReputation();  // 获取电影口碑
      const _this = this
      const {data : res} = await axios.get('sysMovie/find/' + this.movieId)
      if(res.code !== 200) return this.$message.error('数据查询失败')
      this.movieInfo = res.data
      this.movieInfo.moviePoster = this.httpURL + JSON.parse(res.data.moviePoster)[0]
      this.movieInfo.moviePictures = JSON.parse(this.movieInfo.moviePictures).map((obj, index) => {
        return this.httpURL + obj
      })
      this.movieInfo.movieCategoryList = this.movieInfo.movieCategoryList.map((obj,index) => {
        return obj.movieCategoryName;
      }).join(" ")
    },
    async fetchReviews() {
      try {
        const response = await axios.get(`/api/reviews/${this.movieId}`);
        this.reviews = response.data;
      } catch (error) {
        this.$message.error('获取评论失败');
        console.error(error);
      }
    },
    showPictures(){
      this.activeName = 'pictures'
    },
    showReviews() {
      this.activeName = 'reviews';
      // 点击评论时，不再需要重新获取评论，已在页面加载时获取
    },
    // 格式化日期方法
    formatDate(dateString) {
      return moment(dateString).format('YYYY-MM-DD HH:mm');
    },
    // 打开评分对话框
    openRatingDialog() {
      this.ratingDialogVisible = true;
    },
    // 提交评分
    submitRating() {
      if (this.userRating === 0) {
        return this.$message.error('请选择评分');
      }
      if (this.userComment.trim() === '') {
        return this.$message.error('请输入评价');
      }

      const review = {
        movieId: this.movieId, // 电影ID
        userId: 1, // 用户ID，实际使用中从用户会话中获取
        rating: this.userRating,
        comment: this.userComment,
        createdAt: new Date().toISOString()
      };

      axios.post('/api/reviews/add', review)
        .then(response => {
          this.$message.success('提交成功');
          this.ratingDialogVisible = false;
          this.userRating = 0;
          this.userComment = '';
          this.fetchReviews(); // 提交成功后重新获取评论
        })
        .catch(error => {
          this.$message.error('提交失败');
          console.error(error);
        });
    },
    toChooseSession1(){
      let cinemaId = 2
      this.$router.push('/chooseSession1/' + cinemaId)
    },
    //转到购票页面
    toChooseSession(){
      let cinemaId = 1
      this.$router.push('/chooseSession/' + cinemaId)
    }
  }
}
</script>


<style scoped>
.movie-container{

}

a{
  text-decoration: none;
  cursor:pointer;
}

.header{
  padding: 0;
  width: 100%;
  min-width: 1200px;
  /* background: url('../../assets/movie-info-background.jpg') */

  background: rgba(0, 0, 0, 0.84)
}

.header-inner{
  width: 1200px;
  margin: 0 auto;
  position: relative;
}

.clearfix::before, .clearfix::after{
  content: " ";
  display: table;
}

.clearfix::after{
  clear: both;
}

.movie-info-left{
  width: 300px;
  height: 370px;
  float: right;
  position: relative;
  top: 20px;
  overflow: hidden;
  z-index: 9;
  right: -200px;
}

.styled-table tbody tr:last-of-type {
  border-bottom: 2px solid #009879;
}

.styled-table tbody tr.active-row {
  font-weight: bold;
  color: #009879;
}


  .avatar-shadow{
  position: relative;
  margin: 0 30px;
  width: 240px;
  height: 330px;
  padding-bottom: 40px;
  background: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAPAAAAAyAgMAAAAfG76+AAAADFBMVEUAAAAAAAAAAAAAAAA16TeWAAAABHRSTlMOAgoGQ0SIeAAAADpJREFUSMdjGAWjYBRgAasoAAwdFACKbB7VPEI076YAUGbzfwrAqOYRormcAjCANodSAEY1j2oexJoBlx1+yE7RXIIAAAAASUVORK5CYII=) no-repeat bottom;
}

.avatar{
  border: 4px solid #fff;
  height: 322px;
  width: 232px;
}

.movie-info-msg{
  position: absolute;
  color: #ffffff;
  font-size: 14px;
  z-index: 1;
  left: 40px; /* 将组件移到页面左边 */
  top: 80px;
}
.movie-name{
  width: 900px;
  margin-top: 0;
  font-size: 33px;
  line-height: 32px;
  font-weight: 700;
  margin-bottom: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  max-height: 64px;
}

.movie-ename{
  width: 340px;
  font-size: 18px;
  line-height: 1.3;
  margin-bottom: 14px;
}

ul{
  width: 250px;
  list-style: none;
  padding-left: 0;
  margin-bottom: 20px;
}

ul li{
  margin: 12px 0;
  line-height: 100%;
}

.movie-info-btn{
  position: absolute;
  bottom: 20px;

}

.buy-btn{
  margin-top: 10px;
  width: 250px;
  font-size: 16px;
  text-align: center;

}

.movie-info-score{
  position: absolute;
  top: 145px;
  left: 360px;
}

.movie-index {
  margin-bottom: 16px;
  color: #ffffff;
}

.box-office-container{
  display: flex;
  flex-direction: column;
}

.movie-index-title{
  font-size: 12px;
  margin-bottom: 8px;
}

.movie-score-num{
  font-size: 30px;
  color: #ffc600;
  height: 30px;
  line-height: 30px;
  margin-right: 10px;
}

.score-container{
  display: flex;
  flex-direction: column;
  align-items: center;
  font-size: 12px;
}

.box-office{
  font-size: 30px;
}

.unit{
  font-size: 12px;
  padding-left: 1px;
  line-height: 24px;
}

.movie-info-right{
  height: 300px;
  position: relative;
  margin-right: 30px;
  margin-left: 300px;
  margin-top: 70px;
}

.movie-info-detail-container{
  width:1200px;
  margin: 0 auto;
}

.movie-info-detail{
  margin-top: 80px;
}

.main-content {
  width: 730px;
  float: left;
  margin-bottom: 20px;
}

.crumbs-nav-container {
  margin-bottom: 25px;
  color: #333;
}

.crumbs-nav-container a{
  color: inherit;
}

.el-tabs >>> .el-tabs__item{
  font-size: 20px;
}

.tab-body{
  margin-top: 40px;
}

.module{
  position: relative;
  margin-bottom: 60px;
}

.mod-title h2{
  display: inline-block;
  margin: 0;
  padding: 0;
  font-weight: 400;
  font-size: 18px;
  color: #333;
  line-height: 18px;
}

.mod-title h2:before {
  float: left;
  content: "";
  display: inline-block;
  width: 4px;
  height: 18px;
  margin-right: 6px;
  background-color: #409EFF;
}

.mod-content{
  margin-top: 20px;
  color: #333;
}

.mod-content .dra{
  font-size: 14px;
  line-height: 26px;
}

.more{
  float: right;
  cursor: pointer;
  font-size: 14px;
  color: #999;
  padding-right: 14px;
  background: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAgAAAAOCAYAAAASVl2WAAAABmJLR0QA/wD/AP+gvaeTAAAAv0lEQVQY013RTUpDQRAE4G8eghcR8ScgKCIugpJFjuIjqAvBc7jxj0muEnCjiIQQJOImB3GnbnpkfL1qpqqrunpSzvkDPxjhGdq2VarBF3q4wRHknP8RzvCEQzzguCalaHZwiwHecY6XogCf8TjFHh7Rh9Tx3AylIZa4TgWpSBuY4BSrYlFXKsr4bjrTW5HkJJa9SBW4jbtukmKxG5MDLOKqfzEPcB9LzQN8LSdfwxj7eMMlZvV/NFiPzFddEH4Bt5Y1mf3fnDwAAAAASUVORK5CYII=) no-repeat 100%;
}

.portrait{
  margin-bottom: 6px;
  width: 128px;
  height: 170px;
  overflow: hidden;
}

.portrait .default-img{
  width: 128px;
  height: 170px;
}

.pictures-list{
  display: flex;
}

.pictures-list>.el-image:first-child{
  width: 465px;
  height: 258px;
}

.pictures-list .default-img{
  border-style: none;
  cursor: pointer;
  width: 126px;
  height: 126px;
}

.little-pictures{
  width: 262px;
  height: 262px;
  display: flex;
  flex-wrap: wrap;
  margin-left: 10px;
  justify-content: space-between;
}

#pane-pictures .little-pictures{
  width: 100%;
  justify-content: flex-start;
  margin-left: 0;
}

#pane-pictures .default-img{
  margin-top: 10px;
  margin-left: 10px;
}

.user-avatar{

  margin-right: 20px;
}


.main-header{
  font-size: 16px;
}

.user-name{
  margin-top: 2px;
}
.cinema-list {
  display: flex;
  flex-direction: column;
  width: 100%;
}

.cinema-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 30px;
  margin: 10px 0;
  background-color: #fff;
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  border-left: 5px solid red;
  width: 250%;
}

.cinema-info {
  flex: 3;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.cinema-tags {
  margin-top: 10px;
}

.cinema-pricing {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  justify-content: center;
  margin-left: auto; /* 添加此行将价格和按钮对齐到右边 */
}

.price {
  color: red;
  font-size: 22px;
  font-weight: bold;
  margin-bottom: 10px;
}

.buy-btn1 {
  font-size: 14px;
  background-color: #ff4c4c;
  border-color: #ff4c4c;
  padding: 6px 10px; /* 调整内边距，减小按钮宽度 */
  border-radius: 3px;
  width: auto; /* 确保按钮宽度自适应内容 */
  transition: all 0.3s;
}

.buy-btn1:hover {
  background-color: #ff3333;
  border-color: #ff3333;
}



.progress-control {
  position: fixed;
  top: 200px; /* 自定义你想要的位置 */
  right: 1500px; /* 自定义你想要的位置 */
  z-index: 1000; /* 确保它在其他元素的前面 */

}

</style>
