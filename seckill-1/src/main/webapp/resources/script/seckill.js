/**
 * javascript 模块化
 */
const seckill = {
    /**
     * 封装秒杀相关请求的 URL
     */
    URL: {
        now: function () {
            return '/seckill/time/now';
        },
        exposer: function (seckillId) {
            return '/seckill/' + seckillId + '/exposer';
        },
        execution: function (seckillId, md5) {
            return '/seckill/' + seckillId + '/' + md5 + '/execution';
        }
    },

    /**
     * 验证手机号
     */
    validatePhone: function (phone) {
        return !!(phone && phone.length === 11 && !isNaN(phone));
    },

    /**
     * 秒杀
     */
    handleSeckillKill: function (seckillId, node) {
        console.log(2)
        // 添加秒杀按钮
        node.hide().html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');
        $.post(seckill.URL.exposer(seckillId), {}, function (result) {
            if (result && result['success']) {
                const exposer = result['data'];
                if (exposer['exposed']) {
                    /* 开启秒杀 */
                    // 获取秒杀地址
                    const md5 = exposer['md5'];
                    const killUrl = seckill.URL.execution(seckillId, md5);
                    // 绑定一次点击事件，防止多次点击多次发送
                    $('#killBtn').one('click', function () {
                        /* 执行秒杀请求 */
                        // 1.先禁用按钮
                        $(this).addClass('disabled');
                        // 2.发送秒杀请求执行秒杀
                        $.post(killUrl, {}, function (result) {
                            if (result && result['success']) {
                                const killResult = result['data'];
                                const state = killResult['state'];
                                const stateInfo = killResult['stateInfo'];
                                // 3.显示秒杀结果
                                node.html('<span class="label label-success">' + stateInfo + '</span>');
                            }
                        });
                    });
                    node.show();
                } else {
                    /* 未开启秒杀 */
                    const now = exposer['now'];
                    const start = exposer['start'];
                    const end = exposer['end'];
                    // 重新倒计时
                    seckill.countdown(seckillId, now, start, end);
                }
            } else {
                console.log(1)
                console.log('result:' + result);
            }

        });

    },

    /**
     * 倒计时
     */
    countdown: function (seckillId, nowTime, startTime, endTime) {
        const $seckillBox = $('#seckill-box');
        if (nowTime > endTime) {
            /* 秒杀结束 */
            $seckillBox.html('秒杀结束');
        } else if (nowTime < startTime) {
            /* 秒杀未开始 */
            // 绑定倒计时事件
            const killTime = new Date(startTime + 1000);
            $seckillBox.countdown(killTime, function (event) {
                // 时间格式
                const format = event.strftime('秒杀倒计时：%D天 %H时 %M分 %S秒');
                $seckillBox.html(format);
            })
                // 倒计时完成后回调事件
                .on('finish.countdown', function () {
                    seckill.handleSeckillKill(seckillId, $seckillBox);
                });
        } else {
            /* 秒杀开始 */
            seckill.handleSeckillKill(seckillId, $seckillBox);
        }
    },

    detail: {
        /**
         * 详情页初始化
         */
        init: function (params) {
            /* 验证手机号 */
            // 在 cookie 中查找手机号
            const killPhone = $.cookie('killPhone');
            if (!seckill.validatePhone(killPhone)) {
                /* 绑定 phone */
                const $killPhoneModal = $('#killPhoneModal');
                // 显示弹出层：https://v3.bootcss.com/javascript/#modals-options
                $killPhoneModal.modal({
                    // 指定一个静态的背景，当用户点击模态框外部时不会关闭模态框
                    backdrop: 'static',
                    // 键盘上的 ESC 键被按下时关闭模态框
                    keyboard: false
                });
                $('#killPhoneBtn').click(function () {
                    const inputPhone = $('#killPhoneKey').val();
                    if (seckill.validatePhone(inputPhone)) {
                        // inputPhone 写入 cookie
                        $.cookie('killPhone', inputPhone, {expires: 7, path: '/seckill'});
                        // 刷新页面
                        location.reload();
                    } else {
                        $('#killPhoneMessage').hide().html('<label class="label label-danger">手机号错误！</label>').show(300);
                    }
                });
            }
            /* 计时交互 */
            const seckillId = params['seckillId'];
            const startTime = params['startTime'];
            const endTime = params['endTime'];
            $.get(seckill.URL.now(), {}, function (result) {
                if (result && result['success']) {
                    const nowTime = result['data'];
                    seckill.countdown(seckillId, nowTime, startTime, endTime)
                } else {
                    console.log('result:' + result);
                }
            })

        }
    }
};