<template>
    <div class="container">
        <div  class="item_container" v-for="event in events" :key="event.id" >
            <div>
                <div class="img_container">
                    <img :src="event.imgUrl" :alt="event.title" @click.prevent="goToEventDetail(event.id)">  
                    <!-- <img :src="scrapImgUrl" class="scrap-button" @click="onScrap"></img> -->
                    <img :src="event.wish? checkImg:unCheckImg" class="scrap-button" @click.prevent="onScrap(event.id)"></img>   
                </div>     
            </div>         
            <div>
                <div class="date">{{ event.eventDate }}</div>
                <div class="title">{{ event.title }}</div>
                <div class="etc_container">
                    <div class="price">{{ event.price==0 ? '무료': event.price.toLocaleString() +" 원"}}</div>
                    <div class="view_count">조회 {{ event.views }}</div>
                </div>
            </div>
        </div>
    </div>
   
</template>

<script setup>
    import { computed, onMounted, ref } from 'vue';
    import scrapUnCheck from '../assets/images/scrap_uncheck.png';
    import scrapCheck from '../assets/images/scrap_check.png';
    import axios from 'axios';
    import { useRouter } from 'vue-router';

    const scrapImgUrl = ref(scrapUnCheck);
    const unCheckImg = ref(scrapUnCheck);
    const checkImg = ref(scrapCheck);

    const props = defineProps(['events']);
    const emit = defineEmits(['refresh-data']);
    const router = useRouter();

    // const events = ref(props.events);


    // onMounted(()=>{
    //     events.value = props.events;
    // });


    const onScrap =(eventNo)=>{
     //   scrapImgUrl.value = scrapCheck;
     // 로그인 여부 먼저 확인 
     // todo 스크랩 api 호출
     console.log('onScrap 클릭');
     const isLoggedIn = localStorage.getItem("isLoggedIn");
    
     
     if(isLoggedIn=="false"){
        alert("관심행사 등록은 로그인 후 이용해주세요");
        return;
     }
     // todo : 관심행사 등록 api 호출
     // api 재실행 
     const userInfo = localStorage.getItem("userInfo");
     const memberNo = JSON.parse(userInfo)['no'];

     console.log(`memberNo : ${memberNo}`);
     console.log(`eventNo : ${eventNo}`);

     scrapApi(memberNo,eventNo);

    }

    const scrapApi = async(memberNo,eventNo)=>{
        try{
                await axios.post('http://localhost:8080/v1/api/wish/register',{
                 memberNo,
                 eventNo
            }).then((res)=>{
                console.log(res);
                console.log(res.data);
                if(res.data=="register"){
                    emit('refresh-data');
                    alert("관심행사로 등록되었습니다.");
                }else if(res.data=="cancel"){
                    emit('refresh-data');
                    alert("관심행사 등록을 취소했습니다.")
                }
            
            })

        }catch(err){
            console.log(err);
        }

    }
    const goToEventDetail = (id)=>{
        router.push({ path: '/event', query: { id } });
    }
    
    
</script>

<style lang="scss" scoped>
    .container {
        // background-color: red;
        padding-left:0;
        padding-right: 0;
        display: grid;
        grid-template-columns: repeat(3, 1fr); 
        gap: 15px; /* 아이템 간의 간격 */

    }
    .item_container {
        position: relative;
        margin-bottom: 15px;
        // background-color: yellow;
        img{
            width: 100%;
        }
    }

    .date{
        font-size: 12px;
        color: gray;
    }

    .etc_container {
        // background-color: gray;
        display: flex;
        justify-content: space-between;
    }

    .title {
        font-size: 15px;
        font-weight: 500;
    }

    .price {
        font-size: 15px;
        color: purple;
    }

    .view_count {
        color: gray;
    }

    .scrap-button {
       position: absolute;
       top:0px;
       right:5px;
       width:35px !important;
       cursor: pointer;
     
    }
    img {
        width: 100%;
        height: 70%;
        border-radius: 5px;
        object-fit: cover;
        display: block;
        height: auto;
        transition: transform 0.3s ease, box-shadow 0.3s ease; /* 변환 및 그림자 효과를 부드럽게 적용 */
    }
     
    img:hover {
        transform: scale(1.1); /* 마우스를 올리면 이미지가 1.1배 확대 */
        box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3); /* 확대 시 그림자 추가 */
    }

</style>