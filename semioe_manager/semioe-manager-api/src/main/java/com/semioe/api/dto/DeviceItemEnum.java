package com.semioe.api.dto;

/**
 * 设备相关 枚举
 * 
 * Created by King L on 2017/4/18.
 */
public enum DeviceItemEnum {

	/**
	 * 设备类型 START
	 */
	KPJ("卡片机", "", ""),						// 卡片机
	YTJ("一体机", "", ""),						// 一体机
	
	XTY("血糖仪", "", ""),						// 血糖仪
	SH("蓝牙手环", "", ""),						// 蓝牙手环
	MY("免疫定量分析仪", "", ""),				// 免疫定量分析仪
	
	TWJ("体温计", "", ""),						// 体温计
	/**
	 * 设备类型 END
	 */

	/**
	 * 自填 测量数据 START
	 */
	SLEEP("睡眠", "", ""),						// 睡眠
	DRINK("饮酒", "", ""),						// 饮酒
	SMOKE("吸烟", "", ""),						// 吸烟
	MEDICINE("服药", "", ""),					// 服药
	LABORATORY_REPORT("化验单", "", ""),			// 化验单
	MEDICAL_IMAGE("医学影像", "", ""),			// 医学影像
	ABNORMAL("异常情况", "", ""),				// 异常情况
	SPORT("运动", "", ""),						// 运动
	/**
	 * 自填 测量数据 END
	 */
	
	/**
	 * 检测数据 START
	 */
	BMI("BMI", "", ""),							// BMI
	BP("血压", "", "mmHg"),						// 血压
	BO("血氧", "", "%"),							// 血氧
	HR("心率", "", "次/分"),						// 心率
	SN("步数", "", "步"), 						// 步数
	BG("血糖", "", "mmol/L"),					// 血糖
	
	RU("尿常规", "", ""),						// 尿常规
	CHOL("总胆固醇", "", "mmol/L"),				// 总胆固醇
	UA("尿酸", "", "mmol/L"),					// 尿酸
	BHB("血红蛋白", "", "g/dL"),					// 血红蛋白
	BF("血脂五项", "", ""),						// 血脂五项
	TE("体温", "", "℃"),						// 体温
	
	WP("水分比", "", "%"),                     	// 水分比
	MUCALE("肌肉", "", "kg"),					// 肌肉
	BONE("骨量", "", "kg"),						// 骨质量
	BMR("基础代谢率", "", "Kcal/d"),				// 代谢
	PIW("蛋白质", "", "kg"),						// 蛋白质质量
	FP("体脂", "", "%"),							// 体脂百分比
	
	BODY("体脂称", "", ""),						// 体脂称
	ECG("ECG", "", ""),							// ECG
	// 一体机 保留类型
	BR("血常规", "", ""),						// 血常规
	
