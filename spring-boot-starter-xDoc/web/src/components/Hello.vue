<template>
    <div class="hello">

    </div>
</template>

<script>
export default {
  name: 'hello',
  data () {
    return {
      msg: 'Welcome to Your Vue.js App'
    }
  },
  methods : {
    clickMe : function () {
      this.$http({
        url : '/api/user/test',
        method : 'post',
        params : {
          id : '583924c946501436dcc17270'
        }
      }).then((resp) => {
        console.log(resp)
        if(resp.ok && resp.data) {
            alert(JSON.stringify(resp.data));
        } else {
            alert('请求失败');
        }
      }, (resp) => {
        alert('系统错误');
      });
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h1, h2 {
  font-weight: normal;
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  display: inline-block;
  margin: 0 10px;
}

a {
  color: #42b983;
}
</style>
