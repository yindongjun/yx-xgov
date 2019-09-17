import html2Canvas from 'html2canvas';
import JsPDF from 'jspdf';

export default {
    install(Vue, options) {
        Vue.prototype.$getPdf = function(dom, name = +new Date()) {
            html2Canvas(document.querySelector(dom), {
                allowTaint: true,
                scale: 2 // 提升画面质量，但是会增加文件大小
            }).then(function(canvas) {
                /** jspdf整体思路：
                 * 1. 获取DOM
                 * 2. 将DOM转换为canvas
                 * 3. 获取canvas的宽度、高度（稍微大一点）
                 * 4. 将pdf的宽高设置为canvas的宽高
                 * 5. 将canvas转为图片
                 * 6. 实例化jspdf，将内容图片放在pdf中（因为内容宽高和pdf宽高一样，就只需要一页，也防止内容截断问题）
                 */

                // 得到canvas画布的单位是px 像素单位
                let contentWidth = canvas.width;
                let contentHeight = canvas.height;
                // 将canvas转为base64图片
                let pageData = canvas.toDataURL('image/jpeg', 1.0);
                // 设置pdf的尺寸，pdf要使用pt单位 已知 1pt/1px = 0.75   pt = (px/scale)* 0.75
                let pdfX = ((contentWidth + 10) / 2) * 0.75;
                let pdfY = ((contentHeight + 500) / 2) * 0.75; // 500为底部留白
                let pdfs = [pdfX, pdfY]
                // 设置内容图片的尺寸，img是pt单位
                let imgX = pdfX;
                let imgY = (contentHeight / 2) * 0.75; //内容图片这里不需要留白的距离
                const gapHeight = (contentHeight / 2) * 0.75
                if (gapHeight < 841.89) {
                    pdfs = 'a4';
                    imgX = 595.28;
                    imgY = 592.28 / contentWidth * contentHeight;
                }
                // 初始化jspdf 第一个参数方向：默认''时为纵向，第二个参数设置pdf内容图片使用的长度单位为pt，第三个参数为PDF的大小，单位是pt(无论前面的单位是什么in cm pt，这里的pdf的长度单位都是按照pt，前面参数的意义是内容的尺寸单位)
                let PDF = new JsPDF('', 'pt', pdfs);
                // 将内容图片添加到pdf中，因为内容宽高和pdf宽高一样，就只需要一页，位置就是 0,0
                PDF.addImage(pageData, 'jpeg', 0, 0, imgX, imgY);
                PDF.save(name + '.pdf');
            });
        };
    }
};
