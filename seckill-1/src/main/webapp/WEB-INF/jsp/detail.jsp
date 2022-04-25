<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="common/jstl.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>秒杀列详情页</title>
    <%@ include file="common/head.jsp" %>
</head>
<body>
<div class="container">
    <div class="panel panel-default text-center">
        <div class="panel-heading">
            <h1>${seckill.name}</h1>
        </div>
        <div class="panel-body">
            <h2 class="text-danger">
                <%-- time 图标 --%>
                <span class="glyphicon glyphicon-time"></span>
                <%-- 倒计时 --%>
                <span class="glyphicon" id="seckill-box"></span>
            </h2>
        </div>
    </div>
</div>

<%-- 登录弹出层，输入电话 --%>
<div class="modal fade" id="killPhoneModal">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h3 class="modal-title text-center">
                    <span class="glyphicon glyphicon-phone"></span>
                </h3>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-xs-8 col-xs-offset-2">
                        <input type="text" name="killPhone" id="killPhoneKey" placeholder="填手机号^o^"
                               class="form-control">
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <%-- 验证信息 --%>
                <span id="killPhoneMessage" class="glyphicon"></span>
                <button type="button" id="killPhoneBtn" class="btn btn-success">
                    <span class="glyphicon glyphicon-phone"></span>
                    Submit
                </button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>

<script src="https://cdn.bootcdn.net/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script src="https://cdn.bootcdn.net/ajax/libs/twitter-bootstrap/3.4.1/js/bootstrap.min.js"></script>
<script src="https://cdn.bootcdn.net/ajax/libs/jquery-cookie/1.4.1/jquery.cookie.js"></script>
<script src="https://cdn.bootcdn.net/ajax/libs/jquery.countdown/2.2.0/jquery.countdown.min.js"></script>
<script src="/resources/script/seckill.js" type="text/javascript"></script>
<scirpt src="/seckill.js" type="text/javascript"></scirpt>
<script type="text/javascript">
    $(function () {
        // 使用 EL 表达式传入参数
        seckill.detail.init({
            seckillId: ${seckill.seckillId},
            // 开始时间（毫秒）
            startTime: ${seckill.startTime.time},
            endTime: ${seckill.endTime.time}
        });
    });
</script>
</body>
</html>