	// 一体机检测列表
	ALLDATA("[BP,BG,RU,CHOL,UA,BHB,BF,TE,BODY,ECG,BO]", "[\"血压\",\"血糖\",\"尿常规\",\"总胆固醇\",\"尿酸\",\"血红蛋白\",\"血脂五项\",\"体温\",\"体脂称\",\"ECG\",\"血氧\"]", ""),
	// 检测列表
	CHECKlIST("[BMI,BP,BO,HR,SN,BG,RU,CHOL,UA,BHB,BF,TE,WP,MUCALE,BONE,BMR,PIW,FP]", "[\"BMI\",\"血压\",\"血氧\",\"心率\",\"步数\",\""
			+ "血糖\",\"尿常规\",\"总胆固醇\",\"尿酸\",\"血红蛋白\",\"血脂五项\",\"体温\",\"水分"
			+ "比\",\"肌肉\",\"骨量\",\"基础代谢率\",\"蛋白质\",\"体脂\"]", 
			"[{'itemId':'weValue','itemName':'体重'};{'itemId':'heValue','itemName':'身高'};{'itemId':'value','itemName':'BMI'}].[{'itemId':'sys','itemName':'收缩压'};{'itemId':'dia','itemName':'舒张压'}].[{'itemId':'value','itemName':'血氧饱和度'};{'itemId':'pr','itemName':'脉率'}].[{'itemId':'value','itemName':'心率'}].[{'itemId':'value','itemName':'步数'}].[{'itemId':'value','itemName':'血糖'}].[{'itemId':'urine_VitC','itemName':'维生素C'};{'itemId':'urine_leukocytes','itemName':'尿液白细胞'};{'itemId':'urine_bilirubin','itemName':'尿胆红素'};{'itemId':'urobilinogen','itemName':'尿胆原'};{'itemId':'urine_glucose','itemName':'尿葡萄糖'};{'itemId':'urine_specific_gravity','itemName':'尿比重'};{'itemId':'urine_protein','itemName':'尿蛋白'};{'itemId':'urine_ketone','itemName':'尿酮体'};{'itemId':'urine_occult_blood','itemName':'尿潜血'};{'itemId':'urine_nitrite','itemName':'亚硝酸盐'};{'itemId':'urine_PH','itemName':'尿酸碱度'}].[{'itemId':'value','itemName':'总胆固醇'}].[{'itemId':'value','itemName':'尿酸'}].[{'itemId':'hb','itemName':'血红蛋白浓度'};{'itemId':'hct','itemName':'红细比容'}].[{'itemId':'HDL','itemName':'高密度脂蛋白胆固醇'};{'itemId':'CE','itemName':'总胆固醇与高低密度比值'};{'itemId':'TG','itemName':'甘油三酯'};{'itemId':'LDL','itemName':'低密度脂蛋白胆固醇'};{'itemId':'chol','itemName':'总胆固醇'}].[{'itemId':'value','itemName':'体温'}].[{'itemId':'value','itemName':'水分比'}].[{'itemId':'value','itemName':'肌肉'}].[{'itemId':'value','itemName':'骨量'}].[{'itemId':'value','itemName':'基础代谢率'}].[{'itemId':'value','itemName':'蛋白质'}].[{'itemId':'value','itemName':'体脂'}].[{'itemId':'vfp','itemName':'内脏脂肪数'};{'itemId':'fc','itemName':'脂肪控制量'}]"),
	/**
	 * 检测数据 END
	 */
								
	/**
	 * 免疫定量分析仪  START
	 */
	
	cTnI("心肌肌钙蛋白I", "", "ng/ml"),
	NT_proBNP("N-端脑利钠肽前体", "", "pg/ml"),
	
	CRP("超敏C反应蛋白", "", "mg/L"),
	a_hs_CRP("超敏C反应蛋白", "", "mg/L"),
	a_CRP("C反应蛋白", "", "mg/L"),
	
	cTnI_NT_proBNP("", "", ""),
	b_cTnI("心肌肌钙蛋白I", "", "ng/ml"),
	b_NT_proBNP("N-端脑利钠肽前体", "", "pg/ml"),
	
	cTnI_CKMB_Myo("", "", ""),
	CK_MB("肌酸激酶同工酶", "", "ng/ml"),
	c_Myo("肌红蛋白", "", "mg/L"),
	
	D_Dimer("D-二聚体", "", "mg/L"),
	
	mAlb("微量白蛋白", "", "mg/L"),
	CysC("胱抑素C", "", "mg/L"),
	β2_MG("β2-微球蛋白", "", "mg/L"),
	hs_CRP ("高敏C反应蛋白", "", "mg/L"),
	HCG("人绒毛膜促性腺激素", "", "mIU/ml"),
	H_FABP("心型脂肪酸结合蛋白", "", "ng/ml"),
	
	PCT_CRP("", "", ""),
	PCT("降钙素原", "", "ng/ml"),
	
	CK_MB_cTnI_H_FABP("", "", ""),
	d_H_FABP("心型脂肪酸结合蛋白", "", "ng/ml"),
	
