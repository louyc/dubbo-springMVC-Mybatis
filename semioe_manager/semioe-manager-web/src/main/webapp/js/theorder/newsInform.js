window.onload=function(){
    layui.use('element',function(){
        var $=layui.jquery,
            element=layui.element();
        var active={
            tabChange:function(){
                // element.tabChange();
            }
        };
        $('.site-demo-active').on('click', function(){
            var othis = $(this), type = othis.data('type');
            active[type] ? active[type].call(this, othis) : '';
        });
        var layid = location.hash.replace(/^#test=/, '');
        element.tabChange('test', layid);

        element.on('tab(test)', function(elem){
            location.hash = 'test='+ $(this).attr('lay-id');
        });

        


    })
}