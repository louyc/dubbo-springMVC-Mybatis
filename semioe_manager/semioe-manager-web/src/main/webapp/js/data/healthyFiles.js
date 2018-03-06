layui.use(['layer','jquery','element'], function(){
    var $ = layui.jquery;
    var layer = layui.layer,element = layui.element();
    
    var managerId = window.utils.getRequestParam("managerId");
    element.on('tab(filter)', function(data){
    	if(this.type=="1"){
    		getFileData();
    	}else{
    		getYunfuData();
    	}
	});
    getFileData();
    function getYunfuData(){
    	$.ajax({
    	url: '/motherhood/getMotherhoodRecordById?id='+managerId,
    	type: 'GET',
        contentType: "application/json",
    	success:function(data){
    		if(data.successful){
    			if(!data.data){
    				layer.msg("暂无数据")
    			}else{
    				data.data.maritalStatus = data.data.maritalStatus==1?"是":"否";
    				data.data.husbandSingle = data.data.husbandSingle==1?"是":"否";
    				data.data.husbandReplace = data.data.husbandReplace==1?"是":"否";
    				data.data.isFarmer = data.data.isFarmer==1?"是":"否";
    				data.data.isChildbirthAllowance = data.data.isChildbirthAllowance==1?"是":"否";
    				data.data.isMoreSixMonths = data.data.isMoreSixMonths==1?"是":"否";
    				data.data.isMoreOneYear = data.data.isMoreOneYear==1?"是":"否";
    				data.data.screeningResults = data.data.screeningResults==1?"是":"否";
    				data.data.isPrenatalExamination = data.data.isPrenatalExamination==1?"是":"否";
    				data.data.isHivDetectionCard = data.data.isHivDetectionCard==1?"是":"否";
    				data.data.isBVerification = data.data.isBVerification==1?"是":"否";
    				data.data.gestationTimes = data.data.gestationTimes==1?"是":"否";
    				data.data.isFolicAcid = data.data.isFolicAcid==1?"是":"否";
    				data.data.isLaw = data.data.isLaw==1?"是":"否";
    				data.data.medicineType = data.data.medicineType?JSON.parse(data.data.medicineType):{"itemName":""};
    				data.data.familyHistoryDesc="";
    				data.data.personalHistoryDesc="";
    				data.data.pastHistoryDesc="";
    				var familyHistory =JSON.parse(data.data.familyHistory);
    				var personalHistory =JSON.parse(data.data.personalHistory);
    				var pastHistory =JSON.parse(data.data.pastHistory);
    				if(familyHistory=="0"){
    					data.data.familyHistoryDesc = "";
    				}else{
    					for(var item of familyHistory){
        					if(!!data.data.familyHistoryDesc){
        						data.data.familyHistoryDesc+=",";
        					}
        					data.data.familyHistoryDesc+=item.itemName;
        				}
    				}
    				if(personalHistory=="0"){
    					data.data.familyHistoryDesc = "";
    				}else{
    					for(var item of personalHistory){
        					if(!!data.data.personalHistoryDesc){
        						data.data.personalHistoryDesc+=",";
        					}
        					data.data.personalHistoryDesc+=item.itemName;
        				}
    				}
    				if(pastHistory=="0"){
    					data.data.familyHistoryDesc = "";
    				}else{
    					for(var item of pastHistory){
        					if(!!data.data.pastHistoryDesc){
        						data.data.pastHistoryDesc+=",";
        					}
        					data.data.pastHistoryDesc+=item.itemName;
        				}
    				}
    				
    				/*data.data.personalHistory = data.data.personalHistory==1?"是":"否";
    				data.data.pastHistory = data.data.pastHistory==1?"是":"否";
    				data.data.isHighRisk = data.data.isHighRisk==1?"是":"否";*/
    				var sHtml1 =template('yunfudata', {
    					data : data.data
    				});
    				$('#yunfu').html(sHtml1);
    			}
    		}else{
    			layer.msg(data.resultCode.message)
    		}
        }
    });
    }
    
    function getFileData(){
    	$.ajax({
        	url: '/api/contracte/queryUserArchives?managerId='+managerId,
        	type: 'POST',
            contentType: "application/json",
        	success:function(data){
        		if(data.successful){
        			if(!data.data){
        				layer.msg("暂无数据");
        			}else{
        				data.data.jibing = [],data.data.shoushu = [],data.data.waishang=[],data.data.shuxue=[];
        				data.data.father = [],data.data.monther = [],data.data.brother = [],data.data.children=[];
        				data.data.yichuanbingshi = "",data.data.canjiqingkuang = "",data.data.guominshi="";
        				data.data.paifeng = "",data.data.ranliao = "",data.data.yinshui = "",data.data.cesuo = "",data.data.qinchu = "";
        				$(data.data.dataList).each(function(){
        					var itemTypeId = this.itemTypeId;
        					var arr = {};
        					arr.time = this.time;
        					arr.name = this.itemName;
        					switch(itemTypeId){
        					//疾病
        					case 3:
        						data.data.jibing.push(arr);
        						break;
        					case 21:
        						data.data.shoushu.push(arr);
        						break;
        					case 22:
        						data.data.waishang.push(arr);
        						break;
        					case 23:
        						data.data.shuxue.push(arr);
        						break;
        					case 25:
        						data.data.father.push(arr);
        						break;
        					case 26:
        						data.data.monther.push(arr);
        						break;
        					case 27:
        						data.data.brother.push(arr);
        						break;
        					case 28:
        						data.data.children.push(arr);
        						break;
        					case 24:
        						if(data.data.yichuanbingshi){
        							data.data.yichuanbingshi +=",";
        						}
        						data.data.yichuanbingshi += this.itemName;
        						break;
        					case 4:
        						if(data.data.canjiqingkuang){
        							data.data.canjiqingkuang +=",";
        						}
        						data.data.canjiqingkuang += this.itemName;
        						break;
        						//厨房排风
        					case 5:
    							if(data.data.paifeng){
        							data.data.paifeng +=",";
        						}
    							data.data.paifeng += this.itemName;
        						break;
        						//燃料类型
        					case 6:
        						if(data.data.ranliao){
        							data.data.ranliao +=",";
        						}
        						data.data.ranliao += this.itemName;
        						break;
        						//饮水
        					case 7:
        						if(data.data.yinshui){
        							data.data.yinshui +=",";
        						}
        						data.data.yinshui += this.itemName;
        						break;
        						//厕所
        					case 8:
        						if(data.data.cesuo){
        							data.data.cesuo +=",";
        						}
        						data.data.cesuo += this.itemName;
        						break;
        						//禽畜栏
        					case 8:
        						if(data.data.qinchu){
        							data.data.qinchu +=",";
        						}
        						data.data.qinchu += this.itemName;
        						break;
        						//药物过敏史
        					case 18:
        						if(data.data.guominshi){
        							data.data.guominshi +=",";
        						}
        						data.data.guominshi += this.itemName;
        						break;
        					default:
        						break;
        					}
        				});
        				var sHtml =template('healthyFileData', {
        					data : data.data
        				});
        				$('#healthyFiles').html(sHtml);
        			}
        		}else{
        			layer.msg("查询出错！")
        		}
        	}
        })
    }
});