	NT_proBNP_BNP("", "", ""),
	BNP("脑钠肽", "", "pg/ml"),
	
	NGAL_mAlb("", "", ""),
	NGAL("中性粒细胞明胶酶相关脂质运载蛋白", "", "ng/ml"),
	mALb("微量白蛋白", "", "mg/L"),
	
	HbA1c("糖化血红蛋白", "", "%"),
	
	h_cTnI("高敏心肌肌钙蛋白I", "", "ng/ml");
	/**
	 * 免疫定量分析仪  END
	 */
	
	/**
	 * 一体机检测项  START
	 */
//    NIBP_SYS("收缩压", "0-300", "mmHg"),    	// 血压-收缩压 有效范围
//    NIBP_DIA("舒张压", "0-300", "mmHg"),    	// 血压-舒张压 有效范围
//    NIBP_PR("脉率", "10-400", "bpm"),     	// 血压-脉率 有效范围
//
//    SpO2_SpO2("血氧饱和度", "0-100", "%"),		// 血氧-血氧饱和度 有效范围
//    SpO2_PR("脉率", "10-400", "bpm"),     		// 血氧-脉率 有效范围
//
//    TEMP("体温", "0.0-50.9", "℃"),       		// 体温-体温 有效范围
//
//    RU_LEU("白细胞", "-1 - 7", ""),           	// 尿常规-白细胞 有效范围
//    RU_NIT("亚硝酸盐", "-1 - 7", ""),           // 尿常规-亚硝酸盐 有效范围
//    RU_UBG("尿胆原", "-1 - 7", ""),           	// 尿常规-尿胆原 有效范围
//    RU_PRO("蛋白质", "-1 - 7", ""),           	// 尿常规-蛋白质 有效范围
//    RU_pH("酸碱度", "5.0 - 8.5", ""),         	// 尿常规-酸碱度 有效范围
//    RU_BLD("潜血", "-1 - 7", ""),           		// 尿常规-潜血 有效范围
//    RU_SG("比重", "1.000 - 1.030", ""),     		// 尿常规-比重 有效范围
//    RU_KET("酮体", "-1 - 7", ""),           		// 尿常规-酮体 有效范围
//    RU_BIL("胆红素", "-1 - 7", ""),           	// 尿常规-胆红素 有效范围
//    RU_GLU("葡萄糖", "-1 - 7", ""),           	// 尿常规-葡萄糖 有效范围
//    RU_C("维生素C", "-1 - 7", ""),             	// 尿常规-维生素C 有效范围
//
//    GLU_GLU("血糖", "1.1 - 33.3", "mmol/L"),            	// 血糖-血糖 有效范围
//
//    CHOL_CHOL("总胆固醇", "2.59 - 10.35", "mmol/L"),		// 总胆固醇-总胆固醇 有效范围
//
//    UA_UA("尿酸", "0.18 - 1.19", "mmol/L"),             	// 尿酸-尿酸 有效范围
//
//    HB_HB("血红蛋白", "7.0 - 26.0", "g/dL"),				// 血红蛋白-血红蛋白 有效范围
//    HB_HCT("红细胞比容", "20.0 - 75.0", "%"),				// 血红蛋白-红细胞比容 有效范围
//             						
//    BMI_Height("身高", "0 - 250", "cm"),                	// 健康指数-height 有效范围
//    BMI_Weight("体重", "0 - 250", "kg"),                	// 健康指数-Weight 有效范围
//
//    BMI_fp("体脂百分比", "", "%"),								// 体脂百分比
//    BMI_wp( "水分比", "", "%"),                         		// 水分比
//    BMI_mucale("肌肉", "", "kg"),                    			// 肌肉
//    BMI_fat("脂肪", "", "kg"),                       			// 脂肪
//    BMI_bmr("基础代谢率", "", "Kcal/d"),                   	// 基础代谢率
//    BMI_sm("标准体重", "", "kg"),                        		// 标准体重
//    BMI_fc("脂肪控制量", "", "kg"),                        	// 脂肪控制量
//    BMI_mc("肌肉控制量", "", "kg"),                        	// 肌肉控制量
//    BMI_wc("体重控制量", "", "kg"),                        	// 体重控制量
//    BMI_bone("骨质量", "", "kg"),                      		// 骨质量
//    BMI_lbm("去脂体重", "", "kg"),                       		// 去脂体重
//    BMI_piw("蛋白质质量", "", "kg"),                       	// 蛋白质质量
//
//    Lipid_CHOL("总胆固醇", "2.60 - 10.36", "mmol/L"),       	// 血脂-总胆固醇 有效范围
//    BMI_TG("甘油三酯", "0.56 - 5.65", "mmol/L"),            	// 血脂-甘油三酯 有效范围
//    BMI_HDL("高密度脂蛋白胆固醇", "0.39 - 2.59", "mmol/L"),	// 血脂-高密度脂蛋白胆固醇 有效范围
//
//    WBC("", "3.5 - 10.0", "10^9/L"),
//    RBC("", "3.50 - 6.00", "10^12/L"),
//    HB("", "110 - 175", "g/L"),
//    HCT("", "35.0 - 54.0", "%"),
//    MCV("", "80.0 - 100.0", "fL"),
//    MCH("", "26.0 - 34.0", "pg"),
//    MCHC("", "315 - 360", "g/L"),
//    PLT("", "100 - 350", "10^9/L"),
//    LYM_ratio("", "20.0 - 40.0", "%"),
//    MID_ratio("", "1.0 - 15.0", "%"),
//    GRAN_ratio("", "50.0 - 70.0", "%"),
//    LYM("", "0.6 - 4.1", "10^9/L"),
//    MID("", "0.1 - 1.8", "10^9/L"),
//    GRAN("", "2.0 - 7.8", "10^9/L"),
//    RDW_SD("", "35.0 - 56.0", "fL"),
//    RDW_CV("", "11.0 - 16.0", "%"),
//    PDW("", "9.0 - 17.0", "fL"),
//    MPV("", "6.5 - 12.0", "fL"),
//    r_PCT("", "0.10 - 0.28", "%"),
//    P_LCR("", "11.0 - 45.0", ""),
//    P_LCC("", "11 - 135", "10^9/L"),
//
//    cTnl("", "0.50 - 50.00", "ng/ml"),
//    NT_proBNP("", "100 - 35000", "pg/ml"),
//    D_Dimer("", "0.10 - 10.00", "mg/L"),
//    CK_MB("", "2.5 - 80.00", "ng/ml"),
//    Myo("", "30.0 - 1000.0", "ng/ml"),
//    H_FABP("", "1.00 - 120.00", "ng/ml"),
//    PCT("", "0.10 - 50.00", "ng/ml"),
//    CRP("", "0.50 - 200.0", "mg/L"),
//    hs_CRP("", "0.5 - 5.0", "mg/L"),
//    mALb("", "10.0 - 200.0", "mg/L"),
//    CysC("", "0.50 - 10.00", "mg/L"),
//    two_MG("", "0.50 - 20.00", "mg/L"),
//    HbA1c("", "3.10 - 18.50", "%"),
//    NGAL("", "50.0 - 5000.0", "ng/ml"),
//    HCG("", "5.0 - 10000.0", "ng/ml"),
//    BNP("", "5.0 - 1000.0", "pg/ml"),
//    U_CRP("", "0.5 - 5.0", "mg/L");
	/**
	 * 一体机检测项  END
	 */
	
    private String name;			// 名称
    private String desc;			// 描述
    private String unit;			// 单位

    public String getName() {
        return name;
    }
    public String getDesc() {
        return desc;
    }
    public String getUnit() {
        return unit;
    }

    DeviceItemEnum(String name, String desc, String unit){
        this.name = name;
        this.desc = desc;
        this.unit = unit;
    }
}