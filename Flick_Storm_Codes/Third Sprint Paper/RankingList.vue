
<template>
    <div>
        <el-menu
            :default-active="$route.path"
            class="el-menu-demo"
            mode="horizontal"
            background-color="#545c64"
            text-color="#fff"
            active-text-color="#ffd04b"
            router
        >
            <el-menu-item
                v-for="item in menuList"
                :key="item.id"
                :index="item.path"
            >
                <i :class="item.icon" style="font-size: 36px"> </i>
                <b style="font-size: 22px"> {{ item.name }}</b>
            </el-menu-item>
        </el-menu>
        <router-view></router-view>
    </div>
</template>

<script>
export default {
    data() {
        return {
            menuList: [
                {
                    id: 1,
                    name: "票房榜",
                    path: "/rankingList/totalBoxOfficeList",

                },
                {
                    id: 2,
                    name: "好评榜",
                    path: "/rankingList/domesticBoxOfficeList",

                },

            ],
        };
    },
    methods: {},
};
</script>

<style scoped>
.el-menu {
    display: flex;
    justify-content: space-between;
}

.el-menu-item {
    font-size: 16px;
}
</style>
