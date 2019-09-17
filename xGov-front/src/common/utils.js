import moment from 'moment'
export default {
    dateFormat:function(row, column) {
        let date = row[column.property];
        if (date == undefined) {
            return "";
        }
        return moment(date).format("YYYY-MM-DD HH:mm:ss");
    },
    dateFormatTwo:function(val){
        if(!val){
            return "";
        }else{
            let date = new Date(val);
            let Y = date.getFullYear() + '-';
            let M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
            let D = (date.getDate()< 10 ? '0' + date.getDate() : date.getDate())+' ';
            let h = (date.getHours()< 10 ? '0' + date.getHours() : date.getHours()) +':';
            let m = (date.getMinutes()< 10 ? '0' + date.getMinutes() : date.getMinutes()) +':'
            let s = date.getSeconds()< 10 ? '0' + date.getSeconds() : date.getSeconds()
            let ss = date.getMilliseconds();
            return Y+M+D+h+m+s;
        }
    },
    moveBall(moveArr, target, timer) {  //运动小球
        let  ball = $('<div class="ball"></div>');
        target.append(ball);
        timer = timer ? timer : Math.random() * 1 + 0.5;
        TweenLite.to(ball, 0, {							//第一个参数是需要缓动的对象，第二个参数是持续时间，第三个是需要改变的对象属性
            left: moveArr[0].x - 5,
            top: moveArr[0].y - 5,
            opacity: 1
        });
        let tl = new TimelineLite();
        moveArr.forEach(function(item, i) {
            tl.add(TweenLite.to(ball, timer, {
                ease: Power0.easeOut,
                left: item.x - 5,
                top: item.y - 5,
                onComplete: function() {
                    if (i >= moveArr.length - 1) {
                        tl.play(0.5);
                    }
                }
            }));
        });
    },
};
